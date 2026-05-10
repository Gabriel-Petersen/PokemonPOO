package game.ui.player;

import engine.ui.elements.UiImage;
import game.player.Player;
import java.awt.Color;

public abstract class PauseTab extends UiImage 
{
    protected Player player;
    protected final PauseTabType type;

    protected PauseTab(PauseTabType type, int sizeX, int sizeY, Color color, Player player) 
    {
        super(sizeX, sizeY, color);
        this.type = type;
        this.player = player;
    }

    public Player getPlayer() { return player; }
}
