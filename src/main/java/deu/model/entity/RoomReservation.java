package deu.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RoomReservation implements Serializable {
    private String id = UUID.randomUUID().toString(); // 예약 생성 시 UUID 자동 할당
    private String buildingName;
    private String floor;
    private String lectureRoom;

    private String number;
    private String status = "대기";

    private String title; // 제목
    private String description; // 설명

    private String date; // 날짜
    private String dayOfTheWeek; // 요일

    private String startTime;
    private String endTime;
}