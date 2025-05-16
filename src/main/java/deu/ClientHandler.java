package deu;

import deu.controller.SystemController;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            Object request = in.readObject(); // 클라이언트로 부터 요청 수신
            Object response = new SystemController().handle(request); // 요청 처리
            out.writeObject(response); // 응답 전송
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}