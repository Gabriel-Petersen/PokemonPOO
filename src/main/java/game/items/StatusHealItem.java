package game.items;

import game.battle.ActionResult;
import game.creature.Pokemon;
import game.creature.move.status.StatusEffect;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class StatusHealItem extends HealingItem {
    private final Set<StatusEffect> curedStatus = new HashSet<>();

    public StatusHealItem(String name, String description, Boolean battleUsable, Integer basePrice,
            BufferedImage sprite, Integer healAmount) {
        super(name, description, battleUsable, basePrice, sprite, healAmount);
    }



    @Override
    public Boolean canUse(Pokemon target) {
        var lista = target.getStatusEffects();
        for(StatusEffect eff : lista){
            if(curedStatus.contains(eff)){
                return true;
            }
        }
        return false;
    }

    @Override
    public ActionResult use(Pokemon target) {
        if(canUse(target)){
            target.getStatusEffects().removeAll(curedStatus);
            return ActionResult.SUCCESS;
        }
        return ActionResult.INVALID_ACTION;
    }
}
