package deu.controller;

import deu.model.dto.request.LoginRequest;
import deu.model.dto.request.LogoutRequest;
import deu.model.dto.request.SignupRequest;
import deu.model.dto.request.UserCommandRequest;
import deu.model.dto.response.BasicResponse;

public class SystemController {
    private final UserController userController = UserController.getInstance();

    public Object handle(Object request) {
        if (request instanceof UserCommandRequest r) {
            return switch (r.command) {
                case "로그인" -> userController.handleLogin((LoginRequest) r.payload);
                case "회원가입" -> userController.handleSignup((SignupRequest) r.payload);
                case "로그아웃" -> userController.handleLogout((LogoutRequest) r.payload);
                case "동시접속자" -> userController.handleCurrentUser();
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        return new BasicResponse("405", "지원하지 않는 요청 타입");
    }
}