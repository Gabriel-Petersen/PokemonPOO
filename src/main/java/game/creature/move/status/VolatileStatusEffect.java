package game.creature.move.status;

import game.battle.BattleContext;
import game.creature.Pokemon;
import java.util.List;

public class VolatileStatusEffect implements StatusEffect {
    
    private Integer remainingTurns;
    private List<StatModifierRule> modifiers;
    
    @Override
    public Boolean isExpired() {
        return remainingTurns <= 0;
    }

    @Override
    public void onApply(Pokemon target, BattleContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onTurnStart(Pokemon target, BattleContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onTurnEnd(Pokemon target, BattleContext context) {
        remainingTurns--;
    }

    @Override
    public List<StatModifierRule> getStatModifierRules() {
        return modifiers;
    }
}
