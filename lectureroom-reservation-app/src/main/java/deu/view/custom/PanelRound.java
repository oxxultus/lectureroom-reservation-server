package deu.view.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class PanelRound extends JPanel {

    private int roundTopLeft = 0;
    private int roundTopRight = 0;
    private int roundBottomLeft = 0;
    private int roundBottomRight = 0;

    Image backgroundImage = null;
    private Color borderColor = null;

    public PanelRound() {
        setOpaque(false);
    }

    // 🖼️ 이미지 설정 메서드
    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
        repaint();
    }

    public void setBackgroundImageIcon(ImageIcon icon) {
        this.backgroundImage = icon.getImage();
        repaint();
    }

    // 🎨 테두리 색상 설정
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    // 🔽 라운드 설정
    public int getRoundTopLeft() { return roundTopLeft; }
    public void setRoundTopLeft(int roundTopLeft) {
        this.roundTopLeft = roundTopLeft;
        repaint();
    }

    public int getRoundTopRight() { return roundTopRight; }
    public void setRoundTopRight(int roundTopRight) {
        this.roundTopRight = roundTopRight;
        repaint();
    }

    public int getRoundBottomLeft() { return roundBottomLeft; }
    public void setRoundBottomLeft(int roundBottomLeft) {
        this.roundBottomLeft = roundBottomLeft;
        repaint();
    }

    public int getRoundBottomRight() { return roundBottomRight; }
    public void setRoundBottomRight(int roundBottomRight) {
        this.roundBottomRight = roundBottomRight;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Area area = createClippedArea();
        g2.setClip(area);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(getBackground());
            g2.fill(area);
        }

        if (borderColor != null) {
            g2.setClip(null);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(area);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    // 🧩 라운드 조합 영역
    protected Area createClippedArea() {
        Area area = new Area(createRoundTopLeft());
        if (roundTopRight > 0) area.intersect(new Area(createRoundTopRight()));
        if (roundBottomLeft > 0) area.intersect(new Area(createRoundBottomLeft()));
        if (roundBottomRight > 0) area.intersect(new Area(createRoundBottomRight()));
        return area;
    }

    private Shape createRoundTopLeft() {
        int w = getWidth(), h = getHeight();
        int rx = Math.min(w, roundTopLeft);
        int ry = Math.min(h, roundTopLeft);
        Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        area.add(new Area(new Rectangle2D.Double(rx / 2.0, 0, w - rx / 2.0, h)));
        area.add(new Area(new Rectangle2D.Double(0, ry / 2.0, w, h - ry / 2.0)));
        return area;
    }

    private Shape createRoundTopRight() {
        int w = getWidth(), h = getHeight();
        int rx = Math.min(w, roundTopRight);
        int ry = Math.min(h, roundTopRight);
        Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        area.add(new Area(new Rectangle2D.Double(0, 0, w - rx / 2.0, h)));
        area.add(new Area(new Rectangle2D.Double(0, ry / 2.0, w, h - ry / 2.0)));
        return area;
    }

    private Shape createRoundBottomLeft() {
        int w = getWidth(), h = getHeight();
        int rx = Math.min(w, roundBottomLeft);
        int ry = Math.min(h, roundBottomLeft);
        Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        area.add(new Area(new Rectangle2D.Double(rx / 2.0, 0, w - rx / 2.0, h)));
        area.add(new Area(new Rectangle2D.Double(0, 0, w, h - ry / 2.0)));
        return area;
    }

    private Shape createRoundBottomRight() {
        int w = getWidth(), h = getHeight();
        int rx = Math.min(w, roundBottomRight);
        int ry = Math.min(h, roundBottomRight);
        Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        area.add(new Area(new Rectangle2D.Double(0, 0, w - rx / 2.0, h)));
        area.add(new Area(new Rectangle2D.Double(0, 0, w, h - ry / 2.0)));
        return area;
    }
}