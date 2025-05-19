package deu.controller.business;

import deu.model.dto.request.data.lecture.LectureRequest;
import deu.model.dto.response.BasicResponse;
import deu.service.LectureService;

// 강의 컨트롤러
public class LectureController {
    private static final LectureController instance = new LectureController();

    private LectureController() {}

    public static LectureController getInstance() {
        return instance;
    }

    private final LectureService lectureService = new LectureService();

    public Object handleReturnLectureOfWeek(LectureRequest payload){
        return lectureService.returnLectureOfWeek(payload);
    }
}
