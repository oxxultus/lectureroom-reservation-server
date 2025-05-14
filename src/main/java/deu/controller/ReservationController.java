package deu.controller;

import deu.model.dto.request.ReservationRequest;
import deu.model.dto.response.BasicResponse;
import deu.service.ReservationService;

public class ReservationController {
    private static final ReservationController instance = new ReservationController();
    private final ReservationService reservationService = new ReservationService();

    private ReservationController() {}

    public static ReservationController getInstance() {
        return instance;
    }

    public BasicResponse reserve(ReservationRequest payload) {
        return reservationService.reserve(payload);
    }
}
