package deu.controller.business;

import deu.model.dto.response.BasicResponse;
import deu.model.entity.Reservation;
import deu.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 예약 컨트롤러
public class ReservationController {
    private static final ReservationController instance = new ReservationController();
    private final ReservationRepository reservationRepository = ReservationRepository.getInstance();
    private ReservationController() {}

    public static ReservationController getInstance() {
        return instance;
    }

    // private final ReservationService reservationService = new ReservationService();

    // 각 메서드 앞에 handle를 꼭 붙혀주세요 (알관성 유지)
    // TODO: 예약 요청 (성공/실패 코드와 메시지 반환)
    public BasicResponse handleCreateReservation(Reservation reservation) {
        boolean isDup = reservationRepository.isDuplicate(
                reservation.getClassroom(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        if (isDup) {
            return new BasicResponse("400", "예약에 실패했습니다. 이미 예약된 시간입니다.");
        }

        reservationRepository.save(reservation);
        return new BasicResponse("200", "예약이 성공적으로 완료되었습니다.");
    }

    // TODO: 예약 삭제 (성공/실패 코드와 메시지 반환)
    public BasicResponse handleDeleteReservation(String userId, LocalDateTime startTime) {
        boolean removed = reservationRepository.delete(userId, startTime);
        if (removed) {
            return new BasicResponse("200", "예약이 성공적으로 삭제되었습니다.");
        } else {
            return new BasicResponse("404", "해당 예약을 찾을 수 없습니다.");
        }
    }

    // TODO: 예약 수정 (성공/실패 코드와 메시지 반환)
    public BasicResponse handleUpdateReservation(String userId, LocalDateTime originalStartTime, Reservation updated) {
        boolean success = reservationRepository.update(userId, originalStartTime, updated);
        if (success) {
            return new BasicResponse("200", "예약이 성공적으로 수정되었습니다.");
        } else {
            return new BasicResponse("404", "예약 수정에 실패했습니다. 기존 예약을 찾을 수 없습니다.");
        }
    }

    // TODO: 사용자 예약 조회 (금일 ~ 일주일간의 데이터 반환)
    public BasicResponse handleFindUserReservations(String userId) {
        LocalDate today = LocalDate.now();
        LocalDate weekLater = today.plusDays(7);

        List<Reservation> result = reservationRepository.findByUser(userId).stream()
                .filter(r -> {
                    LocalDate startDate = r.getStartTime().toLocalDate();
                    return !startDate.isBefore(today) && !startDate.isAfter(weekLater);
                })
                .collect(Collectors.toList());

        return new BasicResponse("200", result);
    }

    // TODO: 모든 예약 조회 (금일 ~ 일주일간의 데이터 반환)
    public BasicResponse handleFindAllReservations() {
        LocalDate today = LocalDate.now();
        LocalDate weekLater = today.plusDays(7);

        List<Reservation> result = reservationRepository.findAll().stream()
                .filter(r -> {
                    LocalDate startDate = r.getStartTime().toLocalDate();
                    return !startDate.isBefore(today) && !startDate.isAfter(weekLater);
                })
                .collect(Collectors.toList());

        return new BasicResponse("200", result);
    }
}

