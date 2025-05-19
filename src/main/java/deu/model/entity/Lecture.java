package deu.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author oixikite
 * @modifier oxxultus
 * @since 2025.05.16
 */

@Getter
@Setter
@NoArgsConstructor
public class Lecture implements Serializable {
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
