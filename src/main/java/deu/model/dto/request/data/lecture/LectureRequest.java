package deu.model.dto.request.data.lecture;

import java.io.Serializable;

public class LectureRequest implements Serializable {
    public String building;
    public String floor;
    public String lectureroom;

    public LectureRequest(String building, String floor, String lectureroom) {
        this.building = building;
        this.floor = floor;
        this.lectureroom = lectureroom;
    }
}
