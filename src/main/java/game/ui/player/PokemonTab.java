package game.ui.player;

import java.awt.Color;

import engine.ui.core.UiTransform.Anchor;
import game.player.Player;
import game.ui.common.TeamUiPanel;

public class PokemonTab extends PauseTab 
{
    private final TeamUiPanel teamUiPanel;

    public PokemonTab(int sizeX, int sizeY, Color color, Player player) 
    {
        super(PauseTabType.TEAM, sizeX, sizeY, color, player);
        getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        getUiTransform().setPosition(80, 0);
        setVisible(false);

        teamUiPanel = new TeamUiPanel(sizeX, sizeY, new Color(40, 44, 52), player.getTeam());
        addChild(teamUiPanel);
    }

    @Override protected void onEnableVisible() { teamUiPanel.refresh(); }
}
