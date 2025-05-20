package deu.repository;

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

    private final List<Reservation> reservationList = new ArrayList<>();
    private final Yaml yaml;

    private ReservationRepository() {
        // YAML 설정
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true);

        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(Reservation.class, loaderOptions);
        this.yaml = new Yaml(constructor, representer, options);

        loadFromFile();
    }

    public static ReservationRepository getInstance() { return instance; }

    public void save(Reservation reservation) {
        reservationList.add(reservation);
        saveToFile();
    }

    public void delete(Reservation reservation) {
        reservationList.remove(reservation);
        saveToFile();
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservationList);
    }

    public List<Reservation> findByUser(String userId) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation r : reservationList) {
            if (r.getNumber().equals(userId)) { // 'number'를 userId로 사용
                results.add(r);
            }
        }
        return results;
    }

    public boolean isDuplicate(String date, String startTime, String lectureRoom) {
        for (Reservation r : reservationList) {
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
                if (obj instanceof Reservation r) {
                    reservationList.add(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
