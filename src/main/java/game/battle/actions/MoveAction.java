package game.battle.actions;
import engine.events.EventScheduler;
import engine.events.TypewriterEvent;
import game.battle.ActionResult;
import game.battle.BattleContext;
import game.battle.Trainer;
import game.creature.Pokemon;
import game.creature.move.Move;
public class MoveAction extends CombatAction{
    private Pokemon user,target;
    private Move move;
    public MoveAction(Move move,Pokemon target,Pokemon user,Trainer actor){
        super(actor);
        this.move=move;
        this.target=target;
        this.user=user;
    }
    public Pokemon getUser(){return user;}
    public void setUser(Pokemon user){this.user=user;}
    public Pokemon getTarget(){return target;}
    public void setTarget(Pokemon target){this.target=target;}
    public Move getMove(){return move;}
    public void setMove(Move move){this.move=move;}
    @Override
    public ActionResult execute(BattleContext context, EventScheduler scheduler){
        var result=move.execute(user,target,context);
        scheduler.enqueue(new TypewriterEvent(context.getHud().getConsole(), result.getResultMessage(), 0.1, 2));
        //if (!user.isAlive() || !target.isAlive())
        return null;
    }
    @Override
    public Integer getPriority(){return move.getPriority();}
}