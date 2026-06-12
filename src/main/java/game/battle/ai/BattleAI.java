package game.battle.ai;

import game.battle.BattleContext;
import game.battle.Trainer;
import game.battle.actions.CombatAction;

public interface BattleAI {
    CombatAction selectAction(BattleContext context, Trainer npc);
}
