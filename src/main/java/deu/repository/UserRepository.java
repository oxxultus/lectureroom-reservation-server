package deu.repository;

import deu.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final UserRepository instance = new UserRepository();
    private final List<User> users = new ArrayList<>();

    // 임시 메모리 데이터
    private UserRepository() {
        // 테스트용 계정
        users.add(new User("admin", "admin", "admin", "컴퓨터 소프트웨어 공학과"));
    }

    public static UserRepository getInstance() {
        return instance;
    }

    // 로그인 검증
    public String validate(String number, String pw) {
        for (User user : users) {
            if (user.number.equals(number)) {
                // 아이디는 존재함
                if (user.password.equals(pw)) {
                    return "200"; // 로그인 성공
                } else {
                    return "401"; // 비밀번호 오류
                }
            }
        }
        return "400"; // 존재하지 않는 사용자 (아이디 오류)
    }

    // 회원가입 저장
    public String save(String number, String pw, String name, String major) {
        for (User user : users) {
            if (user.number.equals(number)) {
                return "400"; // 이미 존재하는 고유번호 -> 저장 실패
            }
        }
        users.add(new User(number, pw, name, major));
        return "200"; // 저장 성공
    }

    // 사용자 검색
    public User findByNumber(String number) {
        for (User user : users) {
            if (user.number.equals(number)) {
                return user;
            }
        }
        return null;
    }
}
