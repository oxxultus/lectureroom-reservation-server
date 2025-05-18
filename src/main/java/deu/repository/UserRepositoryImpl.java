package deu.repository;

import deu.model.entity.User;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.List;
import java.io.*;
import java.util.ArrayList;

import static com.sun.tools.jdeprscan.DeprDB.loadFromFile;

public class UserRepositoryImpl {
    //저장경로
    private static UserRepositoryImpl instance;

    // TODO: final 변수는 초기화를 진행해야 합니다.
    private final List<User> users;

    private final String filePath = "user_data.yaml";

    // singleton 패턴으로 구현한다
    public static UserRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }
    // TODO: 아래 코드는 중복 된 코드입니다. 싱글톤 패턴에서는 static으로 메서드를 구현해야 합니다.

    // 생성자
    private UserRepositoryImpl() {
        this.users = new ArrayList<>();
        loadAllFromFile();
    }
    // 파일에서 읽어오는 메서드
    private void loadAllFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (InputStream input = new FileInputStream(file)) {
            Yaml yaml = new Yaml(new Constructor(ArrayList.class));
            List<User> loaded = yaml.load(input);
            if (loaded != null) {
                users.clear();
                users.addAll(loaded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 파일에 저장하는 메서드
    private void saveAllToFile() {
        try (Writer writer = new FileWriter(filePath)) {
            Yaml yaml = new Yaml();
            yaml.dump(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 저장 메서드
    public void save(User user) {
        users.add(user);
    }
    // 삭제 메서드
    public boolean deleteByNumber(String number) {
        return users.removeIf(u -> u.number.equals(number));
    }
    // 비교 메서드
    public boolean existsByNumber(String number) {
        return users.stream().anyMatch(u -> u.number.equals(number));
    }
    //특정 사용자 조회
    public User findByNumber(String number) {
        return users.stream()
                .filter(u -> u.number.equals(number))
                .findFirst()
                .orElse(null);
    }

    //전체 사용자 반환
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    //외부에서 파일 저장 요청하는 메서드
    public void saveToFile() {
        saveAllToFile();
    }
}
