package engine.assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssetManager
{
    private static final Map<String, BufferedImage> sprites = new HashMap<>();

    public static BufferedImage getSprite(String path)
    {
        if (!sprites.containsKey(path))
        {
            try {
                BufferedImage img = ImageIO.read(Objects.requireNonNull(AssetManager.class.getResourceAsStream("/assets/" + path)));
                sprites.put(path, img);
            }
            catch (IOException e) {
                System.err.println("Erro de IO ao carregar o asset: " + path);
                return null;
            }
            catch (Exception e) {
                System.err.println("Erro ao carregar o asset: " + path + "\nO erro é: " + e);
                return null;
            }
        }
        return sprites.get(path);
    }
}
