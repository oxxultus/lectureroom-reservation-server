package deu.service;

import deu.model.entity.RoomReservation;
import deu.model.dto.response.BasicResponse;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationServiceTest {

    private static final ReservationService service = ReservationService.getInstance();
    private static String createdReservationId;

    @Test
    @Order(1)
    void testCreateReservation_success() {
        RoomReservation reservation = new RoomReservation();
        reservation.setId("test-id-123");  // ✅ ID 설정 필수
        reservation.setNumber("20230001");
        reservation.setBuildingName("정보관");
        reservation.setFloor("9");
        reservation.setLectureRoom("911");
        reservation.setDate("2025-05-25");
        reservation.setDayOfTheWeek("일요일");
        reservation.setStartTime("13:00");
        reservation.setEndTime("14:00");
        reservation.setStatus("PENDING");

        BasicResponse response = service.createReservation(reservation);

        assertEquals("200", response.code);
        assertEquals("예약이 완료되었습니다.", response.data);

        createdReservationId = reservation.getId(); // ID 저장
    }

    @Test
    @Order(2)
    void testUpdateReservation_success() {
        RoomReservation updated = new RoomReservation();
        updated.setId(createdReservationId);
        updated.setNumber("20230001");
        updated.setBuildingName("정보관");
        updated.setFloor("9");
        updated.setLectureRoom("911");
        updated.setDate("2025-05-25");
        updated.setDayOfTheWeek("일요일");
        updated.setStartTime("13:00");
        updated.setEndTime("14:50");
        updated.setStatus("APPROVED");

        BasicResponse response = service.updateReservation(updated);

        assertEquals("200", response.code);
        assertEquals("예약이 수정되었습니다.", response.data);
    }

    @Test
    @Order(3)
    void testDeleteReservation_success() {
        BasicResponse response = service.deleteReservationById(createdReservationId);

        assertEquals("200", response.code);
        assertEquals("예약이 삭제되었습니다.", response.data);
    }
}
