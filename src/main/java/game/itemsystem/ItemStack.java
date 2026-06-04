package game.itemsystem;

import game.battle.ActionResult;

public class ItemStack {
    private Item item;
    private Integer amount;
       
    
    public ItemStack(Item item, Integer amount) {
        this.item = item;
        this.amount = amount;
    }


    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }


    public void increase(Integer amount){
        this.amount+=amount;
    }
    

    public ActionResult decrease(Integer amount){
        if(amount>this.amount){
            return ActionResult.INVALID_ACTION;
        }
        this.amount-=amount;
        return ActionResult.SUCCESS;
    }

    public Boolean canConsume(Integer amount){
        return this.amount>=amount;
        
    }
}
