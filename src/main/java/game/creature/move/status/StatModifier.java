package game.creature.move.status;

import game.battle.BattleContext;

@FunctionalInterface
public interface StatModifier {
    Integer apply(Integer baseValue, BattleContext context);
}
