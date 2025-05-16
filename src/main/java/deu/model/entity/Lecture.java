package deu.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 *
 * @author oixikite
 * @modifier oxxultus
 * @since 2025.05.16
 */

@Getter
@Setter
@NoArgsConstructor
public class Lecture {
    private String id;
    private String title;
    private String lectureroom;
    private String building;
    private String floor;
    private String professor;
    private String day;  // 요일
    private String startTime;
    private String endTime;
}
