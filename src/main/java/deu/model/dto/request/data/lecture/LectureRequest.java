package deu.model.dto.request.data.lecture;

public class LectureRequest {
    public String building;
    public String floor;
    public String lectureroom;

    public LectureRequest(String building, String floor, String room) {
        this.building = building;
        this.floor = floor;
        this.lectureroom = lectureroom;
    }
}
