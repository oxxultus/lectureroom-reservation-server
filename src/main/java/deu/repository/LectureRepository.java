package deu.repository;

import deu.model.entity.LectureList;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *
 * @author oixikite
 */

public class LectureRepository {
    // Singleton instance
    private static final LectureRepository instance = new LectureRepository();

    // 강의 리스트
    private final List<Lecture> lectureList = new ArrayList<>();

    // YAML 파일 경로
    private final String FILE_PATH = "data/lectures.yaml";

    // SnakeYAML 객체
    private final Yaml yaml;

    // 강의 데이터를 감싸는 내부 클래스
    public static class LectureWrapper {
        public List<Lecture> lectures = new ArrayList<>();
    }

    // 생성자 (private)
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

    // 강의 저장 (수정 포함)
    public void save(Lecture lecture) {
        deleteById(lecture.id);  // 중복 제거 후
        lectureList.add(lecture);
        saveAllToFile();         // 파일로 저장
    }

    // 강의 ID로 조회
    public Optional<Lecture> findById(String id) {
        return lectureList.stream().filter(l -> l.id.equals(id)).findFirst();
    }

    // 전체 강의 리스트 반환
    public List<Lecture> findAll() {
        return new ArrayList<>(lectureList); // 원본 보호
    }

    // 강의 삭제
    public void deleteById(String id) {
        lectureList.removeIf(l -> l.id.equals(id));
        saveAllToFile();
    }


}