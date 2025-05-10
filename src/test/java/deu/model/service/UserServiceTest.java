package deu.model.service;

import deu.model.dto.request.LoginRequest;
import deu.model.dto.request.SignupRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest("S1234", "pw");
        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            UserRepository mockRepo = mock(UserRepository.class);
            mockedStatic.when(UserRepository::getInstance).thenReturn(mockRepo);
            when(mockRepo.validate("S1234", "pw")).thenReturn("200");

            BasicResponse response = userService.login(request);
            assertEquals("200", response.code);
            assertEquals("로그인 성공", response.message);
        }
    }

    @Test
    void login_invalid_password() {
        LoginRequest request = new LoginRequest("S1234", "wrongpw");
        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            UserRepository mockRepo = mock(UserRepository.class);
            mockedStatic.when(UserRepository::getInstance).thenReturn(mockRepo);
            when(mockRepo.validate("S1234", "wrongpw")).thenReturn("401");

            BasicResponse response = userService.login(request);
            assertEquals("401", response.code);
            assertEquals("비밀번호 입력 오류 입니다.", response.message);
        }
    }

    @Test
    void login_user_not_found() {
        LoginRequest request = new LoginRequest("X999", "pw");
        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            UserRepository mockRepo = mock(UserRepository.class);
            mockedStatic.when(UserRepository::getInstance).thenReturn(mockRepo);
            when(mockRepo.validate("X999", "pw")).thenReturn("404");

            BasicResponse response = userService.login(request);
            assertEquals("404", response.code);
            assertEquals("존재하지 않는 아이디 입니다.", response.message);
        }
    }

    @Test
    void signup_success() {
        SignupRequest request = new SignupRequest("S1234", "pw", "홍길동", "컴공");
        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            UserRepository mockRepo = mock(UserRepository.class);
            mockedStatic.when(UserRepository::getInstance).thenReturn(mockRepo);
            when(mockRepo.save("S1234", "pw", "홍길동", "컴공")).thenReturn("200");

            BasicResponse response = userService.signup(request);
            assertEquals("200", response.code);
            assertEquals("회원가입 성공", response.message);
        }
    }

    @Test
    void signup_duplicate_user() {
        SignupRequest request = new SignupRequest("S1234", "pw", "홍길동", "컴공");
        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            UserRepository mockRepo = mock(UserRepository.class);
            mockedStatic.when(UserRepository::getInstance).thenReturn(mockRepo);
            when(mockRepo.save("S1234", "pw", "홍길동", "컴공")).thenReturn("400");

            BasicResponse response = userService.signup(request);
            assertEquals("400", response.code);
            assertEquals("이미 가입된 사용자 정보 입니다.", response.message);
        }
    }

    @Test
    void signup_unknown_error() {
        SignupRequest request = new SignupRequest("S1234", "pw", "홍길동", "컴공");
        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            UserRepository mockRepo = mock(UserRepository.class);
            mockedStatic.when(UserRepository::getInstance).thenReturn(mockRepo);
            when(mockRepo.save("S1234", "pw", "홍길동", "컴공")).thenReturn("500");

            BasicResponse response = userService.signup(request);
            assertEquals("500", response.code);
            assertEquals("회원가입 실패", response.message);
        }
    }
}