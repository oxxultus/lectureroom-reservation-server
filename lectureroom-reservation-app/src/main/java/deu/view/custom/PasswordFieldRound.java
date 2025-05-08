package deu.view.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * 라운드 패스워드 필드 + 플레이스홀더 지원
 * 실제 저장 시에는 isShowingPlaceholder() == false 확인 후 getPassword() 사용
 * @author oxxultus
 */
public class PasswordFieldRound extends JPasswordField {

    private int round = 20;
    private String placeholder = "";
    private boolean showingPlaceholder = false;
    private char defaultEchoChar;
    private Color normalTextColor = Color.WHITE;

    public PasswordFieldRound() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        defaultEchoChar = getEchoChar();
        setForeground(normalTextColor);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setEchoChar(defaultEchoChar);
                    setForeground(normalTextColor);
                    setCaretColor(normalTextColor);
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setPlaceholderInternal();
                }
            }
        });
    }

    public void setRound(int round) {
        this.round = round;
        repaint();
    }

    public int getRound() {
        return round;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (!isFocusOwner() && getPassword().length == 0) {
            setPlaceholderInternal();
        }
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public boolean isShowingPlaceholder() {
        return showingPlaceholder;
    }

    public void setNormalTextColor(Color color) {
        this.normalTextColor = color;
        if (!showingPlaceholder) {
            setForeground(normalTextColor);
            setCaretColor(normalTextColor);
        }
    }

    private void setPlaceholderInternal() {
        setText(placeholder);
        setEchoChar((char) 0);
        setForeground(Color.GRAY);
        setCaretColor(new Color(0, 0, 0, 0));
        setCaretPosition(0);
        showingPlaceholder = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape roundShape = new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, round, round);
        g2.setClip(roundShape);

        Color bg = getBackground();
        if (bg != null && bg.getAlpha() > 0) {
            g2.setColor(bg);
            g2.fill(roundShape);
        }

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        Shape border = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, round, round);
        g2.draw(border);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), round, round);
        return shape.contains(x, y);
    }
}