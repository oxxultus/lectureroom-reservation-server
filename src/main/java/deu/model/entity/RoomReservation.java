package deu.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RoomReservation implements Serializable {

    private String id;               // 고유 예약 ID
    private String buildingName;    // 건물명
    private String floor;           // 층 정보
    private String lectureRoom;     // 강의실 이름
    private String number;          // 학번 또는 예약 번호
    private String status;          // 예약 상태 (예: PENDING, APPROVED 등)
    private String date;            // 예약 날짜 (yyyy-MM-dd)
    private String dayOfTheWeek;    // 요일 (예: MONDAY, TUESDAY)

    private String startTime;       // 시작 시간 or 교시 (예: "09:00" or "1교시")
    private String endTime;         // 종료 시간 or 교시 (예: "10:00" or "2교시")
}
