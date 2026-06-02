package game.battle;
public abstract class CombatAction{
    private Trainer actor;
    public CombatAction(Trainer actor){this.actor=actor;}
    public Trainer getActor(){return actor;}
    public void setActor(Trainer actor){this.actor=actor;}
    public abstract Integer getPriority();
    public abstract ActionResult execute(BattleContext context);
}