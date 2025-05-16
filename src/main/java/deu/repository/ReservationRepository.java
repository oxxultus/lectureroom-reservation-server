package deu.repository;

import deu.model.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {
    private List<Reservation> reservations = new ArrayList<>();

    // 예약 추가 메서드
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // 전체 예약 리스트 반환
    public List<Reservation> getAllReservations() {
        return reservations;
    }

    // 특정 강의실의 예약 리스트 반환
    public List<Reservation> getReservationsByClassroom(String classroom) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getClassroom().equals(classroom)) {
                result.add(res);
            }
        }
        return result;
    }

    // 중복 예약 확인 메서드
    public boolean isOverlapping(Reservation newReservation) {
        for (Reservation res : reservations) {
            if (res.getClassroom().equals(newReservation.getClassroom()) &&
                    newReservation.getStartTime().isBefore(res.getEndTime()) &&
                    newReservation.getEndTime().isAfter(res.getStartTime())) {
                return true; // 중복됨
            }
        }
        return false; // 중복되지 않음
    }
}
