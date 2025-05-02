package deu.dto;

import java.io.Serializable;

public class BasicResponse implements Serializable {
    public boolean success;
    public String message;

    public BasicResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}