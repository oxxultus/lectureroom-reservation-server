package deu.repository;

import deu.model.dto.response.BasicResponse;
import deu.model.entity.User;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    private UserRepository repo;
    private final String TEST_ID = "T2025";
    private final String TEST_PASSWORD = "pw123";
    private final String TEST_NAME = "김영진";
    private final String TEST_MAJOR = "컴퓨터소프트웨어공학과";

    private final String DATA_DIR_PATH = System.getProperty("user.dir") + File.separator + "data";
    private final String TEST_FILE_PATH = DATA_DIR_PATH + File.separator + "users.yaml";

    @BeforeAll
    void init() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) file.delete();
        repo = UserRepository.getInstance();
    }

    @AfterAll
    void cleanup() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) file.delete();

        File dir = new File(DATA_DIR_PATH);
        if (dir.exists() && dir.isDirectory() && dir.list().length == 0) {
            boolean deleted = dir.delete();
            if (deleted) {
                System.out.println("[TestCleanup] data 디렉토리 삭제됨: " + dir.getAbsolutePath());
            }
        }
    }

    @BeforeEach
    void clearUser() {
        repo.deleteByNumber(TEST_ID);
    }

    @Test
    @Order(1)
    void testSaveSuccess() {
        BasicResponse response = repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        assertEquals("200", response.code);
    }

    @Test
    @Order(2)
    void testSaveDuplicate() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse response = repo.save(TEST_ID, "another", "이름", "전공");
        assertEquals("400", response.code);
    }

    @Test
    @Order(3)
    void testValidateSuccess() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse response = repo.validate(TEST_ID, TEST_PASSWORD);
        assertEquals("200", response.code);
    }

    @Test
    @Order(4)
    void testValidateWrongPassword() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse response = repo.validate(TEST_ID, "wrong");
        assertEquals("401", response.code);
    }

    @Test
    @Order(5)
    void testValidateNonexistentUser() {
        BasicResponse response = repo.validate("NONE", "pw");
        assertEquals("400", response.code);
    }

    @Test
    @Order(6)
    void testFindByNumberSuccess() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse response = repo.findByNumber(TEST_ID);
        assertEquals("200", response.code);
        assertTrue(response.data instanceof User);
    }

    @Test
    @Order(7)
    void testFindByNumberFail() {
        BasicResponse response = repo.findByNumber("UNKNOWN");
        assertEquals("404", response.code);
    }

    @Test
    @Order(8)
    void testUpdateSuccess() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse response = repo.update(TEST_ID, "newpw", "이순신", "AI학과");
        assertEquals("200", response.code);
    }

    @Test
    @Order(9)
    void testUpdateFail() {
        BasicResponse response = repo.update("NOPE", "pw", "이름", "전공");
        assertEquals("404", response.code);
    }

    @Test
    @Order(10)
    void testDeleteSuccess() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse response = repo.deleteByNumber(TEST_ID);
        assertEquals("200", response.code);
    }

    @Test
    @Order(11)
    void testDeleteFail() {
        BasicResponse response = repo.deleteByNumber("NOT_EXIST");
        assertEquals("404", response.code);
    }

    @Test
    @Order(12)
    void testFindAll() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse response = repo.findAll();
        assertEquals("200", response.code);
        assertTrue(response.data instanceof List);
    }

    @Test
    @Order(13)
    void testExistsTrue() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse response = repo.existsByNumber(TEST_ID);
        assertEquals("200", response.code);
    }

    @Test
    @Order(14)
    void testExistsFalse() {
        BasicResponse response = repo.existsByNumber("NON_USER");
        assertEquals("404", response.code);
    }
}