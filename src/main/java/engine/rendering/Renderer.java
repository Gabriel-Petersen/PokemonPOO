package engine.rendering;

import engine.core.Transform;

import java.awt.Graphics2D;

public interface Renderer {
    void render(Graphics2D g2d, Transform transform);
}
