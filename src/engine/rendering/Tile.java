package engine.rendering;

import engine.math.vectors.MutableVec2d;
import engine.math.vectors.Vec2d;

import java.awt.image.BufferedImage;

public class Tile
{
    private BufferedImage image;
    private double tileSizeX = 1;
    private double tileSizeY = 1;

    public Tile(BufferedImage source) {
        image = source;
    }

    public boolean isSquare() { return tileSizeX == tileSizeY; }

    public BufferedImage getImage() { return image; }
    public double getTileSizeX() { return tileSizeX; }
    public double getTileSizeY() { return tileSizeY; }
    public Vec2d getTileSize() { return new MutableVec2d(tileSizeX, tileSizeY); }

    public void setImage(BufferedImage image) { this.image = image; }
    public void setTileSizeX(double tileSizeX) { this.tileSizeX = tileSizeX; }
    public void setTileSizeY(double tileSizeY) { this.tileSizeY = tileSizeY; }
    public void setTileSize(double size) {
        tileSizeY = tileSizeX = size;
    }
    public void setTileSize(double x, double y) {
        tileSizeX = x;
        tileSizeY = y;
    }
    public void setTileSize(Vec2d tileSize) {
        tileSizeX = tileSize.x();
        tileSizeY = tileSize.y();
    }
}
