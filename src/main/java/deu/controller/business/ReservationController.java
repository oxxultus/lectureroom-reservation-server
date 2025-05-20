package deu.controller.business;

import deu.model.entity.RoomReservation;
import deu.model.dto.response.BasicResponse;
import deu.service.ReservationService;

import java.util.List;

// 예약 컨트롤러
public class ReservationController {
    private static final ReservationController instance = new ReservationController();
    private final ReservationService reservationService = new ReservationService();

    private ReservationController() {}

    public static ReservationController getInstance() {
        return instance;
    }

    public BasicResponse handleCreateReservation(RoomReservation reservation) {
        return reservationService.createReservation(reservation);
    }

    public List<RoomReservation> handleGetUpcomingReservationsByUser(String userId) {
        return reservationService.getUpcomingReservationsByUser(userId);
    }

    public List<RoomReservation> handleGetUpcomingAllReservations() {
        return reservationService.getUpcomingAllReservations();
    }

    public BasicResponse handleDeleteReservation(String userId, String date, String startTime) {
        return reservationService.deleteReservation(userId, date, startTime);
    }

    // 주간 예약 시간표 반환
    public RoomReservation[][] handleGetWeeklyReservations(String building, String floor, String room) {
        return reservationService.getWeeklyReservations(building, floor, room);
    }

    // 각 메서드 앞에 handle를 꼭 붙혀주세요 (알관성 유지)
    // TODO: 예약 요청 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 삭제 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 수정 (성공/실패 코드와 메시지 반환)

    // TODO: 사용자 예약 조회 (금일 ~ 일주일간의 데이터 반환)

    // TODO: 모든 예약 조회 (금일 일주일간의 데이터 반환)
}