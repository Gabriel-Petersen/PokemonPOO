package game.battle;
public interface Trainer{
    String getDisplayName();
    Team getTeam();
    Boolean isWild();
    CombatAction selectAction(BattleContext context);
}