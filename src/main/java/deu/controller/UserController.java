package deu.controller;

/**
 * [사용자 관리 컨트롤러]
 *
 * <p>
 * 1. 해당 컨트롤러에서 서비스를 호출해서 기능을 처리합니다.
 * </p>
 */

import deu.dto.BasicResponse;
import deu.dto.LoginRequest;
import deu.service.LoginService;

public class UserController {
    private final LoginService loginService = new LoginService();

    public Object handleLogin(LoginRequest req) {
        return loginService.login(req);
    }
}