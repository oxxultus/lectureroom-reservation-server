package deu.repository;

import deu.model.dto.response.BasicResponse;
import deu.model.entity.User;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 정보를 YAML 파일로 관리하는 저장소 클래스
 * 싱글톤 패턴을 사용하며, 파일이 없을 경우 resources에서 복사하여 생성한다.
 */
public class UserRepository {
    // 싱글톤 인스턴스
    private static final UserRepository instance = new UserRepository();

    // 메모리 내 사용자 리스트
    private final List<User> users = new ArrayList<>();

    // 사용자 데이터 파일 경로
    private final String FILE_PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator + "users.yaml";

    // YAML 객체
    private final Yaml yaml;

    // 사용자 데이터를 감싸는 래퍼 클래스 (YAML 구조 유지를 위함)
    public static class UserWrapper {
        public List<User> users;

        // SnakeYAML이 사용하는 명시적 기본 생성자
        public UserWrapper() {
            this.users = new ArrayList<>();
        }
    }

    // 생성자: YAML 설정 및 파일 로딩
    private UserRepository() {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        // SnakeYAML 2.x 대응: 클래스 태그 제거용 Representer
        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true); // (안정성 향상)

        // 클래스별 태그 제거
        representer.addClassTag(UserWrapper.class, Tag.MAP);
        representer.addClassTag(User.class, Tag.MAP);

        yaml = new Yaml(representer, options);

        loadAllFromFile();
    }

    // 외부에서 접근하는 싱글톤 메서드
    public static UserRepository getInstance() {
        return instance;
    }

    // 모든 사용자 정보를 파일에 저장
    private void saveAllToFile() {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();

        // 디렉토리 없으면 생성
        if (!parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (dirCreated) {
                System.out.println("[UserRepository] 디렉토리 생성됨: " + parentDir.getAbsolutePath());
            } else {
                System.err.println("[UserRepository] 디렉토리 생성 실패: " + parentDir.getAbsolutePath());
            }
        }

        // 사용자 리스트를 YAML 형식으로 파일에 저장
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

    // 파일에서 사용자 정보 로딩 (없으면 resources에서 복사)
    private void loadAllFromFile() {
        File file = new File(FILE_PATH);

        // 파일이 없으면 resources에서 복사 시도
        if (!file.exists()) {
            System.out.println("[UserRepository] 파일이 없어 리소스에서 복사합니다: " + file.getAbsolutePath());

            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (dirCreated) {
                    System.out.println("[UserRepository] 디렉토리 생성됨: " + parentDir.getAbsolutePath());
                } else {
                    System.err.println("[UserRepository] 디렉토리 생성 실패: " + parentDir.getAbsolutePath());
                }
            }

            // resources 디렉토리에서 기본 users.yaml 파일 복사
            try (InputStream resourceInput = getClass().getResourceAsStream("/data/users.yaml");
                 OutputStream output = new FileOutputStream(file)) {

                if (resourceInput == null) {
                    System.err.println("[UserRepository] resources/data/users.yaml 리소스가 없습니다.");
                    return;
                }

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = resourceInput.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                System.out.println("[UserRepository] 리소스 파일 복사 완료");

            } catch (IOException e) {
                System.err.println("[UserRepository] 리소스 파일 복사 중 오류: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        // 복사한 파일 또는 기존 파일에서 사용자 정보 로딩
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

    // 사용자 인증 (학번 + 비밀번호)
    public BasicResponse validate(String number, String pw) {
        try {
            for (User user : users) {
                if (user.number.equals(number)) {
                    if (user.password.equals(pw)) {
                        return new BasicResponse("200", "로그인 성공");
                    } else {
                        return new BasicResponse("401", "비밀번호 입력 오류 입니다.");
                    }
                }
            }
            return new BasicResponse("400", "존재하지 않는 아이디 입니다.");
        } catch (Exception e) {
            System.err.println("[UserRepository] 로그인 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new BasicResponse("500", "로그인 처리 중 시스템 오류가 발생했습니다.");
        }
    }

    // 사용자 저장 (회원가입)
    public BasicResponse save(String number, String pw, String name, String major) {
        try {
            for (User user : users) {
                if (user.number.equals(number)) {
                    return new BasicResponse("400", "이미 가입된 사용자 정보 입니다.");
                }
            }

            users.add(new User(number, pw, name, major));
            saveAllToFile();
            return new BasicResponse("200", "회원가입 성공");
        } catch (Exception e) {
            System.err.println("[UserRepository] 회원가입 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new BasicResponse("500", "회원가입 실패");
        }
    }

    // 학번으로 사용자 조회
    public BasicResponse findByNumber(String number) {
        for (User user : users) {
            if (user.number.equals(number)) {
                return new BasicResponse("200", user);
            }
        }
        return new BasicResponse("404", "해당 학번의 사용자가 존재하지 않습니다.");
    }

    // 전체 사용자 목록 반환
    public BasicResponse findAll() {
        return new BasicResponse("200", new ArrayList<>(users));
    }

    // 학번으로 사용자 삭제
    public BasicResponse deleteByNumber(String number) {
        boolean removed = users.removeIf(u -> u.number.equals(number));
        saveAllToFile();
        return removed
                ? new BasicResponse("200", "삭제 성공")
                : new BasicResponse("404", "삭제할 사용자가 존재하지 않습니다.");
    }

    // 사용자 존재 여부 확인
    public BasicResponse existsByNumber(String number) {
        return users.stream().anyMatch(u -> u.number.equals(number))
                ? new BasicResponse("200", "존재함")
                : new BasicResponse("404", "존재하지 않음");
    }

    // 사용자 정보 수정
    public BasicResponse update(String number, String pw, String name, String major) {
        for (User user : users) {
            if (user.number.equals(number)) {
                user.password = pw;
                user.name = name;
                user.major = major;
                saveAllToFile();
                return new BasicResponse("200", "사용자 정보가 수정되었습니다.");
            }
        }
        return new BasicResponse("404", "해당 학번의 사용자가 존재하지 않습니다.");
    }
}