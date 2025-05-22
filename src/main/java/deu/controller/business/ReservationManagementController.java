package deu.controller.business;

import deu.service.ReservationService;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.RoomReservation;

import java.util.List;

// 예약 관리 컨트롤러 (관리자용)
public class ReservationManagementController {

    private static final ReservationManagementController instance = new ReservationManagementController();

    private final ReservationService reservationService = ReservationService.getInstance();

    private ReservationManagementController() {}

    public static ReservationManagementController getInstance() { return instance; }

    // 예약 삭제 (관리자용) - reservationId만 필요
    public BasicResponse handleDeleteReservation(String reservationId) {
        boolean deleted = reservationService.deleteReservationDirectly(reservationId);
        return deleted
                ? new BasicResponse("200", "예약이 삭제되었습니다.")
                : new BasicResponse("404", "예약을 찾을 수 없습니다.");
    }

    // 예약 수정 (관리자용) - RoomReservation 객체 전달
    public BasicResponse handleUpdateReservation(RoomReservation updated) {
        return reservationService.updateReservation(updated);
    }

    // 예약 상태 변경 (관리자용) - 예약 ID와 새로운 상태 전달
    public BasicResponse handleUpdateReservationStatus(String reservationId, String newStatus) {
        return reservationService.updateReservationStatus(reservationId, newStatus);
    }

    // 사용자 예약 조회 (금일 ~ 일주일)
    public List<RoomReservation> handleGetUpcomingReservationsByUser(String userId) {
        return reservationService.getUpcomingReservationsByUser(userId);
    }

    // 전체 예약 조회 (금일 ~ 일주일)
    public List<RoomReservation> handleGetUpcomingAllReservations() {
        return reservationService.getUpcomingAllReservations();
    }

}
