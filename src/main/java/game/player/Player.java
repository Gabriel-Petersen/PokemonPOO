package game.player;

import engine.animation.Animator;
import engine.assets.AssetManager;
import engine.core.GameObject;
import engine.core.GamePanel;
import engine.input.Input;
import engine.math.vectors.MutableVec2d;
import engine.primitives.Square;
import engine.rendering.Renderer;
import engine.rendering.SpriteRenderer;
import engine.tilemap.Tilemap;
import game.ui.player.PauseMenu;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends GameObject 
{
    protected enum LastLookDir {
        UP('u'), DOWN('d'), LEFT('l'), RIGHT('r');

        private final char c;
        LastLookDir(char c) { this.c = c; }
        public char c() { return c; }
    }

    private final PlayerMetadata metadata = new PlayerMetadata();
    private final MutableVec2d speedVec = new MutableVec2d();
    private final MutableVec2d directionPool = new MutableVec2d();
    private final MutableVec2d nextPosPool = new MutableVec2d();
	private final PauseMenu pauseMenu = new PauseMenu(110, 190, Color.lightGray, this);
    private Tilemap currentMap;
    private LastLookDir lastLookDir = LastLookDir.DOWN;
	private Animator animator;
    private boolean isUiOpen = false;
    private boolean isRunning = false;

    private final Square footPos = new Square(0, 0, 5, Color.blue, 2);

    public Player() { 
    	var gamePanel = GamePanel.getInstance();
    	gamePanel.addElement(footPos); 
    	gamePanel.addElement(pauseMenu);
    	pauseMenu.setVisible(false);
    }

    @Override
    public void setup()
    {
        renderer = createSwingRenderer();
        transform.setScale(3, 3);
        animator = new Animator((SpriteRenderer) renderer);

        animator.addAnimation(
                "idle_d", 1, true,
                AssetManager.getSprite("player_sheet/walk_down/sprite_01.png")
        );
        animator.addAnimation(
                "idle_u", 1, true,
                AssetManager.getSprite("player_sheet/walk_up/sprite_04.png")
        );
        animator.addAnimation(
                "idle_r", 1, true,
                AssetManager.getSprite("player_sheet/walk_right/sprite_10.png")
        );
        animator.addAnimation(
                "idle_l", 1, true,
                AssetManager.getSprite("player_sheet/walk_left/sprite_07.png")
        );

        animator.addAnimation("wk_d", 10, true, "player_sheet/walk_down");
        animator.addAnimation("wk_u", 10, true, "player_sheet/walk_up");
        animator.addAnimation("wk_r", 10, true, "player_sheet/walk_right");
        animator.addAnimation("wk_l", 10, true, "player_sheet/walk_left");

        animator.addAnimation("run_d", 10, true, "player_sheet/run_down");
        animator.addAnimation("run_u", 10, true, "player_sheet/run_up");
        animator.addAnimation("run_l", 10, true, "player_sheet/run_left");
        animator.addAnimation("run_r", 10, true, "player_sheet/run_right");
    }

    @Override
    public void update()
    {
        debugs();
        animator.update();
        
        if (Input.getKeyDown(KeyEvent.VK_ESCAPE))
        {
        	isUiOpen = !isUiOpen;
        	pauseMenu.setVisible(isUiOpen);
        }
        else if (canWalk())
        {
            int inputh = Input.getAxisRaw("Horizontal");
            int inputv = Input.getAxisRaw("Vertical");
        	if (inputh != 0)
                speedVec.set(inputh, 0);
            else
                speedVec.set(0, inputv);

            if (speedVec.magnitudeSqrt() > 0)
            {
                isRunning = Input.getKey(KeyEvent.VK_SHIFT);
                directionPool.set(speedVec).normalize();
                directionPool.mul(
                    isRunning ? PlayerMetadata.RUN_SPEED : PlayerMetadata.WK_SPEED
                );
                nextPosPool.set(transform.getPosition()).add(directionPool);

                boolean collision = false;
                double footX = nextPosPool.x();
                double footY = nextPosPool.y() + (transform.getScale().y() * 7);

                footPos.getTransform().setPosition(footX, footY);

                if (currentMap.isSolidAt(footX, footY))
                    collision = true;

                if (!collision || Input.getKey(KeyEvent.VK_CONTROL))
                    transform.translate(directionPool);

                if (collision) isRunning = false;
                updateAnimation();
            }
            else
            {
                isRunning = false;
                animator.play("idle_" + lastLookDir.c());
            }
        }

        GamePanel.getCamera().lookAt(transform.getPosition());
    }

    private void updateAnimation()
    {
        if (speedVec.y < 0)
            lastLookDir = LastLookDir.UP;
        else if (speedVec.y > 0)
            lastLookDir = LastLookDir.DOWN;
        else if (speedVec.x > 0)
            lastLookDir = LastLookDir.RIGHT;
        else if (speedVec.x < 0)
            lastLookDir = LastLookDir.LEFT;

        String prefix = isRunning ? "run_" : "wk_";
        animator.play(prefix + lastLookDir.c());
    }

    public void setCurrentMap(Tilemap currentMap) { this.currentMap = currentMap; }
    
    public PlayerMetadata getMetadata() { return metadata; }
    
    public boolean isUiOpen() { return isUiOpen; }
    
    private boolean canWalk() {
    	return !isUiOpen;
    }

    @Override
	protected Renderer createSwingRenderer() {
		return new SpriteRenderer("player_sheet/walk_down/sprite_01.png");
	}

    private void debugs()
    {
        if (Input.getKeyDown(KeyEvent.VK_P))
            System.out.println(transform.getPosition());
    }
}
