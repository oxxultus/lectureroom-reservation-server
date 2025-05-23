package deu.service;

import deu.model.dto.request.data.user.*;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.User;
import deu.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserService 단위 테스트")
public class UserServiceTest {

    private UserService service;
    private UserRepository mockRepo;
    private MockedStatic<UserRepository> mockedStatic;

    @BeforeEach
    void setUp() {
        mockRepo = mock(UserRepository.class);
        mockedStatic = mockStatic(UserRepository.class);
        mockedStatic.when(UserRepository::getInstance).thenReturn(mockRepo);

        service = UserService.getInstance();
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    @DisplayName("로그인 성공 시 코드 200 및 메시지 반환")
    void testLogin() {
        LoginRequest request = new LoginRequest("S123", "pw");
        when(mockRepo.validate("S123", "pw")).thenReturn(new BasicResponse("200", "로그인 성공"));

        BasicResponse response = service.login(request);

        assertEquals("200", response.code);
        assertEquals("로그인 성공", response.data.toString());
    }

    @Test
    @DisplayName("회원가입 요청 처리 시 코드 201 및 메시지 반환")
    void testSignup() {
        SignupRequest request = new SignupRequest("S123", "pw", "홍길동", "컴퓨터공학");
        when(mockRepo.save("S123", "pw", "홍길동", "컴퓨터공학"))
                .thenReturn(new BasicResponse("201", "가입 완료"));

        BasicResponse response = service.signup(request);

        assertEquals("201", response.code);
        assertEquals("가입 완료", response.data.toString());
    }

    @Test
    @DisplayName("회원 삭제 요청 처리 시 코드 200 및 메시지 반환")
    void testDelete() {
        DeleteRequest request = new DeleteRequest("S123");
        when(mockRepo.deleteByNumber("S123")).thenReturn(new BasicResponse("200", "삭제됨"));

        BasicResponse response = service.delete(request);

        assertEquals("200", response.code);
        assertEquals("삭제됨", response.data.toString());
    }

    @Test
    @DisplayName("회원 단일 조회 성공 시 코드 200과 사용자 객체 반환")
    void testFind() {
        User dummy = new User("S123", "pw", "홍길동", "컴공");
        when(mockRepo.findByNumber("S123")).thenReturn(new BasicResponse("200", dummy));

        BasicResponse response = service.find(new FindRequest("S123"));

        assertEquals("200", response.code);
        assertEquals(dummy, response.data);
    }

    @Test
    @DisplayName("전체 회원 조회 시 코드 200과 데이터 반환")
    void testFindAll() {
        when(mockRepo.findAll()).thenReturn(new BasicResponse("200", "전체 유저"));

        BasicResponse response = service.findAll();

        assertEquals("200", response.code);
        assertEquals("전체 유저", response.data);
    }

    @Test
    @DisplayName("회원 정보 수정 요청 시 코드 200 및 메시지 반환")
    void testUpdate() {
        UserDataModificationRequest request = new UserDataModificationRequest("S123", "newpw", "홍길순", "전자공학");
        when(mockRepo.update("S123", "newpw", "홍길순", "전자공학"))
                .thenReturn(new BasicResponse("200", "수정됨"));

        BasicResponse response = service.update(request);

        assertEquals("200", response.code);
        assertEquals("수정됨", response.data.toString());
    }

    @Test
    @DisplayName("회원 존재 여부 확인 시 true 반환")
    void testExists() {
        ExistsRequest request = new ExistsRequest("S123");
        when(mockRepo.existsByNumber("S123")).thenReturn(new BasicResponse("200", true));

        BasicResponse response = service.exists(request);

        assertEquals("200", response.code);
        assertTrue((Boolean) response.data);
    }

    @Test
    @DisplayName("학번으로 사용자 이름 조회 성공 시 이름 반환")
    void testFindUserNameSuccess() {
        User dummy = new User("S123", "pw", "김철수", "기계공학");
        when(mockRepo.findByNumber("S123")).thenReturn(new BasicResponse("200", dummy));

        BasicResponse response = service.findUserName(new FindUserNameRequest("S123", "pw"));

        assertEquals("200", response.code);
        assertEquals("김철수", response.data.toString());
    }

    @Test
    @DisplayName("학번으로 사용자 이름 조회 실패 시 오류 메시지 반환")
    void testFindUserNameFail() {
        when(mockRepo.findByNumber("S123")).thenReturn(new BasicResponse("404", null));

        BasicResponse response = service.findUserName(new FindUserNameRequest("S123", "pw"));

        assertEquals("404", response.code);
        assertEquals("해당하는 학번의 이름이 존재하지 않습니다.", response.data.toString());
    }
}