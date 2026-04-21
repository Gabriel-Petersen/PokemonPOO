package game;

import java.awt.*;
import java.awt.event.KeyEvent;

import engine.animation.Animator;
import engine.assets.AssetManager;
import engine.core.GameObject;
import engine.core.GamePanel;
import engine.input.Input;
import engine.math.vectors.MutableVec2d;
import engine.math.vectors.Vec2d;
import engine.primitives.Square;
import engine.rendering.Renderer;
import engine.rendering.SpriteRenderer;
import engine.tilemap.Tilemap;

public class Player extends GameObject 
{
    protected enum LastLookDir {
        UP('U'), DOWN('D'), LEFT('L'), RIGHT('R');

        private final char c;
        LastLookDir(char c) { this.c = c; }
        public char c() { return c; }
    }

    private Tilemap currentMap;
    private LastLookDir lastLookDir = LastLookDir.DOWN;
	private Animator animator;
	private final MutableVec2d speedVec = new MutableVec2d();

    private final Square footPos = new Square(0, 0, 5, Color.blue, 2);

    public Player() { GamePanel.getInstance().addElement(footPos); }

    @Override
    public void setup()
    {
        renderer = createSwingRenderer();
        transform.setScale(3, 3);
        animator = new Animator((SpriteRenderer) renderer);

        animator.addAnimation(
                "idleD", 1, true,
                AssetManager.getSprite("player_sheet/walk_down/sprite_01.png")
        );
        animator.addAnimation(
                "idleU", 1, true,
                AssetManager.getSprite("player_sheet/walk_up/sprite_04.png")
        );
        animator.addAnimation(
                "idleR", 1, true,
                AssetManager.getSprite("player_sheet/walk_right/sprite_10.png")
        );
        animator.addAnimation(
                "idleL", 1, true,
                AssetManager.getSprite("player_sheet/walk_left/sprite_07.png")
        );

        animator.addAnimation("wk_d", 10, true, "player_sheet/walk_down");
        animator.addAnimation("wk_u", 10, true, "player_sheet/walk_up");
        animator.addAnimation("wk_r", 10, true, "player_sheet/walk_right");
        animator.addAnimation("wk_l", 10, true, "player_sheet/walk_left");
    }

    @Override
    public void update()
    {
        animator.update();

        speedVec.set(
                Input.getAxisRaw("Horizontal"),
                Input.getAxisRaw("Vertical")
        );

        float speed = Input.getKey(KeyEvent.VK_SHIFT) ? 8 : 3;

        if (speedVec.magnitudeSqrt() > 0)
        {
            Vec2d direction = new MutableVec2d(speedVec).normalized().mul(speed);
            Vec2d nextPos = new MutableVec2d(transform.getPosition()).add(direction);

            boolean collision = false;
            double footX = nextPos.x();
            double footY = nextPos.y() + (transform.getScale().y() * 7);

            footPos.getTransform().setPosition(footX, footY);

            if (currentMap.isSolidAt(footX, footY))
                collision = true;

            if (!collision || Input.getKey(KeyEvent.VK_CONTROL))
                transform.translate(direction);

            updateAnimation(speed);
        }
        else
            animator.play("idle" + lastLookDir.c());

        GamePanel.getCamera().lookAt(transform.getPosition());
    }

    private void updateAnimation(float speed)
    {
        if (speedVec.y < 0)
        {
            animator.play("wk_u");
            lastLookDir = LastLookDir.UP;
        }
        else if (speedVec.y > 0)
        {
            animator.play("wk_d");
            lastLookDir = LastLookDir.DOWN;
        }
        else if (speedVec.x > 0)
        {
            animator.play("wk_r");
            lastLookDir = LastLookDir.RIGHT;
        }
        else if (speedVec.x < 0)
        {
            animator.play("wk_l");
            lastLookDir = LastLookDir.LEFT;
        }

        animator.getCurrentAnim().setFrameDurationMillis(speed == 8 ? 40 : 60);
    }

    public void setCurrentMap(Tilemap currentMap) { this.currentMap = currentMap; }

    @Override
	protected Renderer createSwingRenderer() {
		return new SpriteRenderer("player_sheet/walk_down/sprite_01.png");
	}
}
