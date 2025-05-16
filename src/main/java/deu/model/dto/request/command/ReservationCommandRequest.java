package deu.model.dto.request.command;

import java.io.Serializable;

/**
 * [ReservationCommandRequest 클래스]
 * <p>
 * 이 클래스는 강의 관련 요청을 서버에 전달하기 위한 커맨드 기반 DTO입니다.
 * 커맨드(command) 필드에는 요청의 유형(예: "create", "delete", "update" 등)이 들어가고,
 * payload에는 해당 요청에 필요한 데이터를 객체 형태로 담습니다.
 * </p>
 *
 * <p>직렬화를 위해 Serializable을 구현하고 있습니다.</p>
 */
public class ReservationCommandRequest implements Serializable {
    public String command;
    public Object payload;

    public ReservationCommandRequest(String command, Object payload) {
        this.command = command;
        this.payload = payload;
    }
}