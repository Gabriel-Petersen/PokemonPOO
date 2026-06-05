package game.battle;

import game.battle.actions.CombatAction;
import game.itemsystem.Inventory;
import java.awt.image.BufferedImage;

public interface Trainer{
    String getDisplayName();
    Team getTeam();
    Boolean isWild();
    CombatAction selectAction(BattleContext context);
    Inventory getInventory();
    BufferedImage getOnBattleSprite();
}