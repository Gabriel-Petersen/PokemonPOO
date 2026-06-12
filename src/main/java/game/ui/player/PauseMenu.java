package game.ui.player;

import engine.core.GamePanel;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import game.player.Player;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class PauseMenu extends UiImage 
{
	private static final Color FDW_BTN_COLOR = new Color(124, 139, 182);
	private static final Color BG_BTN_COLOR = new Color(77, 105, 157);

	private static final Map<PauseTabType, PauseTab> tabs = new HashMap<>();

	private final UiButton info = new UiButton("Info", this::onInfoClick);
	private final UiButton inventory = new UiButton("Inventory", this::onInventoryClick);
	private final UiButton team = new UiButton("Team", this::onTeamClick);
	private final UiButton closeTabBtn = new UiButton("X", this::closeAll);
	
	public PauseMenu(int sizeX, int sizeY, Color color, Player player) 
	{
		super(sizeX, sizeY, color);
		getUiTransform().setPosition(-24, 0);
	
		getUiTransform().setAnchor(Anchor.CENTER_RIGHT);
		addChild(info);
		addChild(inventory);
		addChild(team);

		closeTabBtn.setBackgroundColor(Color.orange);
		closeTabBtn.setForegroundColor(Color.red);

		info.getTransform().setScale(100, 44);
		inventory.getTransform().setScale(100, 44);
		team.getTransform().setScale(100, 44);

		info.getUiTransform().setAnchor(Anchor.CENTER_TOP);
		inventory.getUiTransform().setAnchor(Anchor.CENTER_TOP);
		team.getUiTransform().setAnchor(Anchor.CENTER_TOP);

		info.getUiTransform().setPosition(0, 20);
		inventory.getUiTransform().setPosition(0, 76);
		team.getUiTransform().setPosition(0, 132);

		info.setForegroundColor(FDW_BTN_COLOR);
		inventory.setForegroundColor(FDW_BTN_COLOR);
		team.setForegroundColor(FDW_BTN_COLOR);

		info.setBackgroundColor(BG_BTN_COLOR);
		inventory.setBackgroundColor(BG_BTN_COLOR);
		team.setBackgroundColor(BG_BTN_COLOR);

		closeTabBtn.getTransform().setScale(40, 40);
		closeTabBtn.getUiTransform().setPosition(0, -40);
		closeTabBtn.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
		closeTabBtn.setVisible(false);
		addChild(closeTabBtn);

		InfoPauseTab infoTab = new InfoPauseTab(450, 300, color, player);
		tabs.put(PauseTabType.INFO, infoTab);
		GamePanel.getInstance().addElement(infoTab);

		InventoryTab invTab = new InventoryTab(450, 300, color, player);
		tabs.put(PauseTabType.INVENTORY, invTab);
		GamePanel.getInstance().addElement(invTab);

		PokemonTab pokTab = new PokemonTab(800, 400, color, player);
        tabs.put(PauseTabType.TEAM, pokTab);
        GamePanel.getInstance().addElement(pokTab);
	}

	private void closeAll() 
	{
		for (var tab : tabs.values()) tab.setVisible(false);
		closeTabBtn.setVisible(false);
	}

    @Override protected void onEnableVisible() { closeAll(); }
	@Override protected void onDisableVisible() { closeAll(); }

	private void onInfoClick()
	{
		System.out.println("Info clicked");
		closeAll();
		closeTabBtn.setVisible(true);
		var currTab = tabs.get(PauseTabType.INFO);
		if (currTab != null)
			currTab.setVisible(true);
		else
			throw new NullPointerException(
				"There's no such 'InfoTab' in PauseTabType.INFO key at 'tabs' Map"
			);
	}

	private void onInventoryClick()
	{
		System.out.println("Inventory clicked");
		closeAll();
		closeTabBtn.setVisible(true);
		var currTab = tabs.get(PauseTabType.INVENTORY);
		if (currTab != null)
			currTab.setVisible(true);
		else
			throw new NullPointerException(
				"There's no such 'InfoTab' in PauseTabType.INVENTORY key at 'tabs' Map"
			);
	}

	private void onTeamClick()
    {
        System.out.println("Team clicked");
        closeAll();
        closeTabBtn.setVisible(true);
        var currTab = tabs.get(PauseTabType.TEAM);
        if (currTab != null)
            currTab.setVisible(true);
        else
            throw new NullPointerException(
                "There's no such 'PokemonTab' in PauseTabType.TEAM key at 'tabs' Map"
            );
    }
}
