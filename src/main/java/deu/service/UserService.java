package deu.service;

import deu.model.dto.response.BasicResponse;
import deu.model.dto.request.data.LoginRequest;
import deu.model.dto.request.data.SignupRequest;
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

    // TODO: 사용자 수정 (성공/실패 코드와 메시지 반환)

    // TODO: 사용자 삭제 (성공/실패 코드와 메시지 반환)

    // TODO: 사용자 조회 (성공/실패 코드와 사용자 객체 반환)

    // TODO: 전체 사용자 조회 (성공/실패 코드와 사용자 객체 리스트(컬랙션) 반환)
}