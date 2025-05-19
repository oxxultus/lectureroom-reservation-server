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
    private final String TEST_ID = "test001";
    private final String TEST_PASSWORD = "pw123";
    private final String TEST_NAME = "테스트 유저";
    private final String TEST_MAJOR = "소프트웨어학과";

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
            dir.delete();
        }
    }

    @BeforeEach
    void clearUser() {
        repo.deleteByNumber(TEST_ID);
    }

    @Test
    @Order(1)
    void testSaveUser() {
        BasicResponse res = repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        assertEquals("200", res.code);
    }

    @Test
    @Order(2)
    void testDuplicateUserSave() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse res = repo.save(TEST_ID, "otherpw", "다른이름", "다른학과");
        assertEquals("400", res.code);
    }

    @Test
    @Order(3)
    void testValidateSuccess() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse res = repo.validate(TEST_ID, TEST_PASSWORD);
        assertEquals("200", res.code);
    }

    @Test
    @Order(4)
    void testValidateWrongPassword() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse res = repo.validate(TEST_ID, "wrongpw");
        assertEquals("401", res.code);
    }

    @Test
    @Order(5)
    void testValidateNonexistentUser() {
        BasicResponse res = repo.validate("none", "pw");
        assertEquals("400", res.code);
    }

    @Test
    @Order(6)
    void testFindByNumberSuccess() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse res = repo.findByNumber(TEST_ID);
        assertEquals("200", res.code);
        assertTrue(res.data instanceof User);
    }

    @Test
    @Order(7)
    void testFindByNumberFail() {
        BasicResponse res = repo.findByNumber("unknown");
        assertEquals("404", res.code);
    }

    @Test
    @Order(8)
    void testUpdateUser() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse res = repo.update(TEST_ID, "newpw", "변경된이름", "AI학과");
        assertEquals("200", res.code);
    }

    @Test
    @Order(9)
    void testUpdateNonexistentUser() {
        BasicResponse res = repo.update("notExist", "pw", "이름", "전공");
        assertEquals("404", res.code);
    }

    @Test
    @Order(10)
    void testDeleteUser() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse res = repo.deleteByNumber(TEST_ID);
        assertEquals("200", res.code);
    }

    @Test
    @Order(11)
    void testDeleteNonexistentUser() {
        BasicResponse res = repo.deleteByNumber("notExist");
        assertEquals("404", res.code);
    }

    @Test
    @Order(12)
    void testFindAll() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse res = repo.findAll();
        assertEquals("200", res.code);
        assertTrue(res.data instanceof List);
    }

    @Test
    @Order(13)
    void testExistsByNumberTrue() {
        repo.save(TEST_ID, TEST_PASSWORD, TEST_NAME, TEST_MAJOR);
        BasicResponse res = repo.existsByNumber(TEST_ID);
        assertEquals("200", res.code);
    }

    @Test
    @Order(14)
    void testExistsByNumberFalse() {
        BasicResponse res = repo.existsByNumber("notExist");
        assertEquals("404", res.code);
    }
}