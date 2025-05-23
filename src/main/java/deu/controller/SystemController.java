package deu.controller;

import deu.controller.business.*;
import deu.model.dto.request.command.*;
import deu.model.dto.request.data.lecture.LectureRequest;
import deu.model.dto.request.data.reservation.DeleteRoomReservationRequest;
import deu.model.dto.request.data.reservation.RoomReservationRequest;
import deu.model.dto.request.data.user.*;
import deu.model.dto.response.BasicResponse;
import deu.model.entity.RoomReservation;

public class SystemController {
    private final UserController userController = UserController.getInstance();
    private final UserManagementController userManagementController = UserManagementController.getInstance();
    private final LectureController lectureController = LectureController.getInstance();
    private final ReservationController reservationController = ReservationController.getInstance();
    private final ReservationManagementController reservationManagementController = ReservationManagementController.getInstance();

    public Object handle(Object request) {
        // 사용자 컨트롤러 이관 - 완료
        if (request instanceof UserCommandRequest r) {
            return switch (r.command) {
                case "로그인" -> userController.handleLogin((LoginRequest) r.payload);
                case "회원가입" -> userController.handleSignup((SignupRequest) r.payload);
                case "로그아웃" -> userController.handleLogout((LogoutRequest) r.payload);
                case "동시접속자" -> userController.handleCurrentUser();
                case "사용자 이름 반환" -> userController.handleFindUserName((FindUserNameRequest) r.payload);
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
        // 예약 컨트롤러 이관 - 완료
        else if (request instanceof ReservationCommandRequest r) {
            return switch (r.command) {
                case "예약 요청" -> reservationController.handleAddRoomReservation((RoomReservation) r.payload);
                case "예약 수정" -> reservationController.handleModifyRoomReservation((RoomReservation) r.payload);
                case "예약 삭제" -> reservationController.handlDeleteRoomReservation((DeleteRoomReservationRequest) r.payload);
                case "사용자 예약 조회" -> reservationController.handleWeekRoomReservationByUserNumber((String) r.payload);
                case "강의실 예약 조회" -> reservationController.handleWeekRoomReservationByLectureroom((RoomReservationRequest) r.payload);
                default -> new BasicResponse("404", "알 수 없는 명령어");
            };
        }
        // 예약 관리 컨트롤러 이관 - 완료
        else if (request instanceof ReservationManagementCommandRequest r) {
            return switch (r.command) {
                case "예약 수정" -> reservationManagementController.modifyRoomReservation((RoomReservation) r.payload);
                case "예약 삭제" -> reservationManagementController.deleteRoomReservation((String) r.payload);
                case "예약 대기 전체 조회" -> reservationManagementController.findAllRoomReservation();
                case "예약 상태 변경" -> reservationManagementController.changeRoomReservationStatus((String) r.payload);
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