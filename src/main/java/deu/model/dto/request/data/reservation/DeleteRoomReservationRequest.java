package deu.model.dto.request.data.reservation;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter

public class DeleteRoomReservationRequest implements Serializable {
    private String number;
    private String roomReservationId;

    public DeleteRoomReservationRequest(String number, String roomReservationId) {
        this.number = number;
        this.roomReservationId = roomReservationId;
    }
}
