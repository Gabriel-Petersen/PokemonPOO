package engine.tilemap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTilemap extends Tilemap
{
    private static final int THRESHOLD = 64;
    private final BufferedImage visualMap;

    public ImageTilemap(BufferedImage visual, BufferedImage mask, double tileSize)
    {
        super((int)(visual.getWidth() / tileSize), (int)(visual.getHeight() / tileSize), tileSize);
        this.visualMap = visual;
        bakeCollision(mask);
    }

    private void bakeCollision(BufferedImage mask)
    {
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
            {
                int blackCount = 0;
                for (int px = 0; px < tileSize && blackCount < THRESHOLD; px++)
                    for (int py = 0; py < tileSize && blackCount < THRESHOLD; py++)
                    {
                        int ix = (int)(x * tileSize + px);
                        int iy = (int)(y * tileSize + py);
                        if (ix < mask.getWidth() && iy < mask.getHeight()) {
                            if ((mask.getRGB(ix, iy) & 0x00FFFFFF) == 0) blackCount++;
                        }
                    }

                collisionMatrix[x * sizeY + y] = (blackCount >= THRESHOLD);
            }
    }

    @Override
    protected void drawTilemap(Graphics2D g2d)
    {
        var backup = g2d.getTransform();
        g2d.translate(transform.getPosition().x(), transform.getPosition().y());
        g2d.rotate(Math.toRadians(transform.rotation));

        g2d.drawImage(visualMap, 0, 0,
                (int)(visualMap.getWidth() * transform.getScale().x()),
                (int)(visualMap.getHeight() * transform.getScale().y()), null);

        g2d.setTransform(backup);
    }
}