package deu.controller;

import deu.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserManagementController {

    private static final UserManagementController instance = new UserManagementController();

    private UserManagementController() {}

    public static UserManagementController getInstance() {
        return instance;
    }

    private final UserService userService = new UserService();

    // 각 메서드 앞에 handle를 꼭 붙혀주세요 (알관성 유지)
    // TODO: 사용자 수정 (성공/실패 코드와 메시지 반환)

    // TODO: 사용자 삭제 (성공/실패 코드와 메시지 반환)

    // TODO: 사용자 조회 (성공/실패 코드와 사용자 객체 반환)

    // TODO: 전체 사용자 조회 (성공/실패 코드와 사용자 객체 리스트(컬랙션) 반환)

}