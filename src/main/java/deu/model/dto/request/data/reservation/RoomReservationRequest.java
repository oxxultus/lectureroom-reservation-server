package deu.model.dto.request.data.reservation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter

public class RoomReservationRequest implements Serializable {
    private String building;
    private String floor;
    private String lectureroom;

    public RoomReservationRequest(String building, String floor, String lectureroom) {
        this.building = building;
        this.floor = floor;
        this.lectureroom = lectureroom;

    }
}
