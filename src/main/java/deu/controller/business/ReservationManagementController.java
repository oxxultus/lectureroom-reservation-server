package deu.controller.business;

import deu.service.ReservationService;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.RoomReservation;

import java.util.List;

// 예약 관리 컨트롤러
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

    // TODO: 예약 수정 (성공/실패 코드와 메시지 반환) 구현 완료
    public BasicResponse handleUpdateReservation(String userId, String date, String startTime, RoomReservation updated) {
        return reservationService.updateReservation(userId, date, startTime, updated);
    }

    // TODO: 예약 상태 변경 (관리자가 예약 수락/거절 시 호출) 구현 완료
    public BasicResponse handleUpdateReservationStatus(String userId, String date, String startTime, String status) {
        return reservationService.updateReservationStatus(userId, date, startTime, status);
    }

    // TODO: 사용자 예약 조회 (성공/실패 코드와 금일 ~ 일주일간의 데이터 반환) 구현 완료
    public List<RoomReservation> handleGetUpcomingReservationsByUser(String userId) {
        return reservationService.getUpcomingReservationsByUser(userId);
    }

    // TODO: 모든 예약 조회 (성공/실패 코드와 금일 일주일간의 데이터 반환) 구현 완료
    public List<RoomReservation> handleGetUpcomingAllReservations() {
        return reservationService.getUpcomingAllReservations();
    }
}
