## 프로젝트 구조
```
├── controller/                # 도메인별 요청 처리
│   ├── SystemController.java  # 요청 분기 라우팅
│   └── business/              # 각 도메인 컨트롤러
├── service/                   # 비즈니스 로직 처리 계층
├── repository/                # YAML 파일 접근 계층
├── model/                     # DTO, Entity 클래스 정의
└── ServerMain.java            # 서버 진입점
```
## 실행 방법

### 요구사항
- Java 21 이상
- Maven 3.x 이상

### 실행 명령어
```
mvn clean package
java -jar target/DeuLectureRoomServer-1.0.0.jar
```
- 서버 기본 포트: 9999
- 데이터 저장 경로: ./data/
