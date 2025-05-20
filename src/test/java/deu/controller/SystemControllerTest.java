package deu.controller;

import static org.junit.jupiter.api.Assertions.*;

import deu.controller.business.UserController;
import deu.model.dto.request.data.user.LoginRequest;
import deu.model.dto.request.data.user.LogoutRequest;
import deu.model.dto.request.data.user.SignupRequest;
import deu.model.dto.request.command.UserCommandRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.dto.response.CurrentResponse;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

public class SystemControllerTest {

    @Test
    void handle_login_command_should_call_handleLogin() {
        try (MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)) {
            UserController mockUserController = mock(UserController.class);
            mockedStatic.when(UserController::getInstance).thenReturn(mockUserController);

            LoginRequest loginRequest = new LoginRequest("S2023001", "pw");
            BasicResponse expected = new BasicResponse("200", "로그인 성공");
            when(mockUserController.handleLogin(loginRequest)).thenReturn(expected);

            SystemController controller = new SystemController();
            Object response = controller.handle(new UserCommandRequest("로그인", loginRequest));

            assertTrue(response instanceof BasicResponse);
            assertEquals("200", ((BasicResponse) response).code);
        }
    }

    @Test
    void handle_signup_command_should_call_handleSignup() {
        try (MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)) {
            UserController mockUserController = mock(UserController.class);
            mockedStatic.when(UserController::getInstance).thenReturn(mockUserController);

            SignupRequest signupRequest = new SignupRequest("S2023001", "pw", "홍길동", "컴공");
            BasicResponse expected = new BasicResponse("200", "회원가입 성공");
            when(mockUserController.handleSignup(signupRequest)).thenReturn(expected);

            SystemController controller = new SystemController();
            Object response = controller.handle(new UserCommandRequest("회원가입", signupRequest));

            assertTrue(response instanceof BasicResponse);
            assertEquals("200", ((BasicResponse) response).code);
        }
    }

    @Test
    void handle_logout_command_should_call_handleLogout() {
        try (MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)) {
            UserController mockUserController = mock(UserController.class);
            mockedStatic.when(UserController::getInstance).thenReturn(mockUserController);

            LogoutRequest logoutRequest = new LogoutRequest("S2023001", "pw");
            BasicResponse expected = new BasicResponse("200", "로그아웃 성공");
            when(mockUserController.handleLogout(logoutRequest)).thenReturn(expected);

            SystemController controller = new SystemController();
            Object response = controller.handle(new UserCommandRequest("로그아웃", logoutRequest));

            assertTrue(response instanceof BasicResponse);
            assertEquals("200", ((BasicResponse) response).code);
        }
    }

    @Test
    void handle_current_user_command_should_call_handleCurrentUser() {
        try (MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)) {
            UserController mockUserController = mock(UserController.class);
            mockedStatic.when(UserController::getInstance).thenReturn(mockUserController);

            CurrentResponse expected = new CurrentResponse(3);
            when(mockUserController.handleCurrentUser()).thenReturn(expected);

            SystemController controller = new SystemController();
            Object response = controller.handle(new UserCommandRequest("동시접속자", null));

            assertTrue(response instanceof CurrentResponse);
            assertEquals(3, ((CurrentResponse) response).currentUserCount);
        }
    }

    @Test
    void handle_unknown_command_should_return_404() {
        SystemController controller = new SystemController();
        Object response = controller.handle(new UserCommandRequest("삭제", null));

        assertTrue(response instanceof BasicResponse);
        assertEquals("404", ((BasicResponse) response).code);
    }

    @Test
    void handle_invalid_type_should_return_405() {
        SystemController controller = new SystemController();
        Object response = controller.handle("이건 명령이 아님");

        assertTrue(response instanceof BasicResponse);
        assertEquals("405", ((BasicResponse) response).code);
    }
}