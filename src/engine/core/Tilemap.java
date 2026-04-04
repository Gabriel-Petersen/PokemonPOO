package engine.core;

import engine.math.vectors.MutableVec2d;
import engine.math.vectors.Vec2d;
import engine.rendering.Renderer;
import engine.rendering.Tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tilemap extends GameObject
{
    private final Tile[] tilemap;
    private final boolean[] collisionMatrix;
    private final int sizeX;
    private final int sizeY;
    private double tileSize;

    public Tilemap(int sizeX, int sizeY, double tileSize)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tileSize = tileSize;
        tilemap = new Tile[sizeX * sizeY];
        collisionMatrix = new boolean[sizeX * sizeY];
    }

    public double getTileSize() { return tileSize; }
    public Tile getTile(int x, int y) { return tilemap[x*sizeY + y]; }

    public void buildTile(TileIteration tileBuild)
    {
        for (int x = 0; x < sizeX; ++x) for (int y = 0; y < sizeY; ++y)
        {
            int base = x*sizeY + y;
            tilemap[base] = tileBuild.setTile(x, y);
            if (tilemap[base] == null) throw new NullPointerException("Returned 'null' tile from TileBuilding");
            tilemap[base].setTileSize(tileSize);
            collisionMatrix[base] = false;
        }
    }

    public void buildTile(TileIteration tileBuild, CollisionMatrixIteration colliderBuild)
    {
        for (int x = 0; x < sizeX; ++x) for (int y = 0; y < sizeY; ++y)
        {
            int base = x*sizeY + y;
            tilemap[base] = tileBuild.setTile(x, y);
            if (tilemap[base] == null) throw new NullPointerException("Returned 'null' tile from TileBuilding");
            tilemap[base].setTileSize(tileSize);
            collisionMatrix[base] = colliderBuild.isSolid(x, y);
        }
    }

    public boolean isSolidAt(Vec2d position) { return isSolidAt(position.x(), position.y()); }

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

    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }
    public Vec2d getSize() { return new MutableVec2d(sizeX, sizeY); }

    @FunctionalInterface
    public interface TileIteration {
        Tile setTile(int x, int y);
    }

    @FunctionalInterface
    public interface CollisionMatrixIteration {
        boolean isSolid(int x, int y);
    }

    @Override
    protected void onDraw(Graphics2D g2d)
    {
        var backup = g2d.getTransform();

        g2d.translate(transform.getPosition().x(), transform.getPosition().y());
        g2d.rotate(Math.toRadians(transform.rotation));

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++)
            {
                Tile tile = getTile(x, y);
                if (tile == null) continue;

                BufferedImage img = tile.getImage();

                double renderX = x * tile.getTileSizeX() * transform.getScale().x();
                double renderY = y * tile.getTileSizeY() * transform.getScale().y();

                double targetWidth = tile.getTileSizeX() * transform.getScale().x();
                double targetHeight = tile.getTileSizeY() * transform.getScale().y();

                g2d.drawImage(
                        img,
                        (int) renderX,
                        (int) renderY,
                        (int) targetWidth,
                        (int) targetHeight,
                        null
                );
            }
        }

        g2d.setTransform(backup);
    }

    public void setTileSize(double size)
    {
        tileSize = size;
        for (Tile t : tilemap) t.setTileSize(size);
    }

    @Override protected Renderer createSwingRenderer() { return null; }
    @Override public void setup() { }
    @Override public void update() { }
}
