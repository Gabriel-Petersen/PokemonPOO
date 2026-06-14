package game.ui.player;

import java.awt.Color;

import engine.ui.core.UiTransform.Anchor;
import game.battle.ActionResult;
import game.itemsystem.Item;
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

    public void openForItemUse(Item item, Runnable onUseSuccess)
    {
        teamUiPanel.setCustomClickAction((targetPokemon) -> {
            ActionResult result = item.use(targetPokemon);

            if (result == ActionResult.SUCCESS)
            {
                System.out.println(item.getName() + " usado com sucesso em " + targetPokemon.getNickname());

                teamUiPanel.setCustomClickAction(null);
                onUseSuccess.run();

                this.setVisible(false);

                var invTab = PauseMenu.getTabs().get(PauseTabType.INVENTORY);
                if (invTab != null)
                    invTab.setVisible(true);
            }
            else {
                System.out.println("Não foi possível usar " + item.getName() + " em " + targetPokemon.getNickname());
            }
        });

        setVisible(true);
    }

    @Override protected void onEnableVisible() { teamUiPanel.refresh(); }
}
