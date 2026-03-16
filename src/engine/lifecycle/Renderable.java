package engine.lifecycle;

import engine.core.EngineElement;
import engine.core.GamePanel;

import java.awt.*;

public abstract class Renderable implements Comparable<Renderable>, EngineElement
{
    private static short globalCounter = 0;
    private final short id;
    private byte layer;

    protected Renderable() { id = Renderable.globalCounter++; }

    public abstract void draw(Graphics2D g2d);

    public byte getLayer() { return layer; }
    public void setLayer(int layer) {
        GamePanel.getInstance().removeRenderable(this);
        this.layer = (byte)layer;
        GamePanel.getInstance().addRenderable(this);
    }

    @Override
    public int compareTo(Renderable o) {
        if (layer == o.layer)
            return Integer.compare(id, o.id);
        return Integer.compare(layer, o.layer);
    }
}