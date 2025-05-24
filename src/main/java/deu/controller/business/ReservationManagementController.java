package deu.controller.business;

import deu.model.dto.request.data.reservation.RoomReservationRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.RoomReservation;
import deu.service.ReservationService;
import lombok.Getter;

// 예약 관리 컨트롤러
public class ReservationManagementController {
    @Getter
    private static final ReservationManagementController instance = new ReservationManagementController();

    private ReservationManagementController() {}

    private final ReservationService reservationService = ReservationService.getInstance();

    // 예약 수정
    public BasicResponse handleModifyRoomReservation(RoomReservationRequest payload) {
        return reservationService.modifyRoomReservation(payload);
    }

    // 관리자 예약 삭제
    public BasicResponse handleDeleteRoomReservation(String payload) {
        return reservationService.deleteRoomReservationFromManagement(payload);
    }

    // 예약 상태 변경 "대기 -> 완료"
    public BasicResponse handleChangeRoomReservationStatus(String payload) {
        return reservationService.changeRoomReservationStatus(payload);
    }

    // 예약 상태가 "대기" 인 모든 예약 내역 반환
    public BasicResponse handleFindAllRoomReservation() {
        return reservationService.findAllRoomReservation();
    }

}
