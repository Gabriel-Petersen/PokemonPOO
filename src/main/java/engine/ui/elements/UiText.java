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

    @Override
    protected void drawSelf(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString(text, 0, 0);
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