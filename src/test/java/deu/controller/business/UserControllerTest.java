package deu.controller.business;

import static org.junit.jupiter.api.Assertions.*;

import deu.model.dto.request.data.user.LoginRequest;
import deu.model.dto.request.data.user.LogoutRequest;
import deu.model.dto.request.data.user.SignupRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.dto.response.CurrentResponse;
import deu.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController controller;
    private UserService mockUserService;

    @BeforeEach
    void setup() {
        controller = UserController.getInstance();

        mockUserService = mock(UserService.class);

        try {
            var field = UserController.class.getDeclaredField("userService");
            field.setAccessible(true);
            field.set(controller, mockUserService);

            var usersField = UserController.class.getDeclaredField("userNumbers");
            usersField.setAccessible(true);
            ((java.util.List<?>) usersField.get(controller)).clear(); // 사용자 목록 초기화

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("로그인 성공 시 사용자 번호를 목록에 추가한다")
    @Test
    void login_success_adds_user_number() {
        LoginRequest request = new LoginRequest("S1234", "pw");
        when(mockUserService.login(request)).thenReturn(new BasicResponse("200", "로그인 성공"));

        Object response = controller.handleLogin(request);
        assertTrue(response instanceof BasicResponse);
        assertEquals("200", ((BasicResponse) response).code);
    }

    @DisplayName("중복 로그인 시도 시 400 응답을 반환한다")
    @Test
    void login_fail_duplicate_user() {
        LoginRequest request = new LoginRequest("S1234", "pw");
        when(mockUserService.login(request)).thenReturn(new BasicResponse("200", "로그인 성공"));

        controller.handleLogin(request); // 첫 로그인 성공
        Object response = controller.handleLogin(request); // 중복 로그인 시도

        assertTrue(response instanceof BasicResponse);
        assertEquals("400", ((BasicResponse) response).code);
    }

    @DisplayName("동시 로그인 사용자 수가 3명을 초과하면 403을 반환한다")
    @Test
    void login_fail_max_user_limit() {
        when(mockUserService.login(any())).thenReturn(new BasicResponse("200", "로그인 성공"));

        controller.handleLogin(new LoginRequest("U1", "pw"));
        controller.handleLogin(new LoginRequest("U2", "pw"));
        controller.handleLogin(new LoginRequest("U3", "pw"));

        Object response = controller.handleLogin(new LoginRequest("U4", "pw"));

        assertTrue(response instanceof BasicResponse);
        assertEquals("403", ((BasicResponse) response).code);
    }

    @DisplayName("로그아웃 성공 시 사용자 번호를 목록에서 제거한다")
    @Test
    void logout_success_removes_user_number() {
        LoginRequest login = new LoginRequest("S1234", "pw");
        when(mockUserService.login(login)).thenReturn(new BasicResponse("200", "로그인 성공"));

        controller.handleLogin(login); // 먼저 로그인 시도
        Object response = controller.handleLogout(new LogoutRequest("S1234", "pw"));

        assertTrue(response instanceof BasicResponse);
        assertEquals("200", ((BasicResponse) response).code);
    }

    @DisplayName("로그인되어 있지 않은 사용자가 로그아웃 시도 시 실패한다")
    @Test
    void logout_fail_if_not_logged_in() {
        Object response = controller.handleLogout(new LogoutRequest("X999", "pw"));

        assertTrue(response instanceof BasicResponse);
        assertEquals("400", ((BasicResponse) response).code);
    }

    @DisplayName("회원가입 요청을 UserService로 위임한다")
    @Test
    void signup_delegates_to_userService() {
        SignupRequest signup = new SignupRequest("S1234", "pw", "홍길동", "컴공");
        BasicResponse mockResponse = new BasicResponse("200", "회원가입 성공");
        when(mockUserService.signup(signup)).thenReturn(mockResponse);

        Object response = controller.handleSignup(signup);
        assertEquals(mockResponse, response);
    }

    @DisplayName("현재 로그인된 사용자 수를 정확히 반환한다")
    @Test
    void current_user_count_returns_correct_number() {
        when(mockUserService.login(any())).thenReturn(new BasicResponse("200", "로그인 성공"));
        controller.handleLogin(new LoginRequest("A", "pw"));
        controller.handleLogin(new LoginRequest("B", "pw"));

        CurrentResponse res = controller.handleCurrentUser();
        assertEquals(2, res.currentUserCount);
    }
}