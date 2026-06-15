package game.creature.move.status;

import game.battle.BattleContext;
import game.creature.Stats;
import game.creature.StatType;

public record StatModifierRule(StatType targetStat, StatModifier modifier)
{
    public Stats applyOn(Stats baseStats, BattleContext context)
    {
        int baseValue = baseStats.getValue(targetStat);
        int modifiedValue = modifier.modify(baseValue, context);
        return baseStats.withValue(targetStat, modifiedValue);
    }
}