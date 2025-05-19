package deu.service;

import deu.model.dto.request.data.user.*;
import deu.model.dto.response.BasicResponse;
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
    public BasicResponse delete(DeleteRequest payload) {
        return UserRepository.getInstance().deleteByNumber(payload.number);
    }

    // 사용자 단일 조회
    public BasicResponse find(FindRequest payload) {
        return UserRepository.getInstance().findByNumber(payload.number);
    }

    // 전체 사용자 목록 조회
    public BasicResponse findAll() {
        return UserRepository.getInstance().findAll();
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

    // 사용자 존재 여부 확인 - 미사용
    public BasicResponse exists(ExistsRequest payload) {
        return UserRepository.getInstance().existsByNumber(payload.number);
    }
}