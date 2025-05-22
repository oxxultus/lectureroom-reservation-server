package deu.controller.business;

import deu.controller.business.ReservationController;
import deu.model.entity.RoomReservation;
import deu.model.dto.response.BasicResponse;
import deu.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    private ReservationService mockService;
    private ReservationController controller;

    @BeforeEach
    void setup() {
        mockService = mock(ReservationService.class);
        controller = ReservationController.getInstance(); // 이 라인만 남기고 try 문 제거
    }

    @Test
    @DisplayName("예약 생성 요청 처리 성공")
    void handleCreateReservation_success() {
        RoomReservation reservation = new RoomReservation();
        reservation.setDate("2024-05-21");
        reservation.setStartTime("09:00");
        reservation.setEndTime("10:00");

        BasicResponse mockResponse = new BasicResponse("200", "예약이 완료되었습니다.");
        when(mockService.createReservation(reservation)).thenReturn(mockResponse);

        // controller 내부 service가 mockService로 연결돼 있어야 정상 테스트됨
        BasicResponse result = controller.handleCreateReservation(reservation);

        assertNotNull(result);
        assertEquals("200", result.code);
        assertEquals("예약이 완료되었습니다.", result.data);
    }

    // 추가 테스트 예시
    @Test
    @DisplayName("예약 삭제 요청 성공")
    void handleDeleteReservation_success() {
        BasicResponse response = new BasicResponse("200", "예약이 삭제되었습니다.");
        when(mockService.deleteReservation("20230001", "2024-05-21", "09:00")).thenReturn(response);

        BasicResponse result = controller.handleDeleteReservation("20230001", "2024-05-21", "09:00");

        assertEquals("200", result.code);
        assertEquals("예약이 삭제되었습니다.", result.data);
    }
}
