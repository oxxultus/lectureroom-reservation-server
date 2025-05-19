package deu.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Reservation implements Serializable {
    private String userId;      // 예약한 사용자 ID
    private String classroom;   // 예약된 강의실
    private LocalDateTime startTime;  // 예약 시작 시간
    private LocalDateTime endTime;    // 예약 종료 시간

    public Reservation(String userId, String classroom, LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.classroom = classroom;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter 및 Setter 추가
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
