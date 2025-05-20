package deu.controller.business;

import deu.model.dto.request.data.lecture.LectureRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.Lecture;
import deu.service.LectureService;

import java.util.Arrays;

// 강의 컨트롤러
public class LectureController {
    private static final LectureController instance = new LectureController();

    private LectureController() {}

    public static LectureController getInstance() {
        return instance;
    }

    private final LectureService lectureService = new LectureService();

    public Object handleReturnLectureOfWeek(LectureRequest payload) {
        BasicResponse result = lectureService.returnLectureOfWeek(payload);

        // 콘솔에 결과 데이터 출력
        // System.out.println("[LectureController] 반환된 데이터: " + Arrays.deepToString((Lecture[][]) result.data));

        return result;
    }
}
