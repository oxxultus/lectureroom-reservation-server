package deu.model.dto.request.data.user;

import java.io.Serializable;

public class DeleteRequest implements Serializable {
    public String number;

    public DeleteRequest(String number) {
        this.number = number;
    }
}
