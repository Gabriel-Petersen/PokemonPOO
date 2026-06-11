package game.commerce;
import game.itemsystem.Inventory;

public interface Seller {
    Inventory getInventory();
    String getDisplayName();
}
