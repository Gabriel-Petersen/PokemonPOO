package engine.assets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;

public class AssetManager
{
    private static final Map<String, BufferedImage> sprites = new HashMap<>();

    public static BufferedImage getSprite(String path)
    {
        if (!sprites.containsKey(path))
        {
            try {
                InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/" + path);

                BufferedImage img = ImageIO.read(Objects.requireNonNull(stream));
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

    public static BufferedImage[] getSpritesFromFolder(String folderPath) {
        try {
            URL url = AssetManager.class.getResource("/assets/" + folderPath);
            if (url == null) throw new IOException("Diretório não encontrado: " + folderPath);

            File directory = new File(url.toURI());
            File[] files = directory.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg")
            );

            if (files == null) return new BufferedImage[0];

            Arrays.sort(files, Comparator.comparing(File::getName));

            BufferedImage[] spriteArray = new BufferedImage[files.length];
            for (int i = 0; i < files.length; i++) {
                spriteArray[i] = getSprite(folderPath + "/" + files[i].getName());
            }

            return spriteArray;
        }
        catch (IOException | URISyntaxException e) {
            System.err.println("Erro ao listar pasta de sprites: " + e.getMessage());
            return new BufferedImage[0];
        }
    }
}
