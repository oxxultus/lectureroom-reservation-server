package deu.view.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class LabelRound extends JLabel {

    private int roundTopLeft = 0;
    private int roundTopRight = 0;
    private int roundBottomLeft = 0;
    private int roundBottomRight = 0;

    private Image image = null;
    private int imageWidth = -1;
    private int imageHeight = -1;

    public LabelRound() {
        setOpaque(false);
    }

    // --- Round Setter / Getter ---
    public void setRoundTopLeft(int roundTopLeft) {
        this.roundTopLeft = roundTopLeft;
        repaint();
    }

    public void setRoundTopRight(int roundTopRight) {
        this.roundTopRight = roundTopRight;
        repaint();
    }

    public void setRoundBottomLeft(int roundBottomLeft) {
        this.roundBottomLeft = roundBottomLeft;
        repaint();
    }

    public void setRoundBottomRight(int roundBottomRight) {
        this.roundBottomRight = roundBottomRight;
        repaint();
    }

    public int getRoundTopLeft() { return roundTopLeft; }
    public int getRoundTopRight() { return roundTopRight; }
    public int getRoundBottomLeft() { return roundBottomLeft; }
    public int getRoundBottomRight() { return roundBottomRight; }

    // --- Image Setter ---
    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setImageIcon(ImageIcon icon) {
        this.image = icon.getImage();
        repaint();
    }

    public void setImageSize(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Area clipArea = createClippedArea();
        g2.setClip(clipArea);

        // 배경 그리기
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 이미지 그리기
        if (image != null) {
            int imgW = (imageWidth > 0) ? imageWidth : getWidth();
            int imgH = (imageHeight > 0) ? imageHeight : getHeight();
            g2.drawImage(image, 0, 0, imgW, imgH, this);
        }

        // 텍스트 렌더링
        super.paintComponent(g2);
        g2.dispose();
    }

    // --- 라운드 영역 만들기 ---
    private Area createClippedArea() {
        Area area = new Area(createRoundTopLeft());

        if (roundTopRight > 0) {
            area.intersect(new Area(createRoundTopRight()));
        }
        if (roundBottomLeft > 0) {
            area.intersect(new Area(createRoundBottomLeft()));
        }
        if (roundBottomRight > 0) {
            area.intersect(new Area(createRoundBottomRight()));
        }

        return area;
    }

    private Shape createRoundTopLeft() {
        int w = getWidth();
        int h = getHeight();
        int r = roundTopLeft;
        int rx = Math.min(w, r);
        int ry = Math.min(h, r);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        area.add(new Area(new Rectangle2D.Double(rx / 2.0, 0, w - rx / 2.0, h)));
        area.add(new Area(new Rectangle2D.Double(0, ry / 2.0, w, h - ry / 2.0)));
        return area;
    }

    private Shape createRoundTopRight() {
        int w = getWidth();
        int h = getHeight();
        int r = roundTopRight;
        int rx = Math.min(w, r);
        int ry = Math.min(h, r);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        area.add(new Area(new Rectangle2D.Double(0, 0, w - rx / 2.0, h)));
        area.add(new Area(new Rectangle2D.Double(0, ry / 2.0, w, h - ry / 2.0)));
        return area;
    }

    private Shape createRoundBottomLeft() {
        int w = getWidth();
        int h = getHeight();
        int r = roundBottomLeft;
        int rx = Math.min(w, r);
        int ry = Math.min(h, r);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        area.add(new Area(new Rectangle2D.Double(rx / 2.0, 0, w - rx / 2.0, h)));
        area.add(new Area(new Rectangle2D.Double(0, 0, w, h - ry / 2.0)));
        return area;
    }

    private Shape createRoundBottomRight() {
        int w = getWidth();
        int h = getHeight();
        int r = roundBottomRight;
        int rx = Math.min(w, r);
        int ry = Math.min(h, r);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, rx, ry));
        area.add(new Area(new Rectangle2D.Double(0, 0, w - rx / 2.0, h)));
        area.add(new Area(new Rectangle2D.Double(0, 0, w, h - ry / 2.0)));
        return area;
    }
}