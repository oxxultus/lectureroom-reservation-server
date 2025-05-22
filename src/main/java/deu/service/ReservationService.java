package deu.service;

import deu.model.entity.RoomReservation;
import deu.repository.ReservationRepository;
import deu.model.dto.response.BasicResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservationService {

    // 싱글톤 인스턴스
    private static final ReservationService instance = new ReservationService();

    public ReservationService() {}

    public static ReservationService getInstance() {
        return instance;
    }

    // 예약 생성
    public BasicResponse createReservation(RoomReservation reservation) {
        boolean isDup = ReservationRepository.getInstance().isDuplicate(
                reservation.getDate(),
                reservation.getStartTime(),
                reservation.getLectureRoom()
        );

        if (isDup) {
            return new BasicResponse("409", "중복된 예약입니다.");
        }

        ReservationRepository.getInstance().save(reservation);
        return new BasicResponse("200", "예약이 완료되었습니다.");
    }

    // 전체 예약 반환
    public List<RoomReservation> getAllReservations() {
        return ReservationRepository.getInstance().findAll();
    }

    // 사용자별 예약 반환
    public List<RoomReservation> getReservationsByUser(String userId) {
        return ReservationRepository.getInstance().findByUser(userId);
    }

    // 사용자 예약 중 1주일 이내
    public List<RoomReservation> getUpcomingReservationsByUser(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate oneWeekLater = now.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return ReservationRepository.getInstance().findByUser(userId).stream()
                .filter(r -> {
                    try {
                        LocalDate reservationDate = LocalDate.parse(r.getDate(), formatter);
                        return !reservationDate.isBefore(now) && !reservationDate.isAfter(oneWeekLater);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();
    }

    // 전체 예약 중 1주일 이내
    public List<RoomReservation> getUpcomingAllReservations() {
        LocalDate now = LocalDate.now();
        LocalDate oneWeekLater = now.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return ReservationRepository.getInstance().findAll().stream()
                .filter(r -> {
                    try {
                        LocalDate reservationDate = LocalDate.parse(r.getDate(), formatter);
                        return !reservationDate.isBefore(now) && !reservationDate.isAfter(oneWeekLater);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();
    }

    // 예약 삭제 - reservationId 기준
    public BasicResponse deleteReservation(String reservationId, String userId, String time) {
        RoomReservation target = ReservationRepository.getInstance().findById(reservationId);
        if (target == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }
        if (!target.getNumber().equals(userId)) {
            return new BasicResponse("403", "본인의 예약만 삭제할 수 있습니다.");
        }

        ReservationRepository.getInstance().deleteById(reservationId);
        return new BasicResponse("200", "예약이 삭제되었습니다.");
    }

    // 예약 수정 - RoomReservation 객체 전달 (id 포함)
    public BasicResponse updateReservation(RoomReservation updated) {
        RoomReservation original = ReservationRepository.getInstance().findById(updated.getId());
        if (original == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }

        original.setLectureRoom(updated.getLectureRoom());
        original.setEndTime(updated.getEndTime());
        original.setStatus(updated.getStatus());
        original.setBuildingName(updated.getBuildingName());
        original.setFloor(updated.getFloor());
        original.setDayOfTheWeek(updated.getDayOfTheWeek());
        original.setNumber(updated.getNumber());
        original.setDate(updated.getDate());
        original.setStartTime(updated.getStartTime());

        ReservationRepository.getInstance().saveToFile();
        return new BasicResponse("200", "예약이 수정되었습니다.");
    }

    // 예약 상태 변경 - 관리자
    public BasicResponse updateReservationStatus(String reservationId, String newStatus) {
        RoomReservation target = ReservationRepository.getInstance().findById(reservationId);
        if (target == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }

        target.setStatus(newStatus);
        ReservationRepository.getInstance().saveToFile();
        return new BasicResponse("200", "상태가 변경되었습니다.");
    }

        return new BasicResponse("404", "예약을 찾을 수 없습니다.");
    }

    // 특정 강의실의 주간 예약 (7일 x 13교시)
    public RoomReservation[][] getWeeklyReservations(String building, String floor, String room) {
        RoomReservation[][] schedule = new RoomReservation[7][13];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusDays(7);

        List<RoomReservation> reservations = ReservationRepository.getInstance().findAll().stream()
                .filter(r -> r.getBuildingName().equals(building)
                        && r.getFloor().equals(floor)
                        && r.getLectureRoom().equals(room))
                .filter(r -> {
                    try {
                        LocalDate date = LocalDate.parse(r.getDate(), formatter);
                        return !date.isBefore(today) && !date.isAfter(oneWeekLater);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();

        for (RoomReservation r : reservations) {
            try {
                int dayIndex = (int) ChronoUnit.DAYS.between(today, LocalDate.parse(r.getDate(), formatter));
                int periodIndex = Integer.parseInt(r.getStartTime().split(":")[0]) - 9; // 9시 시작 기준
                if (dayIndex >= 0 && dayIndex < 7 && periodIndex >= 0 && periodIndex < 13) {
                    schedule[dayIndex][periodIndex] = r;
                }
            } catch (Exception e) {
                // skip invalid record
            }
        }

        return schedule;
    }

    // 예약 ID 기반 삭제 (관리자용)
    public boolean deleteReservationDirectly(String reservationId) {
        boolean deleted = ReservationRepository.getInstance().deleteById(reservationId);
        if (deleted) {
            ReservationRepository.getInstance().saveToFile();
        }
        return deleted;
    }

    public BasicResponse deleteReservationById(String id) {
        boolean deleted = ReservationRepository.getInstance().deleteById(id);
        return deleted ? new BasicResponse("200", "예약이 삭제되었습니다.") :
                new BasicResponse("404", "예약을 찾을 수 없습니다.");
    }

}
