package game.commerce;

import game.battle.ActionResult;
import game.itemsystem.Item;
import game.player.PlayerMetadata;

public class ShopService {

public ActionResult buyItem(PlayerMetadata player, Seller seller, Item item, int amount){
    if(seller.getInventory().getStack(item).canConsume(amount)){
        int price = item.getBasePrice() * amount;
        if(player.getMoney() >=price){
            player.removeMoney(price);
            seller.getInventory().remove(item, amount);
            return ActionResult.SUCCESS;
        }
    }
    return ActionResult.FAILED;
}


public ActionResult sellItem(PlayerMetadata player, Seller seller, Item item, int amount){
    //TODO: implement this method
    return null;
}
    
}
