package engine.core;

import engine.lifecycle.Renderable;
import engine.lifecycle.Updatable;
import engine.rendering.Renderer;

import java.awt.*;

public abstract class GameObject extends Renderable implements Updatable
{
    protected Transform transform = new Transform();
    protected Renderer renderer;

    public Transform getTransform() { return transform; }
    public Renderer getRenderer() { return renderer; }

    protected abstract Renderer createSwingRenderer();

    @Override
    public final void draw(Graphics2D g2d)
    {
        if (!isVisible) return;
        if (renderer != null)
            renderer.render(g2d, transform);
        else
            onDraw(g2d);
    }

    protected void onDraw(Graphics2D g2d) { }
}
