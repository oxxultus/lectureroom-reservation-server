package deu.model.dto.request.data.user;

import java.io.Serializable;

public class FindRequest implements Serializable {
    public String number;

    public FindRequest(String number) {
        this.number = number;
    }
}
