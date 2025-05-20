package deu.controller.business;

import deu.model.dto.response.BasicResponse;
import deu.model.dto.request.data.user.LoginRequest;
import deu.model.dto.request.data.user.LogoutRequest;
import deu.model.dto.request.data.user.SignupRequest;
import deu.model.dto.response.CurrentResponse;
import deu.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    private static final UserController instance = new UserController();

    private UserController() {}

    public static UserController getInstance() {
        return instance;
    }

    private final UserService userService = new UserService();
    private final List<String> userNumbers = new ArrayList<>(); // 로그인 사용자 고유번호 저장용

    // synchronized 는 이 메서드에 동시에 들어오지 못함: 첫 번째 스레드가 들어오면 락(lock)을 잡고, 다른 스레드들은 락이 풀릴 때까지 대기
    public synchronized Object handleLogin(LoginRequest payload) {
        if (userNumbers.contains(payload.number)) {
            return new BasicResponse("400", "이미 로그인된 사용자입니다.");
        }

        if (userNumbers.size() >= 3) {
            return new BasicResponse("403", "현재 접속 인원 초과 (최대 3명)");
        }

        BasicResponse result = userService.login(payload);
        if ((result.code).equals("200")) {
            userNumbers.add(payload.number); // 로그인 성공 시 번호 등록
        }

        return result;
    }

    // 회원 가입 컨트롤러
    public Object handleSignup(SignupRequest payload) {
        return userService.signup(payload);
    }

    // synchronized 는 이 메서드에 동시에 들어오지 못함: 첫 번째 스레드가 들어오면 락(lock)을 잡고, 다른 스레드들은 락이 풀릴 때까지 대기
    public synchronized Object handleLogout(LogoutRequest payload) {
        // 로그아웃 시 번호 제거
        if(userNumbers.remove(payload.number)){
            System.out.println("현재 접속자 수 " + userNumbers.size() + "명");
            return new BasicResponse("200", "로그아웃 성공");
        }else{
            return new BasicResponse("400", "로그아웃 실패");
        }
    }

    // 동시접속자 수 컨트롤러
    public CurrentResponse handleCurrentUser(){
        return new CurrentResponse(userNumbers.size());
    }
}