package game.commerce;
import game.itemsystem.Inventory;
import game.itemsystem.ItemStack;
import java.util.Collection;

public interface Seller {
    public Collection<ItemStack> listItemsForSale();
    public Inventory getInventory();
}
