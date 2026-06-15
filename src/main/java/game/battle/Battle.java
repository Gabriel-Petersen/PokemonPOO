package game.battle;

import game.battle.actions.CombatAction;
import game.ui.battle.BattleHud;
import java.util.List;
import java.util.Random;

public class Battle 
{
    private final Trainer player;
    private final Trainer opponent;
    private BattleContext context;

    public Battle(BattleHud hud, Trainer player, Trainer opponent) {
        this.player = player;
        this.opponent = opponent;
        this.context = new BattleContext(hud, player, opponent);
    }

    public Trainer getPlayer() { return player; }
    public Trainer getOpponent() { return opponent; }
    public BattleContext getContext() { return context; }

    public List<CombatAction> determineOrder(CombatAction playerAction, CombatAction opponentAction) 
    {
        System.out.println("player priority: " + playerAction.getPriority());
        System.out.println("opponent priority: " + opponentAction.getPriority());
        if (playerAction.getPriority() > opponentAction.getPriority())
            return List.of(playerAction, opponentAction);
        else if (opponentAction.getPriority() > playerAction.getPriority())
            return List.of(opponentAction, playerAction);
        
        var rand = new Random(((int)System.currentTimeMillis()));
        if (rand.nextInt(2) == 0)
            return List.of(playerAction, opponentAction);
        return List.of(opponentAction, playerAction);
    }

    public void setContext(BattleContext context) {
        this.context = context;
    }

    public Boolean isFinished() {
        return !(player.getTeam().hasAvailableMember() && opponent.getTeam().hasAvailableMember());
    }
}
