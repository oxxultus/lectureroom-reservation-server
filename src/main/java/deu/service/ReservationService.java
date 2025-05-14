package deu.service;

import deu.model.dto.request.ReservationRequest;
import deu.model.dto.response.BasicResponse;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ReservationService {
    private static final String FILE_PATH = "reservations.txt";
    private final Set<String> reservationSet = new HashSet<>();

    public ReservationService() {
        loadReservations(); // 서버 시작 시 기존 예약 불러오기
    }

    public BasicResponse reserve(ReservationRequest request) {
        String key = request.date + " " + request.time + " " + request.room;

        if (reservationSet.contains(key)) {
            return new BasicResponse("409", "이미 예약된 시간/강의실입니다.");
        }

        reservationSet.add(key);
        saveToFile(request);

        return new BasicResponse("200", "예약이 완료되었습니다.");
    }

    private void saveToFile(ReservationRequest request) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(request.userId + "," + request.date + "," + request.time + "," + request.room);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadReservations() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String key = parts[1] + " " + parts[2] + " " + parts[3];
                    reservationSet.add(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
