package deu.controller;

import deu.dto.BasicResponse;
import deu.dto.LoginRequest;

public class SystemController {
    private final UserController userController = new UserController();

    public Object handle(Object request) {
        if (request instanceof LoginRequest r) {
            return userController.handleLogin(r);
        }
        return new BasicResponse(false, "지원하지 않는 요청입니다.");
    }
}