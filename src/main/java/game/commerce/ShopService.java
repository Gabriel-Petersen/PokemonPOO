package game.commerce;

import game.battle.ActionResult;
import game.itemsystem.Item;
import game.player.Player;

public class ShopService {

    private static final double MULT = 0.10;

    public ActionResult buyItem(Player player, Seller seller, Item item, int amount){
    if (amount <= 0) return ActionResult.FAILED;
    Integer sellerStack = seller.getInventory().getStack(item);
    if (sellerStack != null && sellerStack >= amount){
        int price = (int)(item.getBasePrice() * amount * (1 + MULT));
        if(player.getMetadata().getMoney() >= price){
            player.getMetadata().removeMoney(price);
            seller.getInventory().remove(item, amount);
            player.getInventory().add(item, amount);
            return ActionResult.SUCCESS;
        }
    }
    return ActionResult.FAILED;
}


public ActionResult sellItem(Player player, Seller seller, Item item, int amount){
    if (amount <= 0) return ActionResult.FAILED;
    if(player.getInventory().getStack(item) >= amount){
        int earning = (int)(item.getBasePrice() * amount * (1 - MULT));
        if (player.getInventory().remove(item, amount)) {
            player.getMetadata().addMoney(earning);
            seller.getInventory().add(item, amount);
            return ActionResult.SUCCESS;
        }
    }
    return ActionResult.FAILED;
}
    
}
