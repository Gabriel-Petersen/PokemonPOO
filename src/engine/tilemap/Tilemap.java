package engine.tilemap;

import engine.core.GameObject;
import engine.rendering.Renderer;

public abstract class Tilemap extends GameObject
{
    protected final boolean[] collisionMatrix;
    protected final int sizeX;
    protected final int sizeY;
    protected double tileSize;

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

    public double getTileSize() { return tileSize; }
    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }

    @Override protected Renderer createSwingRenderer() { return null; }
    @Override public void setup() { }
    @Override public void update() { }
}