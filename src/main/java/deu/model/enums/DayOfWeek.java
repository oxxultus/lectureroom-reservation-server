package deu.model.enums;

public enum DayOfWeek {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    private final int index;

    DayOfWeek(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    // 현재 날짜 기준으로 요일부터 시작한 7일 순서 반환
    public static DayOfWeek[] getOrderedFromToday() {
        int todayIdx = java.time.LocalDate.now().getDayOfWeek().getValue() - 1; // MONDAY=1
        DayOfWeek[] all = DayOfWeek.values();
        DayOfWeek[] ordered = new DayOfWeek[7];

        for (int i = 0; i < 7; i++) {
            ordered[i] = all[(todayIdx + i) % 7];
        }
        return ordered;
    }

    // 문자열로부터 enum 변환 (안전하게)
    public static DayOfWeek fromString(String dayName) {
        try {
            return DayOfWeek.valueOf(dayName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}