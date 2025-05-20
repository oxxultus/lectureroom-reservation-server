package deu.service;

import deu.model.dto.request.data.lecture.LectureRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LectureServiceTest {

    LectureService lectureService = new LectureService();

    @DisplayName("강의실 주간 일정이 정확한 형식으로 반환되는지 검증")
    @Test
    void testReturnLectureOfWeek_withExistingLecture() {
        //실제 존재하는 강의 (정보관, 9층, 911호)
        LectureRequest request = new LectureRequest("정보관", "9", "911");

        BasicResponse res = lectureService.returnLectureOfWeek(request);

        assertNotNull(res, "응답은 null이 아니어야 함");
        assertEquals("200", res.code, "응답 코드는 200이어야 함");
        assertInstanceOf(Lecture[][].class, res.data, "data는 2차원 Lecture 배열이어야 함");

        Lecture[][] schedule = (Lecture[][]) res.data;

        assertEquals(7, schedule.length, "요일 수는 7이어야 함");
        assertEquals(13, schedule[0].length, "교시 수는 13이어야 함");

        // '컴퓨터비전응용' 강의가 포함되어 있는지 확인
        boolean found = false;
        for (Lecture[] row : schedule) {
            for (Lecture lec : row) {
                if (lec != null && "컴퓨터비전응용".equals(lec.getTitle())) {
                    found = true;
                    break;
                }
            }
        }

        assertTrue(found, "'컴퓨터비전응용' 강의가 스케줄에 포함되어 있어야 함");
    }
}