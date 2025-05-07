package deu.model.dto.response;

import java.io.Serializable;

public class CurrentResponse implements Serializable {
    public int currentUserCount;

    public CurrentResponse(int currentUserCount) {
        this.currentUserCount = currentUserCount;
    }
}
