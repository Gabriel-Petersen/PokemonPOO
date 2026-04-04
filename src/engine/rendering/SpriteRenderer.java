package engine.rendering;

import engine.core.Transform;
import engine.assets.AssetManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteRenderer implements Renderer
{
    private BufferedImage image;

    public SpriteRenderer(String assetPath) {
        this.image = AssetManager.getSprite(assetPath);
    }

    @Override
    public void render(Graphics2D g2d, Transform transform)
    {
        var backup = g2d.getTransform();

        g2d.translate(transform.getPosition().x(), transform.getPosition().y());
        g2d.rotate(Math.toRadians(transform.rotation));
        g2d.scale(transform.getScale().x(), transform.getScale().y());

        g2d.drawImage(image,
                -image.getWidth()/2,
                -image.getHeight()/2,
                null);

        g2d.setTransform(backup);
    }

    public void setImage(BufferedImage image) { this.image = image; }
    public BufferedImage getImage() { return image; }
}
