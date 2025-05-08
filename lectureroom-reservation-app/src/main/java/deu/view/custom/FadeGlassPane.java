package deu.view.custom;

import javax.swing.*;
import java.awt.*;

public class FadeGlassPane extends JComponent {
    private float alpha = 0f;
    private Timer timer;
    private boolean fadeIn;

    public FadeGlassPane() {
        setOpaque(false);
    }

    public void startFade(boolean fadeIn, int durationMillis, Runnable onComplete) {
        this.fadeIn = fadeIn;
        int frames = 20;
        int delay = durationMillis / frames;
        float delta = 1f / frames;

        alpha = fadeIn ? 0f : 1f;
        setVisible(true);

        timer = new Timer(delay, e -> {
            alpha += fadeIn ? delta : -delta;
            alpha = Math.max(0f, Math.min(1f, alpha));
            repaint();

            if ((fadeIn && alpha >= 1f) || (!fadeIn && alpha <= 0f)) {
                timer.stop();
                if (!fadeIn) setVisible(false);
                if (onComplete != null) onComplete.run();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (alpha > 0f) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g2.setColor(getBackground() != null ? getBackground() : Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}