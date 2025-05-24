package deu.service;

import deu.model.dto.request.data.reservation.DeleteRoomReservationRequest;
import deu.model.dto.request.data.reservation.RoomReservationLocationRequest;
import deu.model.dto.request.data.reservation.RoomReservationRequest;
import deu.model.entity.RoomReservation;
import deu.repository.ReservationRepository;
import deu.model.dto.response.BasicResponse;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class ReservationService {

    // 싱글톤 인스턴스
    @Getter
    private static final ReservationService instance = new ReservationService();

    private ReservationService() {}


    // TODO: 위 서비를 수정해서 아래 서비스 메서드에 맞게 수정해주세요.
    //  1. 해당 서비스에서 반환값을 BasicResponse로 맞춰서 컨트롤러로 반환해주세요.

    // 사용자 관점 ========================================================================================================

    // 예약 신청
    public BasicResponse createRoomReservation(RoomReservationRequest payload) {
        try {
            // RoomReservation 엔티티 생성
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setBuildingName(payload.getBuildingName());
            roomReservation.setFloor(payload.getFloor());
            roomReservation.setLectureRoom(payload.getLectureRoom());
            roomReservation.setNumber(payload.getNumber());
            roomReservation.setTitle(payload.getTitle());
            roomReservation.setDescription(payload.getDescription());
            roomReservation.setDate(payload.getDate());
            roomReservation.setDayOfTheWeek(payload.getDayOfTheWeek());
            roomReservation.setStartTime(payload.getStartTime());
            roomReservation.setEndTime(payload.getEndTime());

            ReservationRepository repo = ReservationRepository.getInstance();

            // 날짜 필터: 오늘부터 7일간
            LocalDate today = LocalDate.now();
            LocalDate maxDate = today.plusDays(6);

            List<RoomReservation> userReservations = repo.findByUser(payload.getNumber());

            // 예약 수 제한
            long countWithin7Days = userReservations.stream()
                    .filter(r -> {
                        try {
                            LocalDate date = LocalDate.parse(r.getDate());
                            return !date.isBefore(today) && !date.isAfter(maxDate);
                        } catch (Exception e) {
                            return false; // 날짜 파싱 실패한 항목은 무시
                        }
                    })
                    .count();

            if (countWithin7Days >= 5) {
                return new BasicResponse("403", "오늘부터 7일 간 최대 5개의 예약만 가능합니다.");
            }

            // 동일 시간 사용자 중복 예약 체크
            for (RoomReservation r : userReservations) {
                if (r.getDate().equals(payload.getDate()) &&
                        r.getStartTime().equals(payload.getStartTime())) {
                    return new BasicResponse("409", "같은 시간대에 이미 예약이 존재합니다.");
                }
            }

            // 강의실 동일 시간 중복 체크
            boolean isDup = repo.isDuplicate(
                    roomReservation.getDate(),
                    roomReservation.getStartTime(),
                    roomReservation.getLectureRoom()
            );

            if (isDup) {
                return new BasicResponse("409", "해당 시간에 다른 예약이 존재합니다.");
            }

            // 최종 저장
            repo.save(roomReservation);
            return new BasicResponse("200", "예약이 완료되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse("500", "서버 오류: " + e.getMessage());
        }
    }

    // 개인별 예약 삭제 TODO: number 와 id에 해당하는 RoomReservation 의 number가 동일하면 삭제 / 다르면 비정상적인 접근 처리
    public BasicResponse deleteRoomReservationFromUser(DeleteRoomReservationRequest payload) {
        RoomReservation target = ReservationRepository.getInstance().findById(payload.roomReservationId);

        if (target == null) {
            return new BasicResponse("404", "예약을 찾을 수 없습니다.");
        }

        if (!target.getNumber().equals(payload.number)) {
            return new BasicResponse("403", "본인의 예약만 삭제할 수 있습니다.");
        }

        ReservationRepository.getInstance().deleteById(payload.roomReservationId);
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

        return new BasicResponse("200", schedule);
    }

    // 사용자별 예약 리스트 조회 TODO: 동일하게 당일 + 6 일 뒤의정보를 반환해야 한다.
    public BasicResponse getReservationsByUser(String payload) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(6); // 오늘부터 6일 후까지 포함

        List<RoomReservation> reservations = ReservationRepository.getInstance()
                .findByUser(payload).stream()
                .filter(r -> {
                    try {
                        LocalDate date = LocalDate.parse(r.getDate(), formatter);
                        return !date.isBefore(today) && !date.isAfter(endDate); // today ≤ date ≤ today+6
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();

        return new BasicResponse("200", reservations);
    }


    // 통합 관점 ==========================================================================================================

    // 예약 수정
    public BasicResponse modifyRoomReservation(RoomReservationRequest payload) {
        try {
            ReservationRepository repo = ReservationRepository.getInstance();

            RoomReservation original = repo.findById(payload.getId());
            if (original == null) {
                return new BasicResponse("404", "예약을 찾을 수 없습니다.");
            }

            // 필드 업데이트
            original.setBuildingName(payload.getBuildingName());
            original.setFloor(payload.getFloor());
            original.setLectureRoom(payload.getLectureRoom());
            original.setTitle(payload.getTitle());
            original.setDescription(payload.getDescription());
            original.setDate(payload.getDate());
            original.setDayOfTheWeek(payload.getDayOfTheWeek());
            original.setStartTime(payload.getStartTime());
            original.setEndTime(payload.getEndTime());

            // 파일 저장
            repo.saveToFile();

            return new BasicResponse("200", "예약이 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse("500", "예약 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 건물 강의실별 주간 예약 조회 반환: 7x13 배열 (당일 +6일 까지) TODO: RoomReservation[7][13]
    public BasicResponse weekRoomReservationByLectureroom(RoomReservationLocationRequest payload) {
        RoomReservation[][] schedule = new RoomReservation[7][13];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        List<RoomReservation> reservations = ReservationRepository.getInstance().findAll().stream()
                .filter(r -> r.getBuildingName().equals(payload.building) &&
                        r.getFloor().equals(payload.floor) &&
                        r.getLectureRoom().equals(payload.lectureroom))
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

        return new BasicResponse("200", schedule);
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

        target.setStatus("승인");
        ReservationRepository.getInstance().saveToFile();
        return new BasicResponse("200", "예약 상태가 승인로 변경되었습니다.");
    }


    // 예약 상태가 "대기" 인 모든 예약 내역 반환
    public BasicResponse findAllRoomReservation() {
        List<RoomReservation> result = ReservationRepository.getInstance().findAll().stream()
                .filter(r -> "대기".equals(r.getStatus()))
                .toList();

        return new BasicResponse("200", result);
    }

    // =================================================================================================================
}
