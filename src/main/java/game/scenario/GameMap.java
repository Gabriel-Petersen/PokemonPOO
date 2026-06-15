package game.scenario;

import engine.tilemap.ImageTilemap;

import java.awt.image.BufferedImage;

public class GameMap extends ImageTilemap
{
    private final boolean[] tallGrassMatrix;

    private static final int GRASS_THRESHOLD = 64;

    public GameMap(BufferedImage visual, BufferedImage collisionMask, BufferedImage triggerMask, double tileSize)
    {
        super(visual, collisionMask, tileSize);
        this.tallGrassMatrix = new boolean[sizeX * sizeY];
        bakeTallGrass(triggerMask);
    }

    private void bakeTallGrass(BufferedImage triggerMask)
    {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++)
            {
                int redCount = 0;

                for (int px = 0; px < tileSize && redCount < GRASS_THRESHOLD; px++) {
                    for (int py = 0; py < tileSize && redCount < GRASS_THRESHOLD; py++)
                    {
                        int ix = (int) (x * tileSize + px);
                        int iy = (int) (y * tileSize + py);

                        if (ix < triggerMask.getWidth() && iy < triggerMask.getHeight())
                        {
                            int rgb = triggerMask.getRGB(ix, iy);

                            int r = (rgb >> 16) & 0xFF;
                            int g = (rgb >> 8) & 0xFF;
                            int b = rgb & 0xFF;

                            if (r > 200 && g < 50 && b < 50) {
                                redCount++;
                            }
                        }
                    }
                }

                tallGrassMatrix[x * sizeY + y] = (redCount >= GRASS_THRESHOLD);
            }
        }
    }

    public boolean isTallGrassAt(double worldX, double worldY)
    {
        int[] tileCords = worldToTileCords(worldX, worldY);
        int tileX = tileCords[0];
        int tileY = tileCords[1];

        if (tileX < 0 || tileX >= sizeX || tileY < 0 || tileY >= sizeY) {
            return false;
        }

        return tallGrassMatrix[tileX * sizeY + tileY];
    }
}
