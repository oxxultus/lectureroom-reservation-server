package deu.controller.business;

import deu.model.dto.response.BasicResponse;
import deu.model.entity.RoomReservation;

// 예약 관리 컨트롤러
public class ReservationManagementController {
    private static final ReservationManagementController instance = new ReservationManagementController();

    private ReservationManagementController() {}

    public static ReservationManagementController getInstance() {
        return instance;
    }

    // private final ReservationService reservationService = new ReservationService();

    // 예약 수정
    public BasicResponse modifyRoomReservation(RoomReservation roomReservation) {
        return null;
    }

    // 관리자 예약 삭제
    public BasicResponse deleteRoomReservation(String id) {
        return null;
    }

    // 예약 상태가 "대기" 인 모든 예약 내역 반환
    public BasicResponse findAllRoomReservation() {
        return null;
    }

    // 예약 상태 변경 "대기 -> 완료"
    public BasicResponse changeRoomReservationStatus(String id){
        return null;
    }
}
