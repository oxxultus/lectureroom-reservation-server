package deu.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RoomReservation implements Serializable {
    private String id; // 고유번호
    private String buildingName;
    private String floor;
    private String lectureRoom;
    private String number;
    private String status;
    private String title; // 제목
    private String description; // 설명
    private String date; // 날짜
    private String dayOfTheWeek; // 요일

    private String startTime;
    private String endTime;
}