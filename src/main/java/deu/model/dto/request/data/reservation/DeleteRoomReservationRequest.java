package deu.model.dto.request.data.reservation;

import java.io.Serializable;

public class DeleteRoomReservationRequest implements Serializable {
    public String number;
    public String roomReservationId;

    public DeleteRoomReservationRequest(String number, String roomReservationId) {
        this.number = number;
        this.roomReservationId = roomReservationId;
    }
}
