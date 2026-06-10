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

    private Trainer winner = null;
    private Boolean isFinished = false;

    public Battle(BattleHud hud, Trainer player, Trainer opponent) {
        this.player = player;
        this.opponent = opponent;
        this.context = new BattleContext(hud, player, opponent);
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

    public void setContext(BattleContext context) {
        this.context = context;
    }

    public void setWinner(Trainer winner) {
        this.winner = winner;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }
}
