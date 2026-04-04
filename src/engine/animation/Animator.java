package engine.animation;

import engine.assets.AssetManager;
import engine.core.GamePanel;
import engine.lifecycle.Updatable;
import engine.rendering.SpriteRenderer;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Animator
{
    private final Map<String, Animation> animations = new HashMap<>();
    private Animation currentAnim;
    private int currentFrameIndex;
    private double elapsed;
    private final SpriteRenderer targetRenderer;

    public Animator(SpriteRenderer renderer) { this.targetRenderer = renderer; }
    public Animation getCurrentAnim() { return currentAnim; }

    public void addAnimation(Animation anim)
    {
        if (animations.put(anim.getName(), anim) != null)
            System.err.println("Animação de nome " + anim.getName() + " já existe. Sobrepondo animação antiga");
        if (currentAnim == null)
            play(anim.getName());
    }

    public void addAnimation(String name, int fps, boolean loop, BufferedImage... frames) {
        this.addAnimation(new Animation(name, fps, loop, frames));
    }

    public void addAnimation(String name, int fps, boolean loop, String folderPath)
    {
        var frames = AssetManager.getSpritesFromFolder(folderPath);
        if (frames.length > 0)
            this.addAnimation(new Animation(name, fps, loop, frames));
        else
            System.err.println("Aviso: Pasta " + folderPath + " está vazia ou não existe.");
    }

    public void play(String name)
    {
        if (currentAnim != null && currentAnim.getName().equals(name)) return;
        currentAnim = animations.get(name);
        currentFrameIndex = 0;
        updateRenderer();
    }

    public void update()
    {
        if (currentAnim == null) return;
        elapsed += GamePanel.getInstance().getDeltaTime();
        double duration = currentAnim.getFrameDurationMillis() / 1000.0;

        if (elapsed >= duration) {
            advanceFrame();
            elapsed -= duration;
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

    public int getCurrentAnimSize() { return currentAnim.getFrames().length; }
    public int getCurrentFrameIndex() { return currentFrameIndex; }
    private void updateRenderer() {
        targetRenderer.setImage(currentAnim.getFrames()[currentFrameIndex]);
    }
}