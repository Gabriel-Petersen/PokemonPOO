package game.commerce;

import game.entities.Npc;
import game.itemsystem.Inventory;
import game.itemsystem.ItemStack;
import game.player.Player;
import java.awt.image.BufferedImage;
import java.util.Collection;


public class NpcSeller extends Npc implements Seller{

    private final Inventory inventory = new Inventory();

    public NpcSeller(String name, BufferedImage sprite) {
        super(name, sprite);
    }

    @Override
    public Collection<ItemStack> listItemsForSale() {
        return getInventory().getItems();
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
        super.onInteract(player);
    }

    

}
