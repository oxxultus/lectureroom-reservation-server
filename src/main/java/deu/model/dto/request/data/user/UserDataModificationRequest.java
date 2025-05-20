package deu.model.dto.request.data.user;

import java.io.Serializable;

public class UserDataModificationRequest implements Serializable {
    public String number;
    public String password;
    public String name;
    public String major;

    public UserDataModificationRequest(String number, String password, String name, String major) {
        this.number = number;
        this.password = password;
        this.name = name;
        this.major = major;
    }
}
