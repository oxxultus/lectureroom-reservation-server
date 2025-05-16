package deu.service;

import deu.model.entity.Reservation;
import deu.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {

    // 예약 저장 요청 처리
    public void createReservation(Reservation reservation) {
        // 중복 예약 확인
        boolean isDup = ReservationRepository.getInstance().isDuplicate(
                reservation.getClassroom(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        if (isDup) {
            System.out.println("중복된 예약입니다. 저장하지 않습니다.");
            return;
        }

        // 중복이 아니라면 저장
        ReservationRepository.getInstance().save(reservation);
        System.out.println("예약 저장 완료!");
    }

    // 모든 예약 조회
    public List<Reservation> getAllReservations() {
        return ReservationRepository.getInstance().findAll();
    }

    // 사용자별 예약 조회
    public List<Reservation> getReservationsByUser(String userId) {
        return ReservationRepository.getInstance().findByUser(userId);
    }

    // TODO: 예약 요청 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 삭제 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 수정 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 상태 변경 (성공/실패 코드와 메시지 반환) - 해당 메서드는 관리자가 예약 요청을 수락 거절 처리할 때 사용합니다.

    // TODO: 사용자 예약 조회 (성공/실패 코드와 금일 ~ 일주일간의 데이터 반환)

    // TODO: 모든 예약 조회 (성공/실패 코드와 금일 일주일간의 데이터 반환)
}
