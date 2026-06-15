package game.creature.move.status;

import game.battle.BattleContext;

@FunctionalInterface
public interface StatModifier {
    int modify(int baseValue, BattleContext context);
}
