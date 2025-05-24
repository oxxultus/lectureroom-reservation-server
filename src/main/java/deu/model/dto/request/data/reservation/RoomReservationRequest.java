package deu.model.dto.request.data.reservation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
public class RoomReservationRequest implements Serializable {
    @Getter
    @Setter
    private String id;
    public String buildingName;
    public String floor;
    public String lectureRoom;
    public String title; // 제목
    public String description; // 설명

    public String date; // 날짜
    public String dayOfTheWeek; // 요일
    public String startTime;
    public String endTime;

    public String number;
    @Getter
    private String status;

    public RoomReservationRequest(String buildingName, String floor, String lectureRoom, String title, String description, String date, String dayOfTheWeek, String startTime, String endTime,String number) {
        this.buildingName = buildingName;
        this.floor = floor;
        this.lectureRoom = lectureRoom;
        this.title = title;
        this.description = description;
        this.date = date;
        this.dayOfTheWeek = dayOfTheWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.number = number;
    }

}
