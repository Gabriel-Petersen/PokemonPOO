package game.ui.player;

import engine.ui.core.UiTransform.Anchor;
import game.player.Player;
import game.ui.common.InventoryUiPanel;
import java.awt.Color;

public class InventoryTab extends PauseTab
{
    private final InventoryUiPanel inventoryUiPanel;

    public InventoryTab(int sizeX, int sizeY, Color color, Player player)
    {
        super(PauseTabType.INVENTORY, sizeX, sizeY, color, player);
        getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        getUiTransform().setPosition(80, 0);
        setVisible(false);

        inventoryUiPanel = new InventoryUiPanel(sizeX, sizeY, color, player.getInventory());
        addChild(inventoryUiPanel);
    }

    @Override protected void onEnableVisible()
    {
        inventoryUiPanel.setupContext(false, (item) -> {
            if (item == null)
                return false;
            System.out.println("Abri a UI de seleção de Pokémon do time para aplicar: " + item.getName());
            PokemonTab pokemonTab = (PokemonTab) PauseMenu.getTabs().get(PauseTabType.TEAM);

            if (pokemonTab != null)
            {
                this.setVisible(false);
                pokemonTab.openForItemUse(item, () -> {
                    getPlayer().getInventory().remove(item, 1);
                    inventoryUiPanel.refreshItems();
                });
            }

            return false;
        });
    }
}