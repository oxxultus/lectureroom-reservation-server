package deu;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("================================================================================");
            System.out.println("");
            System.out.println("    서버 실행 중... 포트 번호: 9999");
            System.out.println("");
            System.out.println("================================================================================");

            while (true) {
                Socket client = serverSocket.accept();
                new Thread(new ClientHandler(client)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
