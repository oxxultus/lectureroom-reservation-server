package deu.controller.ui;

import deu.controller.UserClientController;
import deu.model.dto.response.BasicResponse;
import deu.view.Auth;
import deu.view.Home;
import deu.view.Reservation;
import deu.view.ReservationManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HomeSwingController {

    private final Home view;

    public HomeSwingController(Home view) {
        this.view = view;

        // 이벤트 연결
        view.addLogoutListener(this::handleLogout);
        view.addReservationMenuListener(this::showReservationPanel);
        view.addMyReservationMenuListener(this::showMainPanel);
        view.addManagementMenuListener(this::showManagerMenu);
        view.addUserManagementListener(this::showUserManagerManagement);
        view.addReservationManagementListener(this::showReservationManagement);
        view.addCommonMenuListener(this::showCommonMenu);
    }

    private void handleLogout(ActionEvent e) {
        Auth frame = (Auth) SwingUtilities.getWindowAncestor(view);
        BasicResponse result = new UserClientController().logout(view.getUserNumber(), view.getUserPassword());

        if (result.code.equals("200") && frame != null) {
            CardLayout layout = (CardLayout) frame.getContentPane().getLayout();
            layout.show(frame.getContentPane(), "login");

            // home 제거
            for (Component comp : frame.getContentPane().getComponents()) {
                if ("home".equals(comp.getName())) {
                    frame.getContentPane().remove(comp);
                    break;
                }
            }
            frame.revalidate();
            frame.repaint();
        } else {
            JOptionPane.showMessageDialog(view, result.message, "로그아웃 실패", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showReservationPanel(ActionEvent e) {
        Reservation reservation = new Reservation(view.getUserNumber(), view.getUserPassword());
        new ReservationSwingController(reservation);
        view.replaceMainContent(view.getMenuPanel(), reservation);
    }

    private void showMainPanel(ActionEvent e) {
        view.replaceMainContent(view.getMenuPanel(), view.getMainPanel());
    }

    private void showManagerMenu(ActionEvent e) {
        ReservationManagement panel = new ReservationManagement();

        new ReservationManagementSwingController(panel);

        view.replaceMainContent(view.getManagerMenuPanel(), panel);
    }

    private void showUserManagerManagement(ActionEvent e) {
        view.replaceMainContent(view.getManagerMenuPanel(), null);
    }

    private void showReservationManagement(ActionEvent e) {
        ReservationManagement panel = new ReservationManagement();

        new ReservationManagementSwingController(panel);

        view.replaceMainContent(view.getManagerMenuPanel(), panel);
    }

    private void showCommonMenu(ActionEvent e) {
        view.replaceMainContent(view.getMenuPanel(), view.getMainPanel());
    }
}