package deu.controller;

import deu.controller.business.*;
import deu.model.dto.request.command.*;
import deu.model.dto.request.data.lecture.LectureRequest;
import deu.model.dto.request.data.user.*;
import deu.model.dto.response.BasicResponse;

public class SystemController {
    private final UserController userController = UserController.getInstance();
    private final UserManagementController userManagementController = UserManagementController.getInstance();
    private final LectureController lectureController = LectureController.getInstance();

    public Object handle(Object request) {
        // 사용자 컨트롤러 이관 - 완료
        if (request instanceof UserCommandRequest r) {
            return switch (r.command) {
                case "로그인" -> userController.handleLogin((LoginRequest) r.payload);
                case "회원가입" -> userController.handleSignup((SignupRequest) r.payload);
                case "로그아웃" -> userController.handleLogout((LogoutRequest) r.payload);
                case "동시접속자" -> userController.handleCurrentUser();
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        // 사용자 관리 컨트롤러 이관 - 완료
        else if (request instanceof UserManagementCommandRequest r) {
            return switch (r.command) {
                case "사용자 수정" -> userManagementController.handleUpdateUser((UserDataModificationRequest) r.payload);
                case "사용자 삭제" -> userManagementController.handleDeleteUser((DeleteRequest) r.payload);
                case "사용자 조회" -> userManagementController.handleFindUser((FindRequest) r.payload);
                case "전체 사용자 조회" -> userManagementController.handleFindAllUsers();
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        // 예약 컨트롤러 이관
        else if (request instanceof ReservationCommandRequest r) {
            return switch (r.command) {
                case "예약 요청" -> userController.handleLogin((LoginRequest) r.payload);
                case "예약 삭제" -> userController.handleSignup((SignupRequest) r.payload);
                case "예약 수정" -> userController.handleLogout((LogoutRequest) r.payload);
                case "사용자 예약 조회" -> userController.handleLogout((LogoutRequest) r.payload);
                case "모든 예약 조회" -> userController.handleLogout((LogoutRequest) r.payload);
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        // 예약 관리 컨트롤러 이관
        else if (request instanceof ReservationManagementCommandRequest r) {
            return switch (r.command) {
                case "예약 삭제" -> userController.handleLogin((LoginRequest) r.payload);
                case "예약 수정" -> userController.handleSignup((SignupRequest) r.payload);
                case "예약 상태 변경" -> userController.handleLogout((LogoutRequest) r.payload);
                case "사용자 예약 조회" -> userController.handleLogout((LogoutRequest) r.payload);
                case "모든 예약 조회" -> userController.handleLogout((LogoutRequest) r.payload);
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        // 강의 컨트롤러 이관 - 완료
        else if (request instanceof LectureCommandRequest r) {
            return switch (r.command) {
                case "주간 강의 조회" -> lectureController.handleReturnLectureOfWeek((LectureRequest) r.payload);
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        return new BasicResponse("405", "지원하지 않는 요청 타입");
    }
}