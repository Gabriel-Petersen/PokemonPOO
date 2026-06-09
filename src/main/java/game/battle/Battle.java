package game.battle;

import java.util.List;
import java.util.Random;

import game.battle.actions.CombatAction;

public class Battle 
{
    private Trainer player;
    private Trainer opponent;
    private BattleContext context;

    private Trainer winner = null;
    private Boolean isFinished = false;

    public Battle(Trainer player, Trainer opponent) {
        this.player = player;
        this.opponent = opponent;
        this.context = new BattleContext(0, player, opponent);
    }

    public Trainer getPlayer() { return player; }
    public Trainer getOpponent() { return opponent; }
    public BattleContext getContext() { return context; }
    public Trainer getWinner() { return winner; }
    public Boolean isFinished() { return isFinished; }

    public List<CombatAction> determineOrder(CombatAction playerAction, CombatAction opponentAction) 
    {
        if (playerAction.getPriority() > opponentAction.getPriority())
            return List.of(playerAction, opponentAction);
        else if (opponentAction.getPriority() > playerAction.getPriority())
            return List.of(opponentAction, playerAction);
        
        var rand = new Random(((int)System.currentTimeMillis()));
        if (rand.nextInt(2) == 0)
            return List.of(playerAction, opponentAction);
        return List.of(opponentAction, playerAction);
    }
}
