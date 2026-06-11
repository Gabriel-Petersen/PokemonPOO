package game.entities;

import game.commerce.Seller;
import game.itemsystem.Inventory;
import game.player.Player;
import game.ui.shop.ShopHud;

public class NpcSeller extends Npc implements Seller{

    private final Inventory inventory = new Inventory();

    public NpcSeller(String name, String sprite) {
        super(name, sprite);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public void onInteract(Player player) {
        player.setTalking(true);
        ShopHud.getInstance().openShop(player, this);
    }
}
