package deu.repository;

import deu.model.entity.User;

import java.util.List;
import java.io.*;
import java.util.ArrayList;

import static com.sun.tools.jdeprscan.DeprDB.loadFromFile;

public class UserRepositoryImpl {
    //저장경로
    private static UserRepositoryImpl instance;

    // TODO: final 변수는 초기화를 진행해야 합니다.
    private final List<User> users;

    private final String filePath = "user_data.ser";

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
        loadFromFile();
    }
    // 파일에서 읽어오는 메서드
    private void loadAllFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            users.clear();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<User> loadedUsers = (List<User>) ois.readObject();
            users.clear();
            users.addAll(loadedUsers);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            users.clear();
        }
    }
    // 파일에 저장하는 메서드


    // 저장 메서드


    // 삭제 메서드


    // 비교 메서드


    // 등 등
}
