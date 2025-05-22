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

    // 예약 삭제
    public BasicResponse deleteReservation(String userId, String date, String startTime) {
        List<RoomReservation> reservations = ReservationRepository.getInstance().findByUser(userId);

        RoomReservation target = null;
        for (RoomReservation r : reservations) {
            if (r.getDate().equals(date) && r.getStartTime().equals(startTime)) {
                target = r;
                break;
            }
        }

        if (target == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }

        ReservationRepository.getInstance().delete(target);
        return new BasicResponse("200", "예약이 삭제되었습니다.");
    }

    // 예약 수정
    public BasicResponse updateReservation(String userId, String date, String startTime, RoomReservation updated) {
        List<RoomReservation> reservations = ReservationRepository.getInstance().findByUser(userId);

        for (RoomReservation r : reservations) {
            if (r.getDate().equals(date) && r.getStartTime().equals(startTime)) {
                r.setLectureRoom(updated.getLectureRoom());
                r.setEndTime(updated.getEndTime());
                r.setStatus(updated.getStatus());
                r.setBuildingName(updated.getBuildingName());
                r.setFloor(updated.getFloor());
                r.setDayOfTheWeek(updated.getDayOfTheWeek());
                r.setNumber(updated.getNumber());

                ReservationRepository.getInstance().saveToFile();
                return new BasicResponse("200", "예약이 수정되었습니다.");
            }
        }

        return new BasicResponse("404", "예약을 찾을 수 없습니다.");
    }

    // 예약 상태 변경
    public BasicResponse updateReservationStatus(String userId, String date, String startTime, String newStatus) {
        List<RoomReservation> reservations = ReservationRepository.getInstance().findByUser(userId);

        for (RoomReservation r : reservations) {
            if (r.getDate().equals(date) && r.getStartTime().equals(startTime)) {
                r.setStatus(newStatus);
                ReservationRepository.getInstance().saveToFile();
                return new BasicResponse("200", "예약 상태가 변경되었습니다.");
            }
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
    public BasicResponse updateReservation(RoomReservation updated) {
        return updateReservation(
                updated.getNumber(),
                updated.getDate(),
                updated.getStartTime(),
                updated
        );
    }


}
