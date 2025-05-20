package deu.controller.business;

import deu.model.dto.request.data.lecture.LectureRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LectureControllerTest {

    @DisplayName("강의실 주간 일정 반환 테스트")
    @Test
    void testHandleReturnLectureOfWeek() {

        String building = "정보공학관";
        String floor = "9";
        String room = "912";
        LectureRequest request = new LectureRequest(building, floor, room);


        LectureController controller = LectureController.getInstance();
        Object result = controller.handleReturnLectureOfWeek(request);

        assertNotNull(result, "결과가 null이 아니어야 합니다.");
        assertTrue(result instanceof BasicResponse, "결과는 BasicResponse 타입이어야 합니다.");

        BasicResponse response = (BasicResponse) result;
        assertEquals("200", response.code, "응답 코드가 200이어야 합니다.");
        assertTrue(response.data instanceof Lecture[][], "응답 데이터는 Lecture[][] 타입이어야 합니다.");

        Lecture[][] schedule = (Lecture[][]) response.data;
        assertEquals(7, schedule.length, "요일 수는 7개여야 합니다.");
        assertEquals(13, schedule[0].length, "교시 수는 13개여야 합니다.");
    }
}