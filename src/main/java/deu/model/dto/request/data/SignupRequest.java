package deu.model.dto.request.data;

import java.io.Serializable;

public class SignupRequest implements Serializable {
    public String number;
    public String password;
    public String name;
    public String major;

    public SignupRequest(String number, String password, String name, String major) {
        this.number = number;
        this.password = password;
        this.name = name;
        this.major = major;
    }
}