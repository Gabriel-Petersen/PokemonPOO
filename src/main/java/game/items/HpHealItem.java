package game.items;

import game.battle.ActionResult;
import game.creature.Pokemon;
import game.creature.move.StatType;

public class HpHealItem extends HealingItem{

    @Override
    public Boolean canUse(Pokemon target) {
        return target.getCurrentStats().getValue(StatType.HP)-target.getCurrentHp()>0;
    }

    @Override
    public ActionResult use(Pokemon target) {
        if(canUse(target)){
            target.heal(getHealAmount());
            return ActionResult.SUCCESS;
        }
        return ActionResult.INVALID_ACTION;
    }
    
}
