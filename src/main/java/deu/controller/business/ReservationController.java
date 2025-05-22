package deu.controller.business;

import deu.model.entity.RoomReservation;
import deu.model.dto.response.BasicResponse;
import deu.service.ReservationService;

import java.util.List;

// 예약 컨트롤러 (사용자용)
public class ReservationController {

    private static final ReservationController instance = new ReservationController();
    private ReservationService reservationService = ReservationService.getInstance();

    private ReservationController() {}

    public static ReservationController getInstance() {
        return instance;
    }

    // 예약 생성
    public BasicResponse handleCreateReservation(RoomReservation reservation) {
        return reservationService.createReservation(reservation);
    }

    // 예약 삭제 - 예약 ID와 사용자 ID만 필요
    public BasicResponse handleDeleteReservation(String reservationId) {
        return reservationService.deleteReservationById(reservationId);
    }

    // 예약 수정 - RoomReservation 객체 전체 전달 (ID 포함되어야 함)
    public BasicResponse handleUpdateReservation(RoomReservation updatedReservation) {
        return reservationService.updateReservation(updatedReservation);
    }

    // 사용자 예약 조회 (금일 ~ 7일)
    public List<RoomReservation> handleGetUpcomingReservationsByUser(String userId) {
        return reservationService.getUpcomingReservationsByUser(userId);
    }

    // 전체 예약 조회 (금일 ~ 7일)
    public List<RoomReservation> handleGetUpcomingAllReservations() {
        return reservationService.getUpcomingAllReservations();
    }

    // 강의실 주간 시간표 조회
    public RoomReservation[][] handleGetWeeklyReservations(String building, String floor, String room) {
        return reservationService.getWeeklyReservations(building, floor, room);
    }

    // ReservationController.java 내부
    public void setService(ReservationService service) {
        this.reservationService = service;
    }

    public BasicResponse handleDeleteReservation(String number, String date, String time) {
        return null;
    }
}
