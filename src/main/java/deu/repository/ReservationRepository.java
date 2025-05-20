package deu.repository;

import deu.model.entity.Reservation;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {
    // 예약 정보가 저장될 YAML 파일 경로
    private static final String FILE_PATH = "reservations.yaml";

    // 싱글턴 인스턴스
    private static final ReservationRepository instance = new ReservationRepository();

    // 메모리에 저장된 예약 목록
    private final List<Reservation> reservationList = new ArrayList<>();

    // SnakeYAML 객체
    private final Yaml yaml;

    // 생성자 - 파일에서 기존 예약을 불러오고 YAML 포맷 설정을 초기화
    private ReservationRepository() {
        // YAML 출력 형식 설정
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true); // 사람이 읽기 쉽게 줄바꿈 허용
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 들여쓰기 블록 스타일 사용

        // 객체 필드 처리 설정
        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true); // 없는 필드는 무시

        // YAML 로딩 설정
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(Reservation.class, loaderOptions); // Reservation 클래스와 매핑

        // YAML 객체 생성
        this.yaml = new Yaml(constructor, representer, options);

        // 서버 실행 시 기존 예약 파일 불러오기

    }

    // 싱글턴 인스턴스 반환
    public static ReservationRepository getInstance() { return instance; }

    // 예약 저장 - 메모리와 파일에 동시에 반영
    public void save(Reservation reservation) {
        reservationList.add(reservation);  // 메모리 저장
        saveToFile();                      // 파일로 저장
    }

    // 모든 예약 정보 반환
    public List<Reservation> findAll() {
        return new ArrayList<>(reservationList); // 복사본 반환 (외부 수정 방지)
    }

    // 특정 사용자(userId)의 예약만 필터링하여 반환
    public List<Reservation> findByUser(String userId) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation r : reservationList) {
            if (r.getUserId().equals(userId)) {
                results.add(r);
            }
        }
        return results;
    }

    // 중복 예약 확인 (같은 강의실에서 시간이 겹치는 예약이 있는지 확인)
    public boolean isDuplicate(String classroom, LocalDateTime start, LocalDateTime end) {
        for (Reservation r : reservationList) {
            if (r.getClassroom().equals(classroom)) {
                // 시간 겹침 조건: !(끝 < 시작 || 시작 > 끝)
                if (!(r.getEndTime().isBefore(start) || r.getStartTime().isAfter(end))) {
                    return true;
                }
            }
        }
        return false;
    }

    // 서버 시작 시 파일로부터 예약 데이터 로드
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return; // 파일 없으면 생략

        try (InputStream input = new FileInputStream(file)) {
            // 파일 내 YAML 객체들을 하나씩 읽어옴
            Iterable<Object> loadedObjects = yaml.loadAll(input);
            for (Object obj : loadedObjects) {
                if (obj instanceof Reservation) {
                    reservationList.add((Reservation) obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 파일 읽기 오류 출력
        }
    }
    public boolean delete(String userId, LocalDateTime startTime) {
        boolean removed = reservationList.removeIf(r ->
                r.getUserId().equals(userId) && r.getStartTime().equals(startTime)
        );
        if (removed) saveToFile();
        return removed;
    }

    public boolean update(String userId, LocalDateTime originalStartTime, Reservation updated) {
        boolean deleted = delete(userId, originalStartTime);
        if (deleted) {
            save(updated);
            return true;
        }
        return false;
    }
    // 현재 메모리의 예약 목록을 YAML 파일로 저장
    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            yaml.dumpAll(reservationList.iterator(), writer); // 모든 예약을 YAML 형식으로 저장
        } catch (IOException e) {
            e.printStackTrace(); // 파일 쓰기 오류 출력
        }
    }

}
