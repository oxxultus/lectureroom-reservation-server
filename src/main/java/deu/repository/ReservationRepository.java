package deu.repository;

import deu.model.entity.RoomReservation;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {
    private static final String FILE_PATH = "reservations.yaml";
    private static final ReservationRepository instance = new ReservationRepository();

    private final List<RoomReservation> reservationList = new ArrayList<>();
    private final Yaml yaml;

    private ReservationRepository() {
        // YAML 설정
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true);

        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(RoomReservation.class, loaderOptions);
        this.yaml = new Yaml(constructor, representer, options);

        loadFromFile();
    }

    public static ReservationRepository getInstance() { return instance; }

    public void save(RoomReservation reservation) {
        reservationList.add(reservation);
        saveToFile();
    }

    public void delete(RoomReservation reservation) {
        reservationList.remove(reservation);
        saveToFile();
    }

    public List<RoomReservation> findAll() {
        return new ArrayList<>(reservationList);
    }

    public List<RoomReservation> findByUser(String userId) {
        List<RoomReservation> results = new ArrayList<>();
        for (RoomReservation r : reservationList) {
            if (r.getNumber().equals(userId)) { // 'number'를 userId로 사용
                results.add(r);
            }
        }
        return results;
    }

    public boolean isDuplicate(String date, String startTime, String lectureRoom) {
        for (RoomReservation r : reservationList) {
            if (r.getDate().equals(date)
                    && r.getStartTime().equals(startTime)
                    && r.getLectureRoom().equals(lectureRoom)) {
                return true;
            }
        }
        return false;
    }

    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            yaml.dumpAll(reservationList.iterator(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (InputStream input = new FileInputStream(file)) {
            Iterable<Object> loadedObjects = yaml.loadAll(input);
            for (Object obj : loadedObjects) {
                if (obj instanceof RoomReservation r) {
                    reservationList.add(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
