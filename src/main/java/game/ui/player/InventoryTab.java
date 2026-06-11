package game.ui.player;

import engine.ui.core.UiTransform.Anchor;
import game.player.Player;
import game.ui.common.InventoryUiPanel;
import java.awt.Color;

public class InventoryTab extends PauseTab 
{
    private final InventoryUiPanel inventoryUiPanel;

    public InventoryTab(int sizeX, int sizeY, Color color, Player player) {
        super(PauseTabType.INVENTORY, sizeX, sizeY, color, player);
        getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        getUiTransform().setPosition(80, 0);
        setVisible(false);

        // Instancia o painel reutilizável dentro da Tab
        inventoryUiPanel = new InventoryUiPanel(sizeX, sizeY, color, player.getInventory());
        addChild(inventoryUiPanel);
    }

    @Override protected void onEnableVisible() 
    {
        inventoryUiPanel.setupContext(false, (item) -> {
            System.out.println("Abri a UI de seleção de Pokémon do time para aplicar: " + item.getName());
            // Aqui você chamaria algo como: tabs.get(PauseTabType.TEAM).openForItemUse(item);
            return false; // Não fecha o inventário imediatamente, espera escolher o alvo
        });
    }
}
