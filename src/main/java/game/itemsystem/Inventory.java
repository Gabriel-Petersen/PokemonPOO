package game.itemsystem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    
    private final Map<Item, ItemStack> items = new HashMap<>();

    public void add(Item item, int amount){ //add: increase the amount of an item, if the item does not exist, add it to the inventory
        if (amount <= 0)
            throw new IllegalArgumentException("amount must be positive");
        if (items.containsKey(item)){
            items.get(item).increase(amount);
        } else {
            items.put(item, new ItemStack(item, amount));
        }
    }

    public boolean remove(Item item, int amount){ //remvove: decrease the amount of an item, if the amount is not enough, return false
        if (amount <= 0)
            throw new IllegalArgumentException("amount must be positive");
        if (items.containsKey(item)){
            var stack = items.get(item);
            if(stack.canConsume(amount)){
                stack.decrease(amount);
                if(stack.getAmount()==0){
                    items.remove(item);
                }
                return true;
            }
        }
        return false;
    }

    public boolean has(Item item, int amount){ //has: check if the inventory has enough amount of an item, return true if it does, false otherwise
        if (amount <= 0)
            throw new IllegalArgumentException("amount must be positive");
        if(items.containsKey(item)){
            return items.get(item).canConsume(amount); 
        }
        return false;
    }

    public int count(Item item){
        if (items.containsKey(item))
            return items.get(item).getAmount();
        return 0;
    }

    public ItemStack getStack(Item item){ //getStack: obtains the stack of an item, return this stack if existis, null otherwise
        return items.get(item);
    }

    public Collection<ItemStack> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }
}
