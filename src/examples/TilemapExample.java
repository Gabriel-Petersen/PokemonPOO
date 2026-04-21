package examples;

import engine.assets.AssetManager;
import engine.core.GamePanel;
import engine.input.Input;
import engine.tilemap.ImageTilemap;
import engine.tilemap.Tilemap;
import game.Player;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class TilemapExample
{
    public static void runExample  ()
    {
        GamePanel gamePanel = GamePanel.getInstance();

        Tilemap tilemap = new DynamicDebuggableTilemap(
                Objects.requireNonNull(AssetManager.getSprite("scenario/mapa_limited.png")),
                AssetManager.getSprite("scenario/collision_matrix_map.png"),
                16
        );
        tilemap.setLayer(-1);
        tilemap.getTransform().setScale(3, 3);
        tilemap.getTransform().setPosition(
                -4 * tilemap.getSizeX(),
                -4 * tilemap.getSizeY()
        );

        gamePanel.addElement(tilemap);

        Player player = new Player();
        player.getTransform().setScale(1.8, 1.8);
        player.setCurrentMap(tilemap);
        gamePanel.addElement(player);
    }

    private static class DynamicDebuggableTilemap extends ImageTilemap
    {
        public DynamicDebuggableTilemap(BufferedImage visual, BufferedImage mask, double tileSize) {
            super(visual, mask, tileSize);
        }

        @Override
        public void update() {
            super.update();
            setDebugMode(
                    Input.getKey(KeyEvent.VK_L)
            );
        }
    }
}
