package engine.animation;

import java.awt.image.BufferedImage;

public class Animation
{
    private final String name;
    private final BufferedImage[] frames;
    private int frameDurationMillis;
    private boolean loop;

    public Animation(String name, int fps, boolean loop, BufferedImage... frames)
    {
        this.name = name;
        this.frames = frames;
        this.frameDurationMillis = 1000 / fps;
        this.loop = loop;
    }

    public String getName() { return name; }
    public BufferedImage[] getFrames() { return frames; }

    public boolean isLoop() { return loop; }
    public void setLoop(boolean loop) { this.loop = loop; }

    public int getFrameDurationMillis() { return frameDurationMillis; }
    public void setFrameDurationMillis(int frameDurationMillis) { this.frameDurationMillis = frameDurationMillis; }
}