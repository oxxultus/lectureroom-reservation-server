package deu.model.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class User implements Serializable{
    public String number;
    public String password;
    public String name;
    public String major;

    public User() {
    }

    public User(String number, String password, String name, String major) {
        this.number = number;
        this.password = password;
        this.name = name;
        this.major = major;
    }
}
