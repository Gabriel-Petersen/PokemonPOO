package engine.animation;

import engine.lifecycle.Updatable;
import engine.rendering.SpriteRenderer;

import java.util.HashMap;
import java.util.Map;

public class Animator implements Updatable
{
    private final Map<String, Animation> animations = new HashMap<>();
    private Animation currentAnim;
    private int currentFrameIndex;
    private long timer;
    private final SpriteRenderer targetRenderer;

    public Animator(SpriteRenderer renderer) { this.targetRenderer = renderer; }

    public void addAnimation(Animation anim)
    {
        if (animations.put(anim.getName(), anim) != null)
            System.err.println("Animação de nome " + anim.getName() + " já existe. Sobrepondo animação antiga");
        if (currentAnim == null)
            play(anim.getName());
    }

    public void play(String name)
    {
        if (currentAnim != null && currentAnim.getName().equals(name)) return;
        currentAnim = animations.get(name);
        currentFrameIndex = 0;
        timer = System.currentTimeMillis();
        updateRenderer();
    }

    @Override
    public void update()
    {
        if (currentAnim == null) return;

        if (System.currentTimeMillis() - timer >= currentAnim.getFrameDurationMillis())
        {
            advanceFrame();
            timer = System.currentTimeMillis();
        }
    }

    private void advanceFrame()
    {
        currentFrameIndex++;
        if (currentFrameIndex >= currentAnim.getFrames().length)
        {
            if (currentAnim.isLoop()) currentFrameIndex = 0;
            else currentFrameIndex = currentAnim.getFrames().length - 1;
        }
        updateRenderer();
    }

    private void updateRenderer() {
        targetRenderer.setImage(currentAnim.getFrames()[currentFrameIndex]);
    }

    @Override public void setup() {}
}