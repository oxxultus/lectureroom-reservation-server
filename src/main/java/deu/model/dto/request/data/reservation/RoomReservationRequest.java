package deu.model.dto.request.data.reservation;

import java.io.Serializable;

public class RoomReservationRequest implements Serializable {
    public String building;
    public String floor;
    public String lectureroom;

    public RoomReservationRequest(String building, String floor, String lectureroom) {
        this.building = building;
        this.floor = floor;
        this.lectureroom = lectureroom;
    }
}
