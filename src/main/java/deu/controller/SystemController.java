package deu.controller;

import deu.dto.request.LoginRequest;
import deu.dto.request.LogoutRequest;
import deu.dto.request.SignupRequest;
import deu.dto.request.UserCommandRequest;
import deu.dto.response.BasicResponse;

public class SystemController {
    private final UserController userController = UserController.getInstance();

    public Object handle(Object request) {
        if (request instanceof UserCommandRequest r) {
            return switch (r.command) {
                case "로그인" -> userController.handleLogin((LoginRequest) r.payload);
                case "회원가입" -> userController.handleSignup((SignupRequest) r.payload);
                case "로그아웃" -> userController.handleLogout((LogoutRequest) r.payload);
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        return new BasicResponse("405", "지원하지 않는 요청 타입");
    }
}