package deu.model.dto.request.data.user;

import java.io.Serializable;

public class LogoutRequest implements Serializable {
    public String number;
    public String password;

    public LogoutRequest(String number, String password) {
        this.number = number;
        this.password = password;
    }
}