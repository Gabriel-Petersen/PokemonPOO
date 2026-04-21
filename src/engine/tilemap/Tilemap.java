package engine.tilemap;

import engine.core.GameObject;
import engine.rendering.Renderer;

import java.awt.*;

public abstract class Tilemap extends GameObject
{
    protected final boolean[] collisionMatrix;
    protected final int sizeX;
    protected final int sizeY;
    protected double tileSize;

    protected boolean debugMode = false;
    private static final Color DEBUG_COLOR = new Color(255, 0, 0, 113);

    public Tilemap(int sizeX, int sizeY, double tileSize)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tileSize = tileSize;
        this.collisionMatrix = new boolean[sizeX * sizeY];
    }

    public boolean isSolidAt(double worldX, double worldY)
    {
        double localX = worldX - transform.getPosition().x();
        double localY = worldY - transform.getPosition().y();

        int tileX = (int) (localX / (tileSize * transform.getScale().x()));
        int tileY = (int) (localY / (tileSize * transform.getScale().y()));

        if (tileX < 0 || tileX >= sizeX || tileY < 0 || tileY >= sizeY)
            return true;

        return collisionMatrix[tileX * sizeY + tileY];
    }

    @Override
    public final void onDraw(Graphics2D g2d)
    {
        drawTilemap(g2d);
        if (debugMode)
            drawDebug(g2d);
    }

    private void drawDebug(Graphics2D g2d)
    {
        var backup = g2d.getTransform();
        g2d.translate(transform.getPosition().x(), transform.getPosition().y());

        double sX = transform.getScale().x();
        double renderTileSize = tileSize * sX;

        g2d.setColor(DEBUG_COLOR);
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
            {
                if (collisionMatrix[x * sizeY + y])
                {
                    g2d.fillRect(
                            (int)(x * renderTileSize),
                            (int)(y * renderTileSize),
                            (int)renderTileSize,
                            (int)renderTileSize
                    );
                }
            }

        g2d.setTransform(backup);
    }

    protected abstract void drawTilemap(Graphics2D g2d);

    public double getTileSize() { return tileSize; }
    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }
    public void setDebugMode(boolean debug) { this.debugMode = debug; }

    @Override protected Renderer createSwingRenderer() { return null; }
    @Override public void setup() { }
    @Override public void update() { }
}