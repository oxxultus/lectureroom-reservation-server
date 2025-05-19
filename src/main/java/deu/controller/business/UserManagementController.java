package deu.controller.business;

import deu.model.dto.request.data.user.*;
import deu.service.UserService;

public class UserManagementController {

    private static final UserManagementController instance = new UserManagementController();

    private UserManagementController() {}

    public static UserManagementController getInstance() {
        return instance;
    }

    private final UserService userService = new UserService();

    // 사용자 정보 수정 처리
    public Object handleUpdateUser(UserDataModificationRequest payload) {
        return userService.update(payload);
    }

    // 사용자 삭제 처리
    public Object handleDeleteUser(DeleteRequest payload) {
        return userService.delete(payload);
    }

    // 사용자 단일 조회 처리
    public Object handleFindUser(FindRequest payload) {
        return userService.find(payload);
    }

    // 전체 사용자 목록 조회 처리
    public Object handleFindAllUsers() {
        return userService.findAll();
    }
}