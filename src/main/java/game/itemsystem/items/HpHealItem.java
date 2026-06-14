package game.itemsystem.items;

import game.battle.ActionResult;
import game.creature.Pokemon;
import game.creature.StatType;
import java.awt.image.BufferedImage;

public class HpHealItem extends HealingItem
{
    private Integer healAmount;

    public HpHealItem(String name, String description, String inGameMessage, Integer basePrice, BufferedImage sprite, Integer healAmount) {
        super(name, description, inGameMessage, basePrice, sprite);
        this.healAmount = healAmount;
    }

    @Override
    public Boolean canUse(Pokemon target) {
        return target.getCurrentStats().getValue(StatType.HP)-target.getCurrentHp()>0;
    }

    @Override
    public ActionResult use(Pokemon target) {
        if(canUse(target)){
            target.heal(healAmount);
            return ActionResult.SUCCESS;
        }
        return ActionResult.INVALID_ACTION;
    }
    
    public Integer getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(Integer healAmount) {
        this.healAmount = healAmount;
    }
}
