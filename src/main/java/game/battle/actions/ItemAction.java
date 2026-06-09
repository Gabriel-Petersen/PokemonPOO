package game.battle.actions;
import game.battle.ActionResult;
import game.battle.BattleContext;
import game.battle.Trainer;
import game.creature.Pokemon;
import game.itemsystem.Item;
public class ItemAction extends CombatAction{
    private Pokemon target;
    private Item item;
    public ItemAction(Item item,Pokemon target,Trainer actor){
        super(actor);
        this.item=item;
        this.target=target;
    }
    public Pokemon getTarget(){return target;}
    public void setTarget(Pokemon target){this.target=target;}
    public Item getItem(){return item;}
    public void setItem(Item item){this.item=item;}
    @Override
    public ActionResult execute(BattleContext context){
        if(item.canUse(target))return item.use(target);
        return ActionResult.INVALID_ACTION;
    }
    @Override
    public Integer getPriority(){return Integer.MAX_VALUE/2;}
}