package game.creature.move.status;

import game.battle.BattleContext;
import game.creature.Pokemon;
import java.util.List;

public interface StatusEffect {
    Boolean isExpired();

    void onApply(Pokemon target, BattleContext context);
    
    void onTurnStart(Pokemon target, BattleContext context);

    void onTurnEnd(Pokemon target, BattleContext context);

    List<StatModifierRule> getStatModifierRules();

}
