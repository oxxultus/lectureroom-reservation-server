package deu.model.entity;

public class User {
    public String number;
    public String password;
    public String name;
    public String major;

    public User(String number, String password, String name, String major) {
        this.number = number;
        this.password = password;
        this.name = name;
        this.major = major;
    }
}
