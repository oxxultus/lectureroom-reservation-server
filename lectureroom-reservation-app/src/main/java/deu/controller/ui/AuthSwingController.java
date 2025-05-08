package deu.controller.ui;

import deu.controller.UserClientController;
import deu.model.dto.response.BasicResponse;
import deu.view.Auth;

import java.awt.event.ActionEvent;

public class AuthSwingController {
    private final Auth view;
    private final UserClientController userController;

    public AuthSwingController(Auth view) {
        this.view = view;
        this.userController = new UserClientController();

        // 이벤트 연결
        view.addLoginListener(this::handleLogin);
        view.addSignupListener(this::handleSignup);
        view.addSwitchToSignupListener(this::switchToSignup);
        view.addSwitchToLoginListener(this::switchToLogin);
    }

    private void handleLogin(ActionEvent e) {
        String id = view.getLoginId();
        String pw = view.getLoginPassword();
        BasicResponse res = userController.login(id, pw);

        if (res != null && res.code.equals("200")) {
            view.transitionToHome(id, pw);
        } else if (res != null && res.code.equals("401")) {
            view.showError("로그인 실패: " + (res != null ? res.message : "비밀번호 오류"));
        } else {
            view.showError("로그인 실패: " + (res != null ? res.message : "존재하지 않는 사용자(아이디 오류)"));
        }
    }

    private void handleSignup(ActionEvent e) {
        String id = view.getSignupId();
        String pw = view.getSignupPassword();
        String name = view.getSignupName();
        String major = view.getSignupMajor();
        BasicResponse res = userController.signup(id, pw, name, major);

        if (res != null && res.code.equals("200")) {
            view.showSuccess("회원가입 성공!");
            view.switchToLoginPanel();
        } else {
            view.showError("회원가입 실패: " + (res != null ? res.message : "서버 오류"));
        }
    }

    private void switchToSignup(ActionEvent e) {
        view.switchToSignupPanel();
    }

    private void switchToLogin(ActionEvent e) {
        view.switchToLoginPanel();
    }
}