package deu.model.dto.response;

import java.io.Serializable;

public class BasicResponse implements Serializable {
    public String code;
    public Object data;

    public BasicResponse(String code, Object data) {
        this.code = code;
        this.data = data;
    }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}