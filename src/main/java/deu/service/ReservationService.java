package deu.service;

import deu.model.dto.request.data.reservation.DeleteRoomReservationRequest;
import deu.model.dto.request.data.reservation.RoomReservationRequest;
import deu.model.entity.RoomReservation;
import deu.repository.ReservationRepository;
import deu.model.dto.response.BasicResponse;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservationService {

    // 싱글톤 인스턴스
    @Getter
    private static final ReservationService instance = new ReservationService();

    private ReservationService() {}

    // 예약 생성 - TODO: 아래 정리된 메서드로 수정
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

    // 전체 예약 조회 - TODO: 아래 정리된 메서드로 수정
    public List<RoomReservation> getAllReservations() {
        return ReservationRepository.getInstance().findAll();
    }

    // 사용자 예약 중 1주일 이내 - TODO: 아래 정리된 메서드로 수정
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

    // 전체 예약 중 1주일 이내 - TODO: 아래 정리된 메서드로 수정
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

    // 예약 삭제 - reservationId 기준 - TODO: 아래 정리된 메서드로 수정
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

    // 예약 수정 - RoomReservation 객체 전달 (id 포함) - TODO: 아래 정리된 메서드로 수정
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

    // 예약 상태 변경 - 관리자 - TODO: 아래 정리된 메서드로 수정
    public BasicResponse updateReservationStatus(String reservationId, String newStatus) {
        RoomReservation target = ReservationRepository.getInstance().findById(reservationId);
        if (target == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }

        target.setStatus(newStatus);
        ReservationRepository.getInstance().saveToFile();
        return new BasicResponse("200", "상태가 변경되었습니다.");
    }

    // 주간 7x13 배열 반환 - TODO: 아래 정리된 메서드로 수정
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
            } catch (Exception ignored) {}
        }

        return schedule;
    }

    // 예약 ID 기반 삭제 (관리자용) - TODO: 아래 정리된 메서드로 수정
    public boolean deleteReservationDirectly(String reservationId) {
        boolean deleted = ReservationRepository.getInstance().deleteById(reservationId);
        if (deleted) {
            ReservationRepository.getInstance().saveToFile();
        }
        return deleted;
    }

    // 예약 ID 기반 삭제 (관리자용) - TODO: 아래 정리된 메서드로 수정
    public BasicResponse deleteReservationById(String id) {
        boolean deleted = ReservationRepository.getInstance().deleteById(id);
        return deleted ? new BasicResponse("200", "예약이 삭제되었습니다.") :
                new BasicResponse("404", "예약을 찾을 수 없습니다.");
    }


    // TODO: 위 서비를 수정해서 아래 서비스 메서드에 맞게 수정해주세요.
    //  1. 해당 서비스에서 반환값을 BasicResponse로 맞춰서 컨트롤러로 반환해주세요.

    // 사용자 관점 ========================================================================================================

    // 예약 신청
    public BasicResponse createRoomReservation(RoomReservation roomReservation) {
        boolean isDup = ReservationRepository.getInstance().isDuplicate(
                roomReservation.getDate(),
                roomReservation.getStartTime(),
                roomReservation.getLectureRoom()
        );

        if (isDup) {
            return new BasicResponse("409", "중복된 예약입니다.");
        }

        ReservationRepository.getInstance().save(roomReservation);
        return new BasicResponse("200", "예약이 완료되었습니다.");
    }


    // 개인별 예약 삭제 TODO: number 와 id에 해당하는 RoomReservation 의 number가 동일하면 삭제 / 다르면 비정상적인 접근 처리
    public BasicResponse deleteRoomReservationFromUser(DeleteRoomReservationRequest payload) {
        RoomReservation target = ReservationRepository.getInstance().findById(payload.getRoomReservationId());

        if (target == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }

        if (!target.getNumber().equals(payload.getNumber())) {
            return new BasicResponse("403", "본인의 예약만 삭제할 수 있습니다.");
        }

        ReservationRepository.getInstance().deleteById(payload.getRoomReservationId());
        return new BasicResponse("200", "예약이 삭제되었습니다.");
    }



    // 개인별 주간 예약 조회 반환: 7x13 배열 (당일 ~ +6일) TODO: RoomReservation[7][13]
    public BasicResponse weekRoomReservationByUserNumber(String payload) {
        RoomReservation[][] schedule = new RoomReservation[7][13];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        List<RoomReservation> reservations = ReservationRepository.getInstance().findByUser(payload).stream()
                .filter(r -> {
                    try {
                        LocalDate date = LocalDate.parse(r.getDate(), formatter);
                        return !date.isBefore(today) && !date.isAfter(today.plusDays(6));
                    } catch (Exception e) {
                        return false;
                    }
                }).toList();

        for (RoomReservation r : reservations) {
            try {
                int dayIndex = (int) ChronoUnit.DAYS.between(today, LocalDate.parse(r.getDate(), formatter));
                int periodIndex = Integer.parseInt(r.getStartTime().split(":")[0]) - 9;
                if (dayIndex >= 0 && dayIndex < 7 && periodIndex >= 0 && periodIndex < 13) {
                    schedule[dayIndex][periodIndex] = r;
                }
            } catch (Exception ignored) {}
        }

        return new BasicResponse("200", "개인 주간 예약 조회 완료");
    }


    // 사용자별 예약 리스트 조회
    public BasicResponse getReservationsByUser(String payload) {
        List<RoomReservation> reservations = ReservationRepository.getInstance().findByUser(payload);
        return new BasicResponse("200", "예약 목록 조회 완료");
    }


    // 통합 관점 ==========================================================================================================

    // 예약 수정
    public BasicResponse modifyRoomReservation(RoomReservation payload) {
        RoomReservation original = ReservationRepository.getInstance().findById(payload.getId());
        if (original == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }

        original.setLectureRoom(payload.getLectureRoom());
        original.setEndTime(payload.getEndTime());
        original.setStatus(payload.getStatus());
        original.setBuildingName(payload.getBuildingName());
        original.setFloor(payload.getFloor());
        original.setDayOfTheWeek(payload.getDayOfTheWeek());
        original.setNumber(payload.getNumber());
        original.setDate(payload.getDate());
        original.setStartTime(payload.getStartTime());

        ReservationRepository.getInstance().saveToFile();
        return new BasicResponse("200", "예약이 수정되었습니다.");
    }


    // 건물 강의실별 주간 예약 조회 반환: 7x13 배열 (당일 +6일 까지) TODO: RoomReservation[7][13]
    public BasicResponse weekRoomReservationByLectureroom(RoomReservationRequest payload) {
        RoomReservation[][] schedule = new RoomReservation[7][13];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        List<RoomReservation> reservations = ReservationRepository.getInstance().findAll().stream()
                .filter(r -> r.getBuildingName().equals(payload.getBuilding()) &&
                        r.getFloor().equals(payload.getFloor()) &&
                        r.getLectureRoom().equals(payload.getLectureroom()))
                .filter(r -> {
                    try {
                        LocalDate date = LocalDate.parse(r.getDate(), formatter);
                        return !date.isBefore(today) && !date.isAfter(today.plusDays(6));
                    } catch (Exception e) {
                        return false;
                    }
                }).toList();

        for (RoomReservation r : reservations) {
            try {
                int dayIndex = (int) ChronoUnit.DAYS.between(today, LocalDate.parse(r.getDate(), formatter));
                int periodIndex = Integer.parseInt(r.getStartTime().split(":")[0]) - 9;
                if (dayIndex >= 0 && dayIndex < 7 && periodIndex >= 0 && periodIndex < 13) {
                    schedule[dayIndex][periodIndex] = r;
                }
            } catch (Exception ignored) {}
        }

        return new BasicResponse("200", "강의실 주간 예약 조회 완료");
    }


    // 관리자 관점 ========================================================================================================

    // 관리자 예약 삭제
    public BasicResponse deleteRoomReservationFromManagement(String payload) {
        boolean deleted = ReservationRepository.getInstance().deleteById(payload);
        if (deleted) {
            ReservationRepository.getInstance().saveToFile();
            return new BasicResponse("200", "예약이 삭제되었습니다.");
        }
        return new BasicResponse("404", "예약을 찾을 수 없습니다.");
    }


    // 예약 상태 변경 "대기 -> 완료"
    public BasicResponse changeRoomReservationStatus(String payload) {
        RoomReservation target = ReservationRepository.getInstance().findById(payload);
        if (target == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }

        target.setStatus("완료");
        ReservationRepository.getInstance().saveToFile();
        return new BasicResponse("200", "예약 상태가 완료로 변경되었습니다.");
    }


    // 예약 상태가 "대기" 인 모든 예약 내역 반환
    public BasicResponse findAllRoomReservation() {
        List<RoomReservation> result = ReservationRepository.getInstance().findAll().stream()
                .filter(r -> "대기".equals(r.getStatus()))
                .toList();

        return new BasicResponse("200", "대기 상태 예약 조회 완료");
    }


    // =================================================================================================================
}
