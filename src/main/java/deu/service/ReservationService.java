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

    // 사용자 ============================================================================================================
    /* TODO: 예약 요청 (성공/실패 코드와 메시지 반환)
    * 필드에 지정된 건물, 층, 강의실, 날짜, 요일 ,시간 제목, 설명 인자값을 가져와서
    * 해당 층,강의실,시간의 예약이 비어있다면 해당 시간대에 해당 데이터를 바탕으로 데이터를 추가 할 수 있어야 한다.
    * */

    /* TODO: 개인별 예약 삭제 (성공/실패 코드와 메시지 반환)
    * 건물, 층, 강의실, 날짜, 요일 ,시간 값을 인자로 받아서
    * 로그인 한 사용자와 해당 예약자가 동일하다면 해당 예약을 삭제할 수 있다.
    * */

    /* TODO: 사용자 예약 수정 (성공/실패 코드와 메시지 반환)
    * 뭔 인자르 받아서 수정 처리를 해야하지
    * 각 예약 생성 시 고유번호를 생성해야 겠네
    * 귀찮아 죽겠네 예약 생성 시 예약 고유 번호를 생성하거나
    * 객체를 찾아서 검사하고 반환해야 겠는데 해당 검사하는걸
    *
    * 선택한 시간대의 예약에 대하여 건물 층 강의실 요일 시간으로 찾은 뒤 로그인 한 사용자와 해당 예약 시간대의 사용자와 로그인 사용자와 비교해서 올바르면
    * 해당 시간대의 값을 수정하거나 삭제가 가능해야 한다.
    * */

    // TODO: 사용자 예약 조회 (성공/실패 코드와 금일 ~ 일주일 간의 데이터 반환)

    // 관리자 ============================================================================================================
    //
    /* TODO: 관리자 예약 삭제 (성공/실패 코드와 메시지 반환)
     * 건물, 층, 강의실, 날짜, 요일 ,시간 값을 인자로 받아서 해당 시간대의 예약을 삭제한다.
     * */

    /* TODO: 관리자 예약 수정 (성공/실패 코드와 메시지 반환)
     * 선택한 시간대의 예약에 대하여 건물, 층, 강의실, 날짜, 요일, 시간, 으로 찾은 뒤 값이 있다면 해당 예약을 수정할 수 있다.
     * */

    /* TODO: 예약 상태 변경 (성공/실패 코드와 메시지 반환) - 해당 메서드는 관리자가 예약 요청을 수락 거절 처리할 때 사용합니다.
    * 건물, 층, 강의실, 날짜, 요일 ,시간 값을 인자로 받아서 해당 시간대의 예약을 수락 처리한다.
    * */

    /* TODO: 해당 강의실의 모든 예약 조회 (성공/실패 코드와 금일 ~ 일주일 간의 데이터 반환)
     * 건물, 층, 강의실 값을 인자로 받아서 금일 ~ 일주일 간의 예약 정보를 전달한다.
     * */

}

