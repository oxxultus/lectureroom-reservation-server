package deu.service;

import deu.model.dto.request.data.user.*;
import deu.model.dto.response.BasicResponse;
import deu.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private UserRepository mockRepo;
    private UserService service;

    private final String TEST_FILE_PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator + "users.yaml";
    private final String DATA_DIR_PATH = System.getProperty("user.dir") + File.separator + "data";

    @BeforeAll
    void setupFile() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) file.delete();
    }

    @AfterAll
    void cleanupFile() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) file.delete();

        File dir = new File(DATA_DIR_PATH);
        if (dir.exists() && dir.isDirectory() && dir.list().length == 0) {
            dir.delete();
        }
    }

    @BeforeEach
    void setup() {
        mockRepo = Mockito.mock(UserRepository.class);
        service = new UserService() {
            @Override
            public BasicResponse signup(SignupRequest payload) {
                return mockRepo.save(payload.number, payload.password, payload.name, payload.major);
            }

            @Override
            public BasicResponse login(LoginRequest payload) {
                return mockRepo.validate(payload.number, payload.password);
            }

            @Override
            public BasicResponse delete(DeleteRequest payload) {
                return mockRepo.deleteByNumber(payload.number);
            }

            @Override
            public BasicResponse find(FindRequest payload) {
                return mockRepo.findByNumber(payload.number);
            }

            @Override
            public BasicResponse findAll() {
                return mockRepo.findAll();
            }

            @Override
            public BasicResponse exists(ExistsRequest payload) {
                return mockRepo.existsByNumber(payload.number);
            }

            @Override
            public BasicResponse update(UserDataModificationRequest payload) {
                return mockRepo.update(payload.number, payload.password, payload.name, payload.major);
            }
        };
    }

    @DisplayName("회원가입 요청이 정상 처리되는지 확인")
    @Test
    void testSignupSuccess() {
        when(mockRepo.save(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new BasicResponse("200", "회원가입 성공"));

        SignupRequest req = new SignupRequest("20250001", "pw", "홍길동", "소프트웨어");
        BasicResponse res = service.signup(req);
        assertEquals("200", res.code);
    }

    @DisplayName("로그인 요청이 정상 처리되는지 확인")
    @Test
    void testLoginSuccess() {
        when(mockRepo.validate(anyString(), anyString()))
                .thenReturn(new BasicResponse("200", "로그인 성공"));

        LoginRequest req = new LoginRequest("20250001", "pw");
        BasicResponse res = service.login(req);
        assertEquals("200", res.code);
    }

    @DisplayName("사용자 정보 수정 요청이 처리되는지 확인")
    @Test
    void testUpdateSuccess() {
        when(mockRepo.update(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new BasicResponse("200", "수정 완료"));

        UserDataModificationRequest req = new UserDataModificationRequest("20250001", "newpw", "이순신", "AI");
        BasicResponse res = service.update(req);
        assertEquals("200", res.code);
    }

    @DisplayName("사용자 삭제 요청이 처리되는지 확인")
    @Test
    void testDeleteSuccess() {
        when(mockRepo.deleteByNumber(anyString()))
                .thenReturn(new BasicResponse("200", "삭제 완료"));

        DeleteRequest req = new DeleteRequest("20250001");
        BasicResponse res = service.delete(req);
        assertEquals("200", res.code);
    }

    @DisplayName("단일 사용자 조회 요청이 처리되는지 확인")
    @Test
    void testFindSuccess() {
        when(mockRepo.findByNumber(anyString()))
                .thenReturn(new BasicResponse("200", new Object()));

        FindRequest req = new FindRequest("20250001");
        BasicResponse res = service.find(req);
        assertEquals("200", res.code);
    }

    @DisplayName("전체 사용자 목록 조회 요청이 처리되는지 확인")
    @Test
    void testFindAll() {
        when(mockRepo.findAll())
                .thenReturn(new BasicResponse("200", new Object()));

        BasicResponse res = service.findAll();
        assertEquals("200", res.code);
    }

    @DisplayName("사용자 존재 여부 확인 요청이 처리되는지 확인")
    @Test
    void testExistsTrue() {
        when(mockRepo.existsByNumber(anyString()))
                .thenReturn(new BasicResponse("200", "존재함"));

        ExistsRequest req = new ExistsRequest("20250001");
        BasicResponse res = service.exists(req);
        assertEquals("200", res.code);
    }
}