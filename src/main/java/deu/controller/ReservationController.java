package deu.controller;

import deu.service.ReservationService;

// 예약 컨트롤러
public class ReservationController {
    private static final ReservationController instance = new ReservationController();

    private ReservationController() {}

    public static ReservationController getInstance() {
        return instance;
    }

    private final ReservationService reservationService = new ReservationService();

}
