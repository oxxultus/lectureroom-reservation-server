package deu.repository;

import deu.model.entity.Lecture;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author oixikite
 * @modifier oxxultus
 * @since 2025.05.16
 */
public class LectureRepository {
    // Singleton instance
    private static final LectureRepository instance = new LectureRepository();

    // 강의 리스트
    private final List<Lecture> lectureList = new ArrayList<>();

    // YAML 파일 경로 (JAR 또는 IDE 실행 경로 기준)
    // 해당 데이터 구조로 지정하면 추후 JAR 로 실행시 해당 JAR 폴더에 data/lectures.yaml로 생성된다.
    private final String FILE_PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator + "lectures.yaml";

    // SnakeYAML 객체
    private final Yaml yaml;

    /* 강의 데이터를 감싸는 내부 클래스 - 아래 형식을 유지하기 위해 사용한다.
    * lectures:
    *    - id: CS101
    *       ...
    * */
    public static class LectureWrapper {
        public List<Lecture> lectures = new ArrayList<>();
    }

    // 생성자
    private LectureRepository() {
        // YAML 저장 시 옵션 (예쁘게 출력)
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);

        // 파일에서 불러오기
        loadAllFromFile();
    }

    // 외부에서 접근하는 싱글톤 인스턴스
    public static LectureRepository getInstance() {
        return instance;
    }

    // 파일 저장
    private void saveAllToFile() {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();

        // 디렉토리가 없으면 생성
        if (!parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (dirCreated) {
                System.out.println("[LectureRepository] 디렉토리 생성됨: " + parentDir.getAbsolutePath());
            } else {
                System.err.println("[LectureRepository] 디렉토리 생성 실패: " + parentDir.getAbsolutePath());
            }
        }

        try (Writer writer = new FileWriter(file)) {
            LectureWrapper wrapper = new LectureWrapper();
            wrapper.lectures = lectureList;
            yaml.dump(wrapper, writer);
            System.out.println("[LectureRepository] 파일 저장 완료: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[LectureRepository] 파일 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 파일에서 불러오기
    private void loadAllFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("[LectureRepository] 파일이 존재하지 않아 새로 생성 예정: " + file.getAbsolutePath());
            return;
        }

        try (InputStream input = new FileInputStream(file)) {
            LectureWrapper wrapper = yaml.loadAs(input, LectureWrapper.class);
            if (wrapper != null && wrapper.lectures != null) {
                lectureList.clear();
                lectureList.addAll(wrapper.lectures);
                System.out.println("[LectureRepository] 파일 로딩 완료: " + file.getAbsolutePath());
                System.out.println("[LectureRepository] 불러온 강의 수: " + lectureList.size());
            } else {
                System.out.println("[LectureRepository] 파일은 있으나 강의 데이터가 비어있습니다.");
            }
        } catch (IOException e) {
            System.err.println("[LectureRepository] 파일 로딩 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 강의 저장 (수정 포함)
    public String save(Lecture lecture) {
        if (lecture == null || lecture.getId() == null || lecture.getId().isBlank()) {
            return "400"; // 잘못된 요청
        }

        deleteById(lecture.getId());  // 기존 것 제거
        lectureList.add(lecture);
        saveAllToFile();
        return "200"; // 성공
    }

    // 강의 삭제
    public String deleteById(String id) {
        boolean removed = lectureList.removeIf(l -> l.getId().equals(id));
        saveAllToFile();
        return removed ? "200" : "404"; // 삭제 성공/실패
    }

    // 강의 존재 여부
    public String existsById(String id) {
        return lectureList.stream()
                .anyMatch(l -> l.getId().equals(id)) ? "200" : "404";
    }

    // 강의 ID로 조회
    public Optional<Lecture> findById(String id) {
        return lectureList.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst();
    }

    // 전체 강의 리스트 반환
    public List<Lecture> findAll() {
        return new ArrayList<>(lectureList); // 원본 보호
    }

    // 강의명 + 교수명으로 ID 조회
    public Optional<String> findIdByLectureNameAndProfessor(String title, String professor) {
        return lectureList.stream()
                .filter(l -> l.getTitle().equals(title) && l.getProfessor().equals(professor))
                .map(Lecture::getId)
                .findFirst();
    }
}