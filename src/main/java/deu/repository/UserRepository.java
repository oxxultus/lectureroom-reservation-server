package deu.repository;

/**
 * [사용자 리포지토리]
 *
 * <p>
 * 1. 해당 클래스는 파일과의 직접적인 통신을 구현합니다.
 * </p>
 */
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static final UserRepository instance = new UserRepository();
    private final Map<String, String> users = new HashMap<>();

    private UserRepository() {
        // 테스트용 계정
        users.put("test", "12345");
    }

    public static UserRepository getInstance() {
        return instance;
    }

    public boolean validate(String id, String pw) {
        return pw.equals(users.get(id));
    }
}
