package engine.tilemap;

import java.awt.*;

public class ManualTilemap extends Tilemap
{
    private final Tile[] tilemap;

    public ManualTilemap(int sizeX, int sizeY, double tileSize)
    {
        super(sizeX, sizeY, tileSize);
        this.tilemap = new Tile[sizeX * sizeY];
    }

    public Tile getTile(int x, int y) { return tilemap[x * sizeY + y]; }

    public void build(TileIteration tileBuild)
    {
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
            {
                int base = x * sizeY + y;
                tilemap[base] = tileBuild.setTile(x, y);
                if (tilemap[base] != null) tilemap[base].setTileSize(tileSize);
            }
    }

    public void build(TileIteration tileBuild, CollisionMatrixIteration colliderBuild)
    {
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
            {
                int base = x * sizeY + y;
                tilemap[base] = tileBuild.setTile(x, y);
                if (tilemap[base] != null) tilemap[base].setTileSize(tileSize);
                collisionMatrix[base] = colliderBuild.isSolid(x, y);
            }
    }

    @Override
    protected void drawTilemap(Graphics2D g2d)
    {
        var backup = g2d.getTransform();
        g2d.translate(transform.getPosition().x(), transform.getPosition().y());
        g2d.rotate(Math.toRadians(transform.rotation));

        double scaleX = transform.getScale().x();
        double scaleY = transform.getScale().y();

        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
            {
                Tile tile = getTile(x, y);
                if (tile == null) continue;

                double renderX = x * tileSize * scaleX;
                double renderY = y * tileSize * scaleY;
                double targetW = tileSize * scaleX;
                double targetH = tileSize * scaleY;

                g2d.drawImage(tile.getImage(), (int)renderX, (int)renderY, (int)targetW, (int)targetH, null);
            }

        g2d.setTransform(backup);
    }

    @FunctionalInterface
    public interface TileIteration { Tile setTile(int x, int y); }

    @FunctionalInterface
    public interface CollisionMatrixIteration { boolean isSolid(int x, int y); }
}