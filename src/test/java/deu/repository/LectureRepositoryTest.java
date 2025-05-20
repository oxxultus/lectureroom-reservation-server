package deu.repository;

import deu.model.entity.Lecture;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LectureRepositoryTest {

    private static final String TEST_ID_1 = "TEST_001";
    private static final String TEST_ID_2 = "TEST_002";
    private static final String TEST_ID_3 = "TEST_003";

    private final LectureRepository repo = LectureRepository.getInstance();
    private final File file = new File(System.getProperty("user.dir") + "/data/lectures.yaml");

    @BeforeEach
    void clearBefore() {
        // 테스트 시작 전에 강의 삭제 (파일은 유지)
        repo.deleteById(TEST_ID_1);
        repo.deleteById(TEST_ID_2);
        repo.deleteById(TEST_ID_3);
    }

    @DisplayName("강의 저장 시 파일이 생성되는지 확인")
    @Test
    @Order(1)
    void test1_saveLecture_createsFile() {
        Lecture lecture = createDummyLecture(TEST_ID_1);
        String result = repo.save(lecture);
        assertEquals("200", result, "강의 저장 성공 코드 반환 확인");
        assertTrue(file.exists(), "파일이 생성되어 있어야 합니다.");
    }

    @DisplayName("ID로 강의 조회 시 정확한 데이터가 반환되는지 확인")
    @Test
    @Order(2)
    void test2_findLectureById_returnsCorrectData() {
        repo.save(createDummyLecture(TEST_ID_1));
        Optional<Lecture> result = repo.findById(TEST_ID_1);
        assertTrue(result.isPresent());
        assertEquals("단위 테스트 강의", result.get().getTitle());
        assertEquals("김교수", result.get().getProfessor());
    }

    @DisplayName("ID 존재 여부 확인 시 200 반환 여부 확인")
    @Test
    @Order(3)
    void test3_existsById_returns200() {
        repo.save(createDummyLecture(TEST_ID_1));
        String result = repo.existsById(TEST_ID_1);
        assertEquals("200", result);
    }

    @DisplayName("findAll 결과에 저장된 강의가 포함되는지 확인")
    @Test
    @Order(4)
    void test4_findAll_containsLecture() {
        repo.save(createDummyLecture(TEST_ID_1));
        List<Lecture> all = repo.findAll();
        assertTrue(all.stream().anyMatch(l -> l.getId().equals(TEST_ID_1)));
    }

    @DisplayName("ID로 강의 삭제 시 목록에서 제거되는지 확인")
    @Test
    @Order(5)
    void test5_deleteById_removesLecture() {
        repo.save(createDummyLecture(TEST_ID_1));
        String result = repo.deleteById(TEST_ID_1);
        assertEquals("200", result, "삭제 성공 코드 반환 확인");
        assertFalse(repo.findById(TEST_ID_1).isPresent(), "삭제 후 해당 ID는 존재하지 않아야 함");
    }

    @DisplayName("여러 개의 강의 저장 후 모두 저장되었는지 확인")
    @Test
    @Order(6)
    void test6_saveMultipleLectures_allSaved() {
        Lecture lec1 = createDummyLecture(TEST_ID_1);
        Lecture lec2 = createDummyLecture(TEST_ID_2);
        Lecture lec3 = createDummyLecture(TEST_ID_3);

        assertEquals("200", repo.save(lec1));
        assertEquals("200", repo.save(lec2));
        assertEquals("200", repo.save(lec3));

        List<Lecture> all = repo.findAll();

        assertTrue(all.stream().anyMatch(l -> l.getId().equals(TEST_ID_1)));
        assertTrue(all.stream().anyMatch(l -> l.getId().equals(TEST_ID_2)));
        assertTrue(all.stream().anyMatch(l -> l.getId().equals(TEST_ID_3)));
    }

    private Lecture createDummyLecture(String id) {
        Lecture lec = new Lecture();
        lec.setId(id);
        lec.setTitle("단위 테스트 강의");
        lec.setLectureroom("A101");
        lec.setBuilding("테스트관");
        lec.setFloor("1층");
        lec.setProfessor("김교수");
        lec.setStartTime("10:00");
        lec.setEndTime("11:15");
        return lec;
    }

    @AfterAll
    void cleanup() {
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println(deleted ? "[정리] 파일 삭제 완료" : "[정리] 파일 삭제 실패");
        }

        File dir = file.getParentFile();
        if (dir.exists() && dir.isDirectory() && dir.list().length == 0) {
            boolean dirDeleted = dir.delete();
            System.out.println(dirDeleted ? "[정리] 빈 디렉토리 삭제 완료" : "[정리] 디렉토리 삭제 실패");
        }
    }
}