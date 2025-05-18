package deu.controller;

import deu.model.entity.ReservationStatus;
import deu.service.ReservationService;
import deu.model.dto.response.BasicResponse;

import java.time.LocalDateTime;

// 예약 관리 컨트롤러
public class ReservationManagementController {
    private static final ReservationManagementController instance = new ReservationManagementController();

    private ReservationManagementController() {}

    public static ReservationManagementController getInstance() { return instance; }

    // ✅ 서비스 인스턴스
    private final ReservationService reservationService = new ReservationService();

    // 각 메서드 앞에 handle를 꼭 붙혀주세요 (일관성 유지)

    // TODO: 예약 삭제 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 수정 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 상태 변경 (관리자가 예약 수락/거절 시 호출)
    public BasicResponse handleUpdateReservationStatus(String userId, LocalDateTime startTime, ReservationStatus status) {
        return reservationService.updateReservationStatus(userId, startTime, status);
    }

    // TODO: 사용자 예약 조회 (성공/실패 코드와 금일 ~ 일주일간의 데이터 반환)

    // TODO: 모든 예약 조회 (성공/실패 코드와 금일 일주일간의 데이터 반환)
}
