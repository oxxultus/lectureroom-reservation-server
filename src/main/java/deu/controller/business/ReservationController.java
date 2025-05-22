package deu.controller.business;

import deu.model.entity.RoomReservation;
import deu.model.dto.response.BasicResponse;
import deu.service.ReservationService;

import java.util.List;

// 예약 컨트롤러
public class ReservationController {

    private static final ReservationController instance = new ReservationController();
    // 싱글턴 적용
    private final ReservationService reservationService = ReservationService.getInstance();

    private ReservationController() {}

    public static ReservationController getInstance() {
        return instance;
    }

    // 예약 생성
    public BasicResponse handleCreateReservation(RoomReservation reservation) {
        return reservationService.createReservation(reservation);
    }

    // 예약 삭제
    public BasicResponse handleDeleteReservation(String userId, String date, String startTime) {
        return reservationService.deleteReservation(userId, date, startTime);
    }

    // 예약 수정
    public BasicResponse handleUpdateReservation(RoomReservation updateReservation) {
        return reservationService.updateReservation(updateReservation);
    }

    // 사용자 예약 조회 (금일 ~ 일주일간)
    public List<RoomReservation> handleGetUpcomingReservationsByUser(String userId) {
        return reservationService.getUpcomingReservationsByUser(userId);
    }

    // 전체 예약 조회 (금일 ~ 일주일간)
    public List<RoomReservation> handleGetUpcomingAllReservations() {
        return reservationService.getUpcomingAllReservations();
    }

    // 주간 예약 시간표 반환
    public RoomReservation[][] handleGetWeeklyReservations(String building, String floor, String room) {
        return reservationService.getWeeklyReservations(building, floor, room);
    }

    public Object handleUpdateReservation(String ignoredNumber, String ignoredDate, String ignoredStartTime, RoomReservation ignoredRr) {
        return null;
    }
}
