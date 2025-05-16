package deu.controller;

import deu.model.dto.request.command.*;
import deu.model.dto.request.data.LoginRequest;
import deu.model.dto.request.data.LogoutRequest;
import deu.model.dto.request.data.SignupRequest;
import deu.model.dto.response.BasicResponse;

public class SystemController {
    private final UserController userController = UserController.getInstance();

    public Object handle(Object request) {
        // 사용자 컨트롤러 이관
        if (request instanceof UserCommandRequest r) {
            return switch (r.command) {
                case "로그인" -> userController.handleLogin((LoginRequest) r.payload);
                case "회원가입" -> userController.handleSignup((SignupRequest) r.payload);
                case "로그아웃" -> userController.handleLogout((LogoutRequest) r.payload);
                case "동시접속자" -> userController.handleCurrentUser();
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        // 사용자 관리 컨트롤러 이관
        else if (request instanceof UserManagementCommandRequest r) {
            return switch (r.command) {
                case "사용자 수정" -> userController.handleLogin((LoginRequest) r.payload);
                case "사용자 삭제" -> userController.handleSignup((SignupRequest) r.payload);
                case "사용자 조회" -> userController.handleLogout((LogoutRequest) r.payload);
                case "전체 사용자 조회" -> userController.handleLogout((LogoutRequest) r.payload);
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
        // 강의 컨트롤러 이관
        else if (request instanceof LectureCommandRequest r) {
            return switch (r.command) {
                case "강의 조회" -> userController.handleLogin((LoginRequest) r.payload);
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        return new BasicResponse("405", "지원하지 않는 요청 타입");
    }
}