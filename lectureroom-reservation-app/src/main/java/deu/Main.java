package deu;

import deu.controller.ui.AuthSwingController;
import deu.view.Auth;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Auth authView = new Auth(); // authView 생성
            new AuthSwingController(authView); // Controller가 View에 이벤트 리스너 연결
            authView.setVisible(true); // 창 띄우기
        });
    }
}