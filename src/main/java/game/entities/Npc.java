package game.entities;

import engine.assets.AssetManager;
import engine.core.GameObject;
import engine.math.vectors.Vec2d;
import engine.rendering.Renderer;
import engine.rendering.SpriteRenderer;
import game.player.Interactable;
import game.player.Player;
import game.ui.DialogueBox;
import java.awt.image.BufferedImage;

public class Npc extends GameObject implements Interactable 
{
    private final BufferedImage sprite;
    private String[] message;
    protected String name;
    private int pending = 0;

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

    @Override 
    public void update() 
    {
        if (pending > 0) 
        {
            if (pending == 1) { pending++; return; }
            DialogueBox.getInstance().getEventQueue().resolve();
            pending = 0;
        }
     }

    @Override
    public void onInteract(Player player) 
    {
        if (message == null) return;
        if (message[0] == null) return;
        
        System.out.println("Start talking");
        player.setTalking(true);
        var db = DialogueBox.getInstance();
        db.setVisible(true);
        db.getEventQueue().clear();

        for (String s : message) db.showText(s);

        db.getEventQueue().setOnEndResolving(() -> {
            player.setTalking(false);
            db.setVisible(false);
            System.out.println("Queue ended");
        });
        pending = 1;
    }

    public String getName() { return name; }
    public String[] getMessage() { return message; }
    public void setName(String name) { this.name = name; }
    public void setMessage(String[] message) { this.message = message; }
    @Override public Vec2d getPosition() { return transform.getPosition(); }
}
