package game;

import engine.core.GameObject;
import engine.input.Input;
import engine.math.vectors.MutableVec2d;
import engine.rendering.Renderer;
import engine.rendering.SpriteRenderer;

import java.awt.event.KeyEvent;

public class Monster extends GameObject
{
    private final MutableVec2d speedVec = new MutableVec2d();

    @Override
    protected Renderer createSwingRenderer() {
        return new SpriteRenderer("monstro.png");
    }

    @Override
    public void setup() {
        renderer = createSwingRenderer();
        transform.setScale(1, 1);
    }

    @Override
    public void update()
    {
        speedVec.set(
                Input.getAxisRaw("Horizontal"),
                Input.getAxisRaw("Vertical")
        );

        float speed = Input.getKey(KeyEvent.VK_SHIFT) ? 6 : 3;
        if (speedVec.magnitudeSqrt() > 0)
        {
            transform.translate(speedVec.normalized().mul(speed));
            double scx = transform.getScale().x();
            if (speedVec.x > 0) scx = 1;
            else if (speedVec.x < 0) scx = -1;
            transform.setScale(scx, transform.getScale().y());
        }
    }
}
