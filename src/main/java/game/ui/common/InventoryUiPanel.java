package game.ui.common;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.itemsystem.Inventory;
import game.itemsystem.Item;
import game.itemsystem.ItemSelectionCallback;
import java.awt.Color;
import java.awt.Font;

public class InventoryUiPanel extends UiImage 
{
    private final Inventory inventory;
    private ItemSelectionCallback callback;
    private boolean battleMode;

    private final UiImage container;
    private final UiText descriptionTxt;

    public InventoryUiPanel(int sizeX, int sizeY, Color color, Inventory inventory) {
        super(sizeX, sizeY, color);
        this.inventory = inventory;

        getUiTransform().setAnchor(Anchor.CENTER);
        getUiTransform().setPosition(0, 0);

        container = new UiImage(sizeX - 40, sizeY - 120, new Color(30, 35, 45));
        container.getUiTransform().setAnchor(Anchor.CENTER_TOP);
        container.getUiTransform().setPosition(0, 20);
        addChild(container);

        UiImage descBox = new UiImage(sizeX - 40, 60, new Color(20, 24, 30));
        descBox.getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
        descBox.getUiTransform().setPosition(0, -20);
        addChild(descBox);

        descriptionTxt = new UiText("Selecione um item...");
        descriptionTxt.setFont("Arial", Font.PLAIN, 14);
        descriptionTxt.setColor(Color.LIGHT_GRAY);
        descriptionTxt.getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        descriptionTxt.getUiTransform().setPosition(15, 0);
        descBox.addChild(descriptionTxt);
    }

    public void setupContext(boolean battleMode, ItemSelectionCallback callback) {
        this.battleMode = battleMode;
        this.callback = callback;
        refreshItems();
    }

    public void refreshItems() 
    {
        container.getAllChildrenSet().clear();

        int index = 0;
        int startY = 15;
        int rowHeight = 40;

        for (var entry : inventory.getItems().entrySet()) 
        {
            Item item = entry.getKey();
            int amount = entry.getValue();

            if (battleMode && !item.isBattleUsable()) {
                continue; 
            }

            InventoryItemButton itemBtn = new InventoryItemButton(
                item, 
                amount, 
                descriptionTxt, 
                () -> handleItemClick(item)
            );
            
            itemBtn.getTransform().setScale(container.getTransform().getScale().x() - 30, 32);
            itemBtn.getUiTransform().setAnchor(Anchor.CENTER_TOP);
            itemBtn.getUiTransform().setPosition(0, startY + (index * rowHeight));
            
            itemBtn.setForegroundColor(new Color(45, 52, 68)); // Cor normal de fundo
            itemBtn.setBackgroundColor(new Color(65, 75, 98)); // Cor quando o mouse entra

            container.addChild(itemBtn);
            index++;
        }
        
        if (index == 0)
            descriptionTxt.setText("Nenhum item disponível para este momento.");
    }

    private void handleItemClick(Item item) 
    {
        descriptionTxt.setText(item.getDescription());
        if (callback != null) 
        {
            boolean shouldClose = callback.onItemChosen(item);
            if (shouldClose)
                setVisible(false);
        }
    }

    private static final class InventoryItemButton extends UiButton {
        private final Item item;
        private final UiText sharedDescriptionTxt;

        public InventoryItemButton(Item item, int amount, UiText sharedDescriptionTxt, Runnable onClick) 
        {
            super(item.getName() + "  x" + amount, onClick);
            this.item = item;
            this.sharedDescriptionTxt = sharedDescriptionTxt;
            
            if (getTxtChild() != null) {
                getTxtChild().setColor(Color.WHITE);
            }
        }

        @Override
        public void onPointerEnter() 
        {
            super.onPointerEnter();
            
            if (item.getDescription() != null)
                sharedDescriptionTxt.setText(item.getDescription());
        }

        @Override
        public void onPointerExit() 
        {
            super.onPointerExit();
            sharedDescriptionTxt.setText("Selecione um item...");
        }
    }
}