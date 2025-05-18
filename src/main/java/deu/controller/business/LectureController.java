package deu.controller.business;

// 강의 컨트롤러
public class LectureController {
    private static final LectureController instance = new LectureController();

    private LectureController() {}

    public static LectureController getInstance() {
        return instance;
    }

    // 강의 서비스 연결
    // private final LectureService lectureService = new LectureService();

    // 각 메서드 앞에 handle를 꼭 붙혀주세요 (알관성 유지)
    // TODO: 이번 학기의 강의 시간 정보를 모두 불러옵니다. (성공/실패 코드와 강의 객체 리스트(컬랙션) 반환)
}
