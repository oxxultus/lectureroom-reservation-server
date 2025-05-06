package deu.service;

import deu.model.dto.response.BasicResponse;
import deu.model.dto.request.LoginRequest;
import deu.model.dto.request.SignupRequest;
import deu.repository.UserRepository;

public class UserService {
    public BasicResponse login(LoginRequest payload) {
        String valid = UserRepository.getInstance().validate(payload.number, payload.password);
        if (valid.equals("200")) {
            return new BasicResponse(valid,"로그인 성공");
        }else if(valid.equals("401")) {
            return new BasicResponse(valid, "비밀번호 입력 오류 입니다.");
        }else{
            return new BasicResponse(valid, "존재하지 않는 아이디 입니다.");
        }
    }

    public BasicResponse signup(SignupRequest payload) {
        String valid = UserRepository.getInstance().save(payload.number, payload.password, payload.name, payload.major);
        if(valid.equals("200")) {
            return new BasicResponse(valid, "회원가입 성공");
        }else if(valid.equals("400")) {
            return new BasicResponse(valid, "이미 가입된 사용자 정보 입니다.");
        }else{
            return new BasicResponse(valid, "회원가입 실패");
        }
    }
}