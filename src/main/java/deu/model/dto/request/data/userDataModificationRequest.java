package deu.model.dto.request.data;

public class userDataModificationRequest {
    public String number;
    public String password;
    public String name;
    public String major;

    public userDataModificationRequest(String number, String password, String name, String major) {
        this.number = number;
        this.password = password;
        this.name = name;
        this.major = major;
    }
}
