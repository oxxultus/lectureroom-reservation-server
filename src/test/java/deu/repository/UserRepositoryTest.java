package deu.repository;

import static org.junit.jupiter.api.Assertions.*;

import deu.model.entity.User;
import deu.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = UserRepository.getInstance();

        // 리플렉션을 사용해 users 리스트 초기화
        try {
            var field = UserRepository.class.getDeclaredField("users");
            field.setAccessible(true);
            ((java.util.List<?>) field.get(userRepository)).clear();

            // 테스트용 초기 계정 추가
            ((java.util.List<User>) field.get(userRepository)).add(
                    new User("admin", "admin", "admin", "컴퓨터 소프트웨어 공학과")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void validate_success() {
        String result = userRepository.validate("admin", "admin");
        assertEquals("200", result);
    }

    @Test
    void validate_wrong_password() {
        String result = userRepository.validate("admin", "wrong");
        assertEquals("401", result);
    }

    @Test
    void validate_user_not_found() {
        String result = userRepository.validate("unknown", "pw");
        assertEquals("400", result);
    }

    @Test
    void save_success() {
        String result = userRepository.save("S1234", "pw", "홍길동", "컴공");
        assertEquals("200", result);
    }

    @Test
    void save_duplicate_user() {
        userRepository.save("S1234", "pw", "홍길동", "컴공");
        String result = userRepository.save("S1234", "pw2", "김철수", "소프트웨어");
        assertEquals("400", result);
    }

    @Test
    void findByNumber_exists() {
        userRepository.save("S2023001", "pw", "김민지", "AI학과");
        User user = userRepository.findByNumber("S2023001");
        assertNotNull(user);
        assertEquals("김민지", user.name);
    }

    @Test
    void findByNumber_not_found() {
        User user = userRepository.findByNumber("X999");
        assertNull(user);
    }
}
