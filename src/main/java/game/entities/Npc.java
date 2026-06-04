package game.entities;

import engine.assets.AssetManager;
import engine.core.GameObject;
import engine.rendering.Renderer;
import engine.rendering.SpriteRenderer;
import game.player.Interactable;
import game.player.Player;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Npc extends GameObject implements Interactable 
{
    public static final Integer INTERACT_KEY = KeyEvent.VK_I;

    private final BufferedImage sprite;
    protected  String name;

    public Npc (String name, BufferedImage sprite) { 
        this.sprite = sprite;
        this.name = name;
    }

    public Npc (String name, String spritePath) { this(name, AssetManager.getSprite(spritePath)); }

    @Override
    protected Renderer createSwingRenderer() { return new SpriteRenderer(sprite); }

    @Override
    public void setup() {
        renderer = createSwingRenderer();
    }

    @Override public void update() { }

    @Override
    public void onInteract(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
