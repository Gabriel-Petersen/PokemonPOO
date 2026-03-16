package engine.primitives;

import engine.core.GameObject;
import engine.rendering.ShapeFactory;
import engine.rendering.Renderer;

import java.awt.*;

public class Square extends GameObject
{
    private final int size;
    private final Color color;

    public Square(int x, int y, int size, Color color, int layer)
    {
        transform.setPosition(x, y);
        this.size = size;
        this.color = color;
        setLayer(layer);
        renderer = createSwingRenderer();
    }

    @Override
    protected Renderer createSwingRenderer() {
        return ShapeFactory.createRect(size, size, color);
    }

    @Override
    public void setup() { }

    @Override
    public void update() { }
}
