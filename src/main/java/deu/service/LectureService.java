package deu.service;

import deu.model.dto.request.data.lecture.LectureRequest;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.Lecture;
import deu.model.enums.DayOfWeek;
import deu.repository.LectureRepository;

import java.time.LocalTime;
import java.util.List;

public class LectureService {

    // 특정 강의실의 금일 + 7일 까지의 강의 데이터를 배열로 반환한다.
    public BasicResponse returnLectureOfWeek(LectureRequest payload) {
        System.out.printf("[필터 확인] 요청 건물: %s, 층: %s, 강의실: %s\n", payload.building, payload.floor, payload.lectureroom);

        String building = payload.building;
        String floor = payload.floor;
        String lectureroom = payload.lectureroom;

        // 1. 오늘 기준으로 7일간의 요일 배열을 가져옴 (오늘부터 일주일간 순차 조회용)
        DayOfWeek[] orderedDays = DayOfWeek.getOrderedFromToday();

        // 2. 스케줄 2차원 배열 생성 (행: 요일 7개, 열: 0교시~12교시 총 13개)
        Lecture[][] schedule = new Lecture[7][13];

        // 3. 전체 강의 목록을 로드 (파일 기반 리포지토리에서 YAML 파싱하여 불러옴)
        List<Lecture> lectures = LectureRepository.getInstance().findAll();
        if (lectures.isEmpty()) {
            return new BasicResponse("404", "파일에서 강의 정보를 불러오지 못했습니다.");
        }

        // 4. 강의 목록 중 대상 강의실에 해당하는 강의만 필터링하고 시간표에 배치
        for (Lecture lec : lectures) {
            // 유효한 강의인지 (null 여부, 강의실/건물/층 일치, 요일/시간 존재 여부 등)
            if (!isValidLectureForRoom(lec, building, floor, lectureroom)) continue;

            // 문자열로 된 요일을 enum 타입으로 변환 (예: "MONDAY" → DayOfWeek.MONDAY)
            DayOfWeek lecDay;
            try {
                lecDay = DayOfWeek.fromString(lec.getDay());
            } catch (IllegalArgumentException e) {
                // 잘못된 요일 텍스트일 경우 무시하고 계속 진행
                System.err.println("[LectureService] 잘못된 요일: " + lec.getDay());
                continue;
            }

            // 변환된 요일이 현재 7일 배열의 몇 번째에 해당하는지 확인
            int dayIndex = indexOfDay(orderedDays, lecDay);
            if (dayIndex == -1) continue; // 요일이 7일 범위에 없다면 스킵

            try {
                // 시작 및 종료 시간을 LocalTime으로 파싱 ("14:00" → LocalTime.of(14, 0))
                LocalTime start = LocalTime.parse(lec.getStartTime());
                LocalTime end = LocalTime.parse(lec.getEndTime());

                // 5. 각 교시에 대해 해당 강의가 포함되는지 체크하여 스케줄에 할당
                for (int period = 0; period < 13; period++) {
                    // 각 교시는 9시부터 1시간 간격 (9~10, 10~11, ..., 21~22)
                    LocalTime periodStart = LocalTime.of(9 + period, 0);
                    LocalTime periodEnd = periodStart.plusHours(1);

                    // 강의 시간이 이 교시와 겹치는 경우 해당 칸에 강의 배치
                    if (!end.isBefore(periodStart) && !start.isAfter(periodEnd.minusMinutes(1))) {
                        schedule[dayIndex][period] = lec;
                    }
                }
            } catch (Exception e) {
                // 시간 형식이 잘못된 경우 오류 출력하고 해당 강의 건너뜀
                System.err.println("[LectureService] 시간 파싱 오류: " + lec.getStartTime() + " ~ " + lec.getEndTime());
            }
        }

        // 최종 결과 반환 (스케줄 배열을 포함한 응답 객체)
        return new BasicResponse("200", schedule);
    }

    // 건물, 층, 강의실, 요일, 시간 정보가 모두 유효한지 확인
    private boolean isValidLectureForRoom(Lecture lec, String building, String floor, String room) {
        return lec != null &&
                building != null && building.equals(lec.getBuilding()) &&
                floor != null && floor.equals(lec.getFloor()) &&
                room != null && room.equals(lec.getLectureroom()) &&
                lec.getDay() != null &&
                lec.getStartTime() != null &&
                lec.getEndTime() != null;
    }

    // 요일 배열에서 특정 요일의 인덱스 반환
    private int indexOfDay(DayOfWeek[] array, DayOfWeek target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) return i;
        }
        return -1;
    }
}
