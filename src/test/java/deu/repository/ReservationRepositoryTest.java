package deu.repository;

import deu.model.entity.RoomReservation;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReservationRepository 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationRepositoryTest {

    private ReservationRepository repository;

    @BeforeAll
    void initRepository() {
        repository = ReservationRepository.getInstance();
    }

    @BeforeEach
    void resetRepository() {
        repository.clear();
    }

    private RoomReservation createSampleReservation(String user, String date, String startTime, String room) {
        RoomReservation res = new RoomReservation();
        res.setNumber(user);
        res.setDate(date);
        res.setStartTime(startTime);
        res.setLectureRoom(room);
        res.setTitle("스터디 모임");
        res.setDescription("시험 준비 스터디");
        res.setBuildingName("정보관");
        res.setFloor("9");
        res.setEndTime("14:00");
        res.setStatus("대기");
        res.setDayOfTheWeek("FRIDAY");
        return res;
    }

    @Test
    @DisplayName("예약 저장 및 ID로 조회")
    void testSaveAndFindById() {
        RoomReservation res = createSampleReservation("S123", "2025-05-23", "13:00", "901");
        repository.save(res);

        RoomReservation result = repository.findById(res.getId()); // UUID 기반 조회
        assertNotNull(result);
        assertEquals("S123", result.getNumber());
    }

    @Test
    @DisplayName("사용자 ID로 예약 전체 조회")
    void testFindByUser() {
        repository.save(createSampleReservation("S124", "2025-05-24", "10:00", "902"));
        repository.save(createSampleReservation("S124", "2025-05-24", "11:00", "903"));

        List<RoomReservation> results = repository.findByUser("S124");
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("예약 ID로 삭제 기능")
    void testDeleteById() {
        RoomReservation res = createSampleReservation("S125", "2025-05-25", "14:00", "904");
        repository.save(res);

        boolean deleted = repository.deleteById(res.getId());
        assertTrue(deleted);
        assertNull(repository.findById(res.getId()));
    }

    @Test
    @DisplayName("예약 중복 체크")
    void testIsDuplicate() {
        repository.save(createSampleReservation("S126", "2025-05-26", "15:00", "905"));
        assertTrue(repository.isDuplicate("2025-05-26", "15:00", "905"));
        assertFalse(repository.isDuplicate("2025-05-26", "16:00", "905"));
    }

    @Test
    @DisplayName("전체 예약 목록 반환")
    void testFindAll() {
        RoomReservation res = createSampleReservation("S999", "2025-06-01", "12:00", "999");
        repository.save(res);

        List<RoomReservation> all = repository.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    @DisplayName("파일 생성 확인 테스트")
    void testFileIsCreatedOnSave() {
        RoomReservation res = createSampleReservation("S777", "2025-06-10", "13:00", "777");
        repository.save(res);

        File expectedFile = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + "reservations.yaml");
        System.out.println("📄 실제 저장 경로: " + expectedFile.getAbsolutePath());

        assertTrue(expectedFile.exists(), "❌ reservations.yaml 파일이 생성되지 않았습니다.");
    }
}