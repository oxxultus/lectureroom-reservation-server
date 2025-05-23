package deu.service;

import deu.model.dto.request.data.user.*;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.User;
import deu.repository.UserRepository;
import lombok.Getter;

public class UserService {

    // Singleton 인스턴스
    @Getter
    private static final UserService instance = new UserService();

    private UserService() {}

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

    // 사용자 이름 찾기
    public BasicResponse findUserName(FindUserNameRequest payload) {
        BasicResponse response = UserRepository.getInstance().findByNumber(payload.number);
        if(response.code.equals("200")){
            return new BasicResponse(response.code, ((User)response.data).getName());
        }
        else{
            return new BasicResponse(response.code, "해당하는 학번의 이름이 존재하지 않습니다.");
        }
    }
}