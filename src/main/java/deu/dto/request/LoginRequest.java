package deu.dto.request;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    public String number;
    public String password;

    public LoginRequest(String number, String password) {
        this.number = number;
        this.password = password;
    }
}