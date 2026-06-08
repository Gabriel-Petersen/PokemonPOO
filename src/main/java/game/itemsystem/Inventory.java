package game.itemsystem;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    
    private final Map<Item, ItemStack> items = new HashMap<>();

    public void add(Item item, int amount){ //add: increase the amount of an item, if the item does not exist, add it to the inventory
        if (items.containsKey(item)){
            items.get(item).increase(amount);
        } else {
            items.put(item, new ItemStack(item, amount));
        }
    }

    public boolean consume(Item item, int amount){ //consume: decrease the amount of an item, if the amount is not enough, return false
        if (items.containsKey(item)){
            if(items.get(item).canConsume(amount)){
                items.get(item).decrease(amount);
                if(items.get(item).getAmount()==0){
                    items.remove(item);
                }
                return true;
            }
        }
        return false;
    }

    public boolean has(Item item, int amount){ //has: check if the inventory has enough amount of an item, return true if it does, false otherwise
        if(items.containsKey(item)){
            return items.get(item).canConsume(amount); 
        }
        return false;
    }

    public int count(Item item){
        return items.get(item).getAmount();
    }

}
