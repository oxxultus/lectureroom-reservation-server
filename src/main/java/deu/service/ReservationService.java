package deu.service;

import deu.model.entity.Reservation;
import deu.model.entity.ReservationStatus;
import deu.repository.ReservationRepository;
import deu.model.dto.response.BasicResponse;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {

    // 예약 저장 요청 처리 (성공/실패 메시지 반환)
    public BasicResponse createReservation(Reservation reservation) {
        // 중복 예약 확인
        boolean isDup = ReservationRepository.getInstance().isDuplicate(
                reservation.getClassroom(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        if (isDup) {
            return new BasicResponse("409", "중복된 예약입니다.");
        }

        // 중복이 아니라면 저장
        ReservationRepository.getInstance().save(reservation);
        return new BasicResponse("200", "예약이 완료되었습니다.");
    }

    // 모든 예약 조회
    public List<Reservation> getAllReservations() {
        return ReservationRepository.getInstance().findAll();
    }

    // 사용자별 예약 조회
    public List<Reservation> getReservationsByUser(String userId) {
        return ReservationRepository.getInstance().findByUser(userId);
    }

    // 예약 상태 변경
    public BasicResponse updateReservationStatus(String userId, LocalDateTime startTime, ReservationStatus newStatus) {
        List<Reservation> reservations = ReservationRepository.getInstance().findByUser(userId);
        for (Reservation r : reservations) {
            if (r.getStartTime().equals(startTime)) {
                r.setStatus(newStatus);
                ReservationRepository.getInstance().saveToFile(); // 파일 반영
                return new BasicResponse("200", "예약 상태가 변경되었습니다.");
            }
        }
        return new BasicResponse("404", "해당 예약을 찾을 수 없습니다.");
    }
    // TODO: 예약 삭제 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 수정 (성공/실패 코드와 메시지 반환)

    // TODO: 예약 상태 변경 (성공/실패 코드와 메시지 반환) - 해당 메서드는 관리자가 예약 요청을 수락 거절 처리할 때 사용합니다.

    // TODO: 사용자 예약 조회 (성공/실패 코드와 금일 ~ 일주일간의 데이터 반환)

    // TODO: 모든 예약 조회 (성공/실패 코드와 금일 일주일간의 데이터 반환)
}
