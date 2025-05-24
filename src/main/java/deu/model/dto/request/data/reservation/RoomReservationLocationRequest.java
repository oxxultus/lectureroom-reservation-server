package deu.model.dto.request.data.reservation;

import java.io.Serializable;

public class RoomReservationLocationRequest implements Serializable {
    public String building;
    public String floor;
    public String lectureroom;

    public RoomReservationLocationRequest(String building, String floor, String lectureroom) {
        this.building = building;
        this.floor = floor;
        this.lectureroom = lectureroom;
    }
}
