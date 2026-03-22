package game;

import java.awt.event.KeyEvent;

import engine.animation.Animator;
import engine.assets.AssetManager;
import engine.core.GameObject;
import engine.core.GamePanel;
import engine.input.Input;
import engine.math.vectors.MutableVec2d;
import engine.rendering.Renderer;
import engine.rendering.SpriteRenderer;

public class Player extends GameObject 
{
    protected enum LastLookDir {
        UP('U'), DOWN('D'), LEFT('L'), RIGHT('R');

        private final char c;
        LastLookDir(char c) { this.c = c; }
        public char c() { return c; }
    }

    private LastLookDir lastLookDir = LastLookDir.DOWN;
	private Animator animator;
	private final MutableVec2d speedVec = new MutableVec2d();

    @Override
    public void setup() {
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

        float speed = Input.getKey(KeyEvent.VK_SHIFT) ? 6 : 2;
        if (speedVec.magnitudeSqrt() > 0)
        {
            transform.translate(speedVec.normalized().mul(speed));
            
            if (speedVec.y < 0) {
            	animator.play("wk_u");
                lastLookDir = LastLookDir.UP;
            } else if (speedVec.y > 0) {
            	animator.play("wk_d");
                lastLookDir = LastLookDir.DOWN;
            } else if (speedVec.x > 0) {
            	animator.play("wk_r");
                lastLookDir = LastLookDir.RIGHT;
            } else if (speedVec.x < 0) {
            	animator.play("wk_l");
                lastLookDir = LastLookDir.LEFT;
            }
            
            animator.getCurrentAnim().setFrameDurationMillis(speed == 6 ? 40 : 60);
        }
        else
        {
        	animator.play("idle" + lastLookDir.c());
        }

        GamePanel.getCamera().lookAt(transform.getPosition());
    }

	@Override
	protected Renderer createSwingRenderer() {
		return new SpriteRenderer("player_sheet/walk_down/sprite_01.png");
	}
}
