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

    // 각 메서드 앞에 handle를 꼭 붙혀주세요 (알관성 유지)
    // TODO: 예약 요청 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 삭제 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 수정 (성공/실패 코드와 메시지 반환)

    // TODO: 사용자 예약 조회 (금일 ~ 일주일간의 데이터 반환)

    // TODO: 모든 예약 조회 (금일 일주일간의 데이터 반환)
}
