package game;

import engine.core.GameObject;
import engine.input.Input;
import engine.rendering.Renderer;
import engine.rendering.SpriteRenderer;

public class Monster extends GameObject
{
    @Override
    protected Renderer createSwingRenderer() {
        return new SpriteRenderer("monstro.png");
    }

    @Override
    public void setup() {
        renderer = createSwingRenderer();
    }

    @Override
    public void update() {
        int dx = Input.getAxisRaw("Horizontal");
        int dy = Input.getAxisRaw("Vertical");

        transform.translate(dx, dy);
    }
}
