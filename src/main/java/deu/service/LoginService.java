package deu.service;

import deu.dto.BasicResponse;
import deu.dto.LoginRequest;
import deu.repository.UserRepository;

public class LoginService {
    public BasicResponse login(LoginRequest req) {
        boolean valid = UserRepository.getInstance().validate(req.username, req.password);
        return new BasicResponse(valid, valid ? "로그인 성공" : "로그인 실패");
    }
}