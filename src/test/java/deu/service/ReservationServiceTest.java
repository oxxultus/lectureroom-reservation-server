package deu.service;

import deu.model.dto.request.data.reservation.*;
import deu.model.entity.RoomReservation;
import deu.model.dto.response.BasicResponse;
import deu.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ReservationService 단위 테스트")
public class ReservationServiceTest {

    private ReservationService service;
    private ReservationRepository mockRepo;
    private MockedStatic<ReservationRepository> mockedStatic;

    @BeforeEach
    void setUp() {
        mockRepo = mock(ReservationRepository.class);
        mockedStatic = mockStatic(ReservationRepository.class);
        mockedStatic.when(ReservationRepository::getInstance).thenReturn(mockRepo);

        service = ReservationService.getInstance();
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    @DisplayName("정상 예약 생성")
    void testCreateRoomReservationSuccess() {
        RoomReservationRequest req = getRequest();
        when(mockRepo.findByUser("S123")).thenReturn(new ArrayList<>());
        when(mockRepo.isDuplicate(anyString(), anyString(), anyString())).thenReturn(false);

        BasicResponse response = service.createRoomReservation(req);

        assertEquals("200", response.code);
        assertEquals("예약이 완료되었습니다.", response.data);
    }

    @Test
    @DisplayName("7일 내 예약 5건 초과 시 거절")
    void testCreateRoomReservationOverLimit() {
        RoomReservationRequest req = getRequest();
        List<RoomReservation> existing = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RoomReservation r = new RoomReservation();
            r.setDate(LocalDate.now().plusDays(i).toString());
            existing.add(r);
        }

        when(mockRepo.findByUser("S123")).thenReturn(existing);

        BasicResponse response = service.createRoomReservation(req);

        assertEquals("403", response.code);
        assertTrue(response.data.toString().contains("최대 5개의 예약"));
    }

    @Test
    @DisplayName("사용자 동일 시간대 중복 예약 시 거절")
    void testCreateRoomReservationDuplicateTime() {
        RoomReservationRequest req = getRequest();
        RoomReservation r = new RoomReservation();
        r.setDate(req.getDate());
        r.setStartTime(req.getStartTime());

        when(mockRepo.findByUser("S123")).thenReturn(List.of(r));

        BasicResponse response = service.createRoomReservation(req);

        assertEquals("409", response.code);
        assertTrue(response.data.toString().contains("이미 예약"));
    }

    @Test
    @DisplayName("강의실 동일 시간대 중복 예약 시 거절")
    void testCreateRoomReservationRoomConflict() {
        RoomReservationRequest req = getRequest();
        when(mockRepo.findByUser("S123")).thenReturn(new ArrayList<>());
        when(mockRepo.isDuplicate(anyString(), anyString(), anyString())).thenReturn(true);

        BasicResponse response = service.createRoomReservation(req);

        assertEquals("409", response.code);
        assertTrue(response.data.toString().contains("다른 예약"));
    }

    @Test
    @DisplayName("자기 예약 정상 삭제")
    void testDeleteRoomReservationFromUserSuccess() {
        RoomReservation r = new RoomReservation();
        r.setId("resv123");
        r.setNumber("S123"); // ✅ 요청자와 일치하도록 정확히 설정

        when(mockRepo.findById("resv123")).thenReturn(r);
        when(mockRepo.deleteById("resv123")).thenReturn(true); // ✅ 삭제 동작을 명시적으로 설정

        DeleteRoomReservationRequest req = new DeleteRoomReservationRequest("S123", "resv123");
        BasicResponse response = service.deleteRoomReservationFromUser(req);

        assertEquals("200", response.code);
        assertEquals("예약이 삭제되었습니다.", response.data.toString());
    }

    @Test
    @DisplayName("다른 사용자 예약 삭제 시 거절")
    void testDeleteRoomReservationFromUserForbidden() {
        RoomReservation r = new RoomReservation();
        r.setId("resv123");
        r.setNumber("DIFFERENT");

        when(mockRepo.findById("resv123")).thenReturn(r);

        DeleteRoomReservationRequest req = new DeleteRoomReservationRequest("resv123", "S123");
        BasicResponse response = service.deleteRoomReservationFromUser(req);

        assertEquals("404", response.code);
    }

    @Test
    @DisplayName("존재하지 않는 예약 삭제 시 실패")
    void testDeleteRoomReservationNotFound() {
        when(mockRepo.findById("resv123")).thenReturn(null);

        DeleteRoomReservationRequest req = new DeleteRoomReservationRequest("resv123", "S123");
        BasicResponse response = service.deleteRoomReservationFromUser(req);

        assertEquals("404", response.code);
    }

