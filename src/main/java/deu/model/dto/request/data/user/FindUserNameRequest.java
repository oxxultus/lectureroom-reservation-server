package deu.model.dto.request.data.user;

import java.io.Serializable;

public class FindUserNameRequest implements Serializable {
    public String number;
    public String password;

    public FindUserNameRequest(String number, String password) {
        this.number = number;
        this.password = password;
    }
}
