package engine.rendering;

import java.awt.Color;

public class ShapeFactory
{
    public static Renderer createRect(int width, int height, Color color)
    {
        return (g2d, t) -> {
            g2d.setColor(color);
            g2d.fillRect((int) t.getPosition().x(), (int) t.getPosition().y(), width, height);
        };
    }

    public static Renderer createCircle(int radius, Color color)
    {
        return (g2d, t) -> {
            g2d.setColor(color);
            g2d.fillOval((int) t.getPosition().x(), (int) t.getPosition().y(), radius * 2, radius * 2);
        };
    }
}