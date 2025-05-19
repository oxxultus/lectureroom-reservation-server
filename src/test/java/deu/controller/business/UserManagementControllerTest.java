package deu.controller.business;

import deu.model.dto.request.data.user.*;
import deu.model.dto.response.BasicResponse;
import deu.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserManagementControllerTest {

    private UserService mockService;
    private UserManagementController controller;

    @BeforeEach
    void setup() throws Exception {
        mockService = Mockito.mock(UserService.class);
        controller = UserManagementController.getInstance();

        // 리플렉션을 통해 private final 필드 주입
        Field serviceField = UserManagementController.class.getDeclaredField("userService");
        serviceField.setAccessible(true);
        serviceField.set(controller, mockService);
    }

    @Test
    void testHandleUpdateUser() {
        when(mockService.update(any())).thenReturn(new BasicResponse("200", "수정 성공"));

        var req = new UserDataModificationRequest("20250001", "pw", "홍길동", "AI");
        Object res = controller.handleUpdateUser(req);
        assertTrue(res instanceof BasicResponse);
        assertEquals("200", ((BasicResponse) res).code);
    }

    @Test
    void testHandleDeleteUser() {
        when(mockService.delete(any())).thenReturn(new BasicResponse("200", "삭제 성공"));

        var req = new DeleteRequest("20250001");
        Object res = controller.handleDeleteUser(req);
        assertTrue(res instanceof BasicResponse);
        assertEquals("200", ((BasicResponse) res).code);
    }

    @Test
    void testHandleFindUser() {
        when(mockService.find(any())).thenReturn(new BasicResponse("200", new Object()));

        var req = new FindRequest("20250001");
        Object res = controller.handleFindUser(req);
        assertTrue(res instanceof BasicResponse);
        assertEquals("200", ((BasicResponse) res).code);
    }

    @Test
    void testHandleFindAllUsers() {
        when(mockService.findAll()).thenReturn(new BasicResponse("200", new Object()));

        Object res = controller.handleFindAllUsers();
        assertTrue(res instanceof BasicResponse);
        assertEquals("200", ((BasicResponse) res).code);
    }
}
