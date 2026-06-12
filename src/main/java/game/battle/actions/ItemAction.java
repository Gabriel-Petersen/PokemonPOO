package game.battle.actions;
import engine.events.EventScheduler;
import engine.events.ProgressBarChangeEvent;
import engine.events.TypewriterEvent;
import game.battle.ActionResult;
import game.battle.BattleContext;
import game.battle.Trainer;
import game.creature.Pokemon;
import game.creature.move.StatType;
import game.itemsystem.Item;
import game.itemsystem.items.HpHealItem;
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
    public ActionResult execute(BattleContext context, EventScheduler scheduler)
    {
        if(getActor().getInventory().has(item, 1) && item.canUse(target))
        {
            scheduler.enqueue(new TypewriterEvent(
                context.getHud().getConsole(), 
                "Using " + item.getName() + " in " + target.getNickname(), 
                0.01, 
                1.2
            ));
            scheduler.enqueue(new TypewriterEvent(
                context.getHud().getConsole(), 
                item.getInGameMessage(),
                 0.01, 
                 1.2
            ));
            var result = item.use(target);
            getActor().getInventory().remove(item, 1);
            if (item instanceof HpHealItem)
            {
                var hud = context.getHud();
                double finalValue = (double)target.getCurrentHp() / target.getEffectiveStat(StatType.HP, context);
                if (hud.getPlayerPokemonIcon().getSource().equals(target))
                    scheduler.enqueue(new ProgressBarChangeEvent(
                        hud.getPlayerPokemonIcon().getHpBar(), finalValue, 1
                    ));
                else
                    scheduler.enqueue(new ProgressBarChangeEvent(
                        hud.getOpponentPokemonIcon().getHpBar(), finalValue, 1
                    ));
            }
            return result;
        }
        return ActionResult.INVALID_ACTION;
    }
    @Override
    public Integer getPriority(){return Integer.MAX_VALUE/2;}
}