package deu.model.dto.request;

import java.io.Serializable;

public class LogoutRequest implements Serializable {
    public String number;
    public String password;

    public LogoutRequest(String number, String password) {
        this.number = number;
        this.password = password;
    }
}