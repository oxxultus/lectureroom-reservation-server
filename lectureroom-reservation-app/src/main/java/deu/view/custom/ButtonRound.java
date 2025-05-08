package deu.view.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * 개별 꼭짓점 라운딩 버튼 + 배경 + 테두리 색상 커스터마이징
 * @author oxxultus
 */
public class ButtonRound extends JButton {

    private int roundTopLeft = 20;
    private int roundTopRight = 20;
    private int roundBottomLeft = 20;
    private int roundBottomRight = 20;
    private Color borderColor = Color.WHITE;

    public ButtonRound() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    // 🔹 라운드 Setter
    public void setRoundTopLeft(int r) { this.roundTopLeft = r; repaint(); }
    public void setRoundTopRight(int r) { this.roundTopRight = r; repaint(); }
    public void setRoundBottomLeft(int r) { this.roundBottomLeft = r; repaint(); }
    public void setRoundBottomRight(int r) { this.roundBottomRight = r; repaint(); }

    public int getRoundTopLeft() { return roundTopLeft; }
    public int getRoundTopRight() { return roundTopRight; }
    public int getRoundBottomLeft() { return roundBottomLeft; }
    public int getRoundBottomRight() { return roundBottomRight; }

    // 🔹 테두리 색상
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    // 🔸 라운딩 영역 생성
    private Shape createRoundedShape() {
        int w = getWidth(), h = getHeight();
        Area area = new Area(new Rectangle2D.Double(0, 0, w, h));

        if (roundTopLeft > 0) area.intersect(new Area(createRoundTopLeft(w, h)));
        if (roundTopRight > 0) area.intersect(new Area(createRoundTopRight(w, h)));
        if (roundBottomLeft > 0) area.intersect(new Area(createRoundBottomLeft(w, h)));
        if (roundBottomRight > 0) area.intersect(new Area(createRoundBottomRight(w, h)));

        return area;
    }

    // 개별 꼭짓점 도형 생성
    private Shape createRoundTopLeft(int w, int h) {
        int rx = Math.min(w, roundTopLeft), ry = Math.min(h, roundTopLeft);
        Area a = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        a.add(new Area(new Rectangle2D.Double(rx / 2.0, 0, w - rx / 2.0, h)));
        a.add(new Area(new Rectangle2D.Double(0, ry / 2.0, w, h - ry / 2.0)));
        return a;
    }

    private Shape createRoundTopRight(int w, int h) {
        int rx = Math.min(w, roundTopRight), ry = Math.min(h, roundTopRight);
        Area a = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        a.add(new Area(new Rectangle2D.Double(0, 0, w - rx / 2.0, h)));
        a.add(new Area(new Rectangle2D.Double(0, ry / 2.0, w, h - ry / 2.0)));
        return a;
    }

    private Shape createRoundBottomLeft(int w, int h) {
        int rx = Math.min(w, roundBottomLeft), ry = Math.min(h, roundBottomLeft);
        Area a = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        a.add(new Area(new Rectangle2D.Double(rx / 2.0, 0, w - rx / 2.0, h)));
        a.add(new Area(new Rectangle2D.Double(0, 0, w, h - ry / 2.0)));
        return a;
    }

    private Shape createRoundBottomRight(int w, int h) {
        int rx = Math.min(w, roundBottomRight), ry = Math.min(h, roundBottomRight);
        Area a = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        a.add(new Area(new Rectangle2D.Double(0, 0, w - rx / 2.0, h)));
        a.add(new Area(new Rectangle2D.Double(0, 0, w, h - ry / 2.0)));
        return a;
    }

    // 🔸 배경 그리기
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape shape = createRoundedShape();

        if (getModel().isArmed()) {
            g2.setColor(getBackground().darker());
        } else {
            g2.setColor(getBackground());
        }

        g2.fill(shape);
        g2.dispose();
        super.paintComponent(g);
    }

    // 🔸 테두리 그리기
    @Override
    protected void paintBorder(Graphics g) {
        if (borderColor != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.draw(createRoundedShape());
            g2.dispose();
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return createRoundedShape().contains(x, y);
    }
}