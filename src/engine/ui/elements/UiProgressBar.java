package engine.ui.elements;

import engine.ui.core.UiElement;

import java.awt.*;

public class UiProgressBar extends UiElement
{
    public enum Direction { LEFT2RIGHT, RIGHT2LEFT, UP2DOWN, DOWN2UP }

    private final Direction direction;
    private Color backgroundColor;
    private Color fillColor;
    private double progress;

    public UiProgressBar(Direction direction) {
        this.direction = direction;
    }

    @Override
    protected void drawSelf(Graphics2D g2d)
    {
        int posX = (int) transform.getPosition().x();
        int posY = (int) transform.getPosition().y();
        int totalW = (int) transform.getScale().x();
        int totalH = (int) transform.getScale().y();

        g2d.setColor(backgroundColor);
        g2d.fillRect(posX, posY, totalW, totalH);

        int fillX = posX;
        int fillY = posY;
        int fillW = totalW;
        int fillH = totalH;

        double clampedProgress = Math.max(0.0, Math.min(1.0, progress));
        if (clampedProgress <= 0) return;

        switch (direction)
        {
            case LEFT2RIGHT:
                fillW = (int) (totalW * clampedProgress);
                break;

            case RIGHT2LEFT:
                fillW = (int) (totalW * clampedProgress);
                fillX = posX + (totalW - fillW);
                break;

            case UP2DOWN:
                fillH = (int) (totalH * clampedProgress);
                break;

            case DOWN2UP:
                fillH = (int) (totalH * clampedProgress);
                fillY = posY + (totalH - fillH);
                break;
        }

        g2d.setColor(fillColor);
        g2d.fillRect(fillX, fillY, fillW, fillH);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(posX, posY, totalW, totalH);
    }

    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress; }

    public Direction getDirection() { return direction; }

    public void setFillColor(Color fillColor) { this.fillColor = fillColor; }
    public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }
}
