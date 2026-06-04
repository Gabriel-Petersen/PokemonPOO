package game.itemsystem.items;

import game.battle.ActionResult;
import game.creature.Pokemon;
import game.creature.move.status.StatusEffect;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class StatusHealItem extends HealingItem {
    private final Set<StatusEffect> curedStatus = new HashSet<>();

    public StatusHealItem(String name, String description, Integer basePrice, BufferedImage sprite) {
        super(name, description, basePrice, sprite);
    }

    public void addCurableStatus(StatusEffect status) { curedStatus.add(status); }
    public void removeCurableStatus(StatusEffect status) { curedStatus.remove(status); }

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
