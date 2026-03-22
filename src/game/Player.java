package game;

import java.awt.event.KeyEvent;

import engine.animation.Animation;
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
	private Animator animator;
	private final MutableVec2d speedVec = new MutableVec2d();
	@Override
	public void setup() {
		renderer = createSwingRenderer();
		
		transform.setScale(3, 3);
		
		animator = new Animator((SpriteRenderer) renderer);
		var idleD = AssetManager.getSprite("player_sheet_data/sprite_01.png");
		var idleU = AssetManager.getSprite("player_sheet_data/sprite_04.png");
		var idleR = AssetManager.getSprite("player_sheet_data/sprite_10.png");
		var idleL = AssetManager.getSprite("player_sheet_data/sprite_07.png");
		
		animator.addAnimation(new Animation("idleD", 10, true, idleD));
		animator.addAnimation(new Animation("idleU", 10, true, idleU));
		animator.addAnimation(new Animation("idleR", 10, true, idleR));
		animator.addAnimation(new Animation("idleL", 10, true, idleL));
		
		animator.addAnimation(new Animation("wk_r", 10, true, 
				AssetManager.getSprite("player_sheet_data/sprite_09.png"),
				idleR,
				AssetManager.getSprite("player_sheet_data/sprite_11.png")
		));
		animator.addAnimation(new Animation("wk_l", 10, true, 
				AssetManager.getSprite("player_sheet_data/sprite_06.png"),
				idleL,
				AssetManager.getSprite("player_sheet_data/sprite_08.png")
		));
		animator.addAnimation(new Animation("wk_u", 10, true, 
				AssetManager.getSprite("player_sheet_data/sprite_03.png"),
				idleU,
				AssetManager.getSprite("player_sheet_data/sprite_05.png")
		));
		animator.addAnimation(new Animation("wk_d", 10, true, 
				AssetManager.getSprite("player_sheet_data/sprite_00.png"),
				idleD,
				AssetManager.getSprite("player_sheet_data/sprite_02.png")
		));
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
            } else if (speedVec.y > 0) {
            	animator.play("wk_d");
            } else if (speedVec.x > 0) {
            	animator.play("wk_r");
            } else if (speedVec.x < 0) {
            	animator.play("wk_l");
            }
            
            animator.getCurrentAnim().setFrameDurationMillis(speed == 6 ? 30 : 60);
        }
        else
        {
        	animator.play("idleD");
        }
    }

	@Override
	protected Renderer createSwingRenderer() {
		return new SpriteRenderer("player_sheet_data/sprite_01.png");
	}
}
