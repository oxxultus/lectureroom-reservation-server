package deu.model.dto.request.data.user;

import java.io.Serializable;

public class ExistsRequest implements Serializable {
    public String number;

    public ExistsRequest(String number) {
        this.number = number;
    }
}
