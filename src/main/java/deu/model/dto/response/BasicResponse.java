package deu.model.dto.response;

import java.io.Serializable;

public class BasicResponse implements Serializable {
    public String code;
    public String message;

    public BasicResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}