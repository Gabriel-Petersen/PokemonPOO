package engine.ui.elements;

import engine.assets.AssetManager;
import engine.ui.core.UiElement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UiImage extends UiElement
{
    private final BufferedImage image;
    private Color color;

    public UiImage(BufferedImage sprite) {
        image = sprite;
    }
    public UiImage(String spritePath) {
        image = AssetManager.getSprite(spritePath);
    }

    public UiImage(int sizeX, int sizeY, Color color)
    {
        image = null;
        transform.setScale(sizeX, sizeY);
        this.color = color;
    }

    @Override
    protected void drawSelf(Graphics2D g2d)
    {
        if (image == null)
        {
            g2d.setColor(color);
            g2d.fillRect(
                    0, 0,
                    (int) transform.getScale().x(),
                    (int) transform.getScale().y()
            );
        }
        else
        {
            g2d.drawImage(
                    image, 0, 0,
                    (int)transform.getScale().x(),
                    (int)transform.getScale().y(),
                    null
            );
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