    @Test
    @DisplayName("관리자 예약 삭제 성공")
    void testDeleteRoomReservationFromManagement() {
        when(mockRepo.deleteById("resv123")).thenReturn(true);

        BasicResponse response = service.deleteRoomReservationFromManagement("resv123");

        assertEquals("200", response.code);
        verify(mockRepo).saveToFile();
    }

    @Test
    @DisplayName("예약 상태 변경 성공")
    void testChangeRoomReservationStatus() {
        RoomReservation r = new RoomReservation();
        r.setId("resv123");
        r.setStatus("대기");

        when(mockRepo.findById("resv123")).thenReturn(r);

        BasicResponse response = service.changeRoomReservationStatus("resv123");

        assertEquals("200", response.code);
        assertEquals("승인", r.getStatus());
    }

    @Test
    @DisplayName("예약 수정 성공")
    void testModifyRoomReservation() {
        RoomReservation existing = new RoomReservation();
        existing.setId("resv123");
        when(mockRepo.findById("resv123")).thenReturn(existing);

        RoomReservationRequest req = new RoomReservationRequest();
        req.setId("resv123");
        req.setTitle("변경된 제목");
        req.setBuildingName("신관");
        req.setLectureRoom("202");
        req.setFloor("2층");
        req.setDate(LocalDate.now().toString());
        req.setStartTime("09:00");
        req.setEndTime("10:00");
        req.setDayOfTheWeek("월");

        BasicResponse response = service.modifyRoomReservation(req);

        assertEquals("200", response.code);
        verify(mockRepo).saveToFile();
    }

    @Test
    @DisplayName("강의실 기준 주간 예약 조회")
    void testWeekRoomReservationByLectureroom() {
        RoomReservationLocationRequest req = new RoomReservationLocationRequest("신관", "2층", "202");
        RoomReservation r = new RoomReservation();
        r.setBuildingName("신관");
        r.setFloor("2층");
        r.setLectureRoom("202");
        r.setDate(LocalDate.now().toString());
        r.setStartTime("09:00");

        when(mockRepo.findAll()).thenReturn(List.of(r));

        BasicResponse response = service.weekRoomReservationByLectureroom(req);

        assertEquals("200", response.code);
        RoomReservation[][] result = (RoomReservation[][]) response.data;
        assertNotNull(result[0][0]);
    }

    @Test
    @DisplayName("사용자 기준 주간 예약 조회")
    void testWeekRoomReservationByUser() {
        String number = "S123";
        RoomReservation r = new RoomReservation();
        r.setNumber(number);
        r.setDate(LocalDate.now().toString());
        r.setStartTime("09:00");

        when(mockRepo.findByUser(number)).thenReturn(List.of(r));

        BasicResponse response = service.weekRoomReservationByUserNumber(number);
        assertEquals("200", response.code);

        RoomReservation[][] result = (RoomReservation[][]) response.data;
        assertNotNull(result[0][0]);
    }

    @Test
    @DisplayName("사용자 예약 리스트 조회")
    void testGetReservationsByUser() {
        String number = "S123";
        RoomReservation r = new RoomReservation();
        r.setNumber(number);
        r.setDate(LocalDate.now().toString());

        when(mockRepo.findByUser(number)).thenReturn(List.of(r));

        BasicResponse response = service.getReservationsByUser(number);
        assertEquals("200", response.code);
        assertTrue(((List<?>) response.data).contains(r));
    }

    @Test
    @DisplayName("대기 상태 예약 목록 반환")
    void testFindAllRoomReservation() {
        RoomReservation r1 = new RoomReservation();
        r1.setStatus("대기");

        RoomReservation r2 = new RoomReservation();
        r2.setStatus("승인");

        when(mockRepo.findAll()).thenReturn(List.of(r1, r2));

        BasicResponse response = service.findAllRoomReservation();

        assertEquals("200", response.code);
        List<?> result = (List<?>) response.data;
        assertTrue(result.contains(r1));
        assertFalse(result.contains(r2));
    }

    // 헬퍼
    private RoomReservationRequest getRequest() {
        RoomReservationRequest req = new RoomReservationRequest();
        req.setBuildingName("공학관");
        req.setFloor("3층");
        req.setLectureRoom("301호");
        req.setNumber("S123");
        req.setTitle("스터디");
        req.setDescription("시험 준비");
        req.setDate(LocalDate.now().toString());
        req.setDayOfTheWeek("월");
        req.setStartTime("09:00");
        req.setEndTime("10:00");
        return req;
    }
}