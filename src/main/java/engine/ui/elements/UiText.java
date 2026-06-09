package engine.ui.elements;

import engine.ui.core.UiElement;
import java.awt.*;

public class UiText extends UiElement {
    private String text;
    private Color color = Color.BLACK;
    private Font font = new Font("Arial", Font.PLAIN, 18);

    public UiText(String text) {
        this.text = text;
    }

    public UiText() { }

    @Override
    protected void beforeDraw(Graphics2D g2d) {
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics(font);
        int width = Math.max(1, metrics.stringWidth(text));
        int height = Math.max(1, metrics.getHeight());
        getTransform().setScale(width, height);
    }

    @Override
    protected void drawSelf(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics(font);
        g2d.drawString(text, 0, metrics.getAscent());
    }

    public void setFont(Font font) {
        this.font = font;
    }
    public void setFont(String fontName, int style, int size) {
        font = new Font(fontName, style, size);
    }
    public void setText(String text) { this.text = text; }
    public void setColor(Color color) { this.color = color; }
}