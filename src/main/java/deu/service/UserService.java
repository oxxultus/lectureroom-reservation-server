package deu.service;

import deu.model.dto.response.BasicResponse;
import deu.model.dto.request.data.LoginRequest;
import deu.model.dto.request.data.SignupRequest;
import deu.model.dto.request.data.UserDataModificationRequest;
import deu.repository.UserRepository;

public class UserService {
    // 사용자 로그인
    public BasicResponse login(LoginRequest payload) {
        return UserRepository.getInstance().validate(payload.number, payload.password);
    }

    // 사용자 가입
    public BasicResponse signup(SignupRequest payload) {
        return UserRepository.getInstance().save(payload.number, payload.password, payload.name, payload.major);
    }

    // 사용자 삭제
    public BasicResponse delete(String number) {
        return UserRepository.getInstance().deleteByNumber(number);
    }

    // 사용자 단일 조회
    public BasicResponse find(String number) {
        return UserRepository.getInstance().findByNumber(number);
    }

    // 전체 사용자 목록 조회
    public BasicResponse findAll() {
        return UserRepository.getInstance().findAll();
    }

    // 사용자 존재 여부 확인
    public BasicResponse exists(String number) {
        return UserRepository.getInstance().existsByNumber(number);
    }

    // 사용자 정보 수정
    public BasicResponse update(UserDataModificationRequest payload) {
        return UserRepository.getInstance().update(
                payload.number,
                payload.password,
                payload.name,
                payload.major
        );
    }
}