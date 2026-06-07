package game.battle;

import game.battle.actions.CombatAction;
import game.itemsystem.Inventory;

public interface Trainer{
    String getDisplayName();
    Team getTeam();
    Boolean isWild();
    CombatAction selectAction(BattleContext context);
    Inventory getInventory();
}