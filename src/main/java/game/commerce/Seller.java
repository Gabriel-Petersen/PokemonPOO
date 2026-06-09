package game.commerce;
import game.itemsystem.Inventory;
import game.itemsystem.ItemStack;
import java.util.Collection;

public interface Seller {
    Collection<ItemStack> listItemsForSale();
    Inventory getInventory();
    String getDisplayName();
}
