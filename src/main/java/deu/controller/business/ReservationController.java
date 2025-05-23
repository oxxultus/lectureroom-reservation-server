package deu.controller.business;

import deu.model.dto.request.data.reservation.DeleteRoomReservationRequest;
import deu.model.dto.request.data.reservation.RoomReservationRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.RoomReservation;
import lombok.Getter;

// 예약 컨트롤러
public class ReservationController {
    @Getter
    private static final ReservationController instance = new ReservationController();

    private ReservationController() {}

    // private final ReservationService reservationService = new ReservationService();

    // 예약 신청
    public BasicResponse handleAddRoomReservation(RoomReservation payload) {
        return null;
    }

    // 개인별 예약 삭제 TODO: String number, String id를 감싸는 DTO 추가 해야됨, number 와 id에 해당하는 예약의 number가 동일하면삭제
    public BasicResponse handlDeleteRoomReservation(DeleteRoomReservationRequest payload) {
        return null;
    }

    // 게인별 전체 예약 리스트 조회
    public BasicResponse handleUserRoomReservationList(String payload) {return null;}

    // 개인별 주간 예약 배열 조회 (당일 ~ +6일) TODO: RoomReservation[7][13]
    public BasicResponse handleWeekRoomReservationByUserNumber(String payload) {
        return null;
    }

    // 어래 기능은 관리자에서도 동일하다.=======================================================================================

    // 예약 수정
    public BasicResponse handleModifyRoomReservation(RoomReservation payload) {
        return null;
    }

    // 건물, 층, 강의실별 주간 예약 배열 조회 (당일 +6일 까지) TODO: RoomReservation[7][13]
    public BasicResponse handleWeekRoomReservationByLectureroom(RoomReservationRequest paylaod) {
        return null;
    }
}
