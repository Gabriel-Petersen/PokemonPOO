package game.battle;

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
}
