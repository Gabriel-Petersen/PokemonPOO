package engine.rendering;

import engine.core.Transform;
import engine.assets.AssetManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class SpriteRenderer implements Renderer
{
    private BufferedImage image;

    public SpriteRenderer(String assetPath) {
        this.image = AssetManager.getSprite(assetPath);
    }

    @Override
    public void render(Graphics2D g2d, Transform transform) {
        g2d.drawImage(
                Objects.requireNonNull(image),
                (int) transform.getPosition().x(), (int) transform.getPosition().y(),
                (int) transform.getScale().x(), (int) transform.getScale().y(),
                null
        );
    }

    public void setImage(BufferedImage image) { this.image = image; }
    public BufferedImage getImage() { return image; }
}
