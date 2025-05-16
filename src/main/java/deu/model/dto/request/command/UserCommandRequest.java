package deu.model.dto.request.command;

import java.io.Serializable;

public class UserCommandRequest implements Serializable {
    public String command; // 예: "예약정보"
    public Object payload; // 추가 전달값 (예: userId, 날짜 등)

    public UserCommandRequest(String command, Object payload) {
        this.command = command;
        this.payload = payload;
    }
}