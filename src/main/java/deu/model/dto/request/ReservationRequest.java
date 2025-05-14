package deu.model.dto.request;

import java.io.Serializable;

public class ReservationRequest implements Serializable {
    public String userId;
    public String date;       // 예: "2025-05-06"
    public String time;       // 예: "14:00"
    public String room;       // 예: "101호"

    public ReservationRequest(String userId, String date, String time, String room) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.room = room;
    }
}
