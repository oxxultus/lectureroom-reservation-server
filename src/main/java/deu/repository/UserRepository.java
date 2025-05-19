package deu.repository;

import deu.model.entity.User;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final UserRepository instance = new UserRepository();
    private final List<User> users = new ArrayList<>();
    private final String FILE_PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator + "users.yaml";
    private final Yaml yaml;

    public static class UserWrapper {
        public List<User> users = new ArrayList<>();
    }

    private UserRepository() {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);

        loadAllFromFile();
    }
    public static UserRepository getInstance() {
        return instance;
    }

    private void saveAllToFile() {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();

        if (!parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (dirCreated) {
                System.out.println("[UserRepository] 디렉토리 생성됨: " + parentDir.getAbsolutePath());
            } else {
                System.err.println("[UserRepository] 디렉토리 생성 실패: " + parentDir.getAbsolutePath());
            }
        }

        try (Writer writer = new FileWriter(file)) {
            UserWrapper wrapper = new UserWrapper();
            wrapper.users = users;
            yaml.dump(wrapper, writer);
            System.out.println("[UserRepository] 파일 저장 완료: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[UserRepository] 파일 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("[UserRepository] 파일이 없어 새로 생성됩니다: " + file.getAbsolutePath());
            saveAllToFile();
            return;
        }

        try (InputStream input = new FileInputStream(file)) {
            UserWrapper wrapper = yaml.loadAs(input, UserWrapper.class);
            if (wrapper != null && wrapper.users != null) {
                users.clear();
                users.addAll(wrapper.users);
                System.out.println("[UserRepository] 파일 로딩 완료: " + file.getAbsolutePath());
                System.out.println("[UserRepository] 불러온 사용자 수: " + users.size());
            } else {
                System.out.println("[UserRepository] 파일은 있으나 사용자 데이터가 비어있습니다.");
            }
        } catch (IOException e) {
            System.err.println("[UserRepository] 파일 로딩 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String validate(String number, String pw) {
        for (User user : users) {
            if (user.number.equals(number)) {
                if (user.password.equals(pw)) {
                    return "200";
                } else {
                    return "401";
                }
            }
        }
        return "400";
    }

    public String save(String number, String pw, String name, String major) {
        for (User user : users) {
            if (user.number.equals(number)) {
                return "400";
            }
        }
        users.add(new User(number, pw, name, major));
        saveAllToFile();
        return "200";
    }

}
