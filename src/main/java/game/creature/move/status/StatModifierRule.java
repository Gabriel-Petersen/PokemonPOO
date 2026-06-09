package game.creature.move.status;

import game.battle.BattleContext;
import game.creature.Stats;
import game.creature.move.StatType;

class StatModifierRule implements StatModifier {
    private final StatType targetStat;
    private final StatModifier modifier;

    public StatModifierRule(StatType targetStat, StatModifier modifier) {
        this.targetStat = targetStat;
        this.modifier = modifier;
    }

    public Stats applyOn(Stats baseStats, BattleContext context) {
        Integer modifiedValue = modifier.apply(baseStats.getValue(targetStat), context);
        return baseStats.withValue(targetStat, modifiedValue);
    }

    @Override
    public Integer apply(Integer baseValue, BattleContext context) {
        return modifier.apply(baseValue, context);
    }


}
