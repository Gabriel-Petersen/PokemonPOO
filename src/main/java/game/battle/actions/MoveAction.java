package game.battle.actions;
import java.awt.Color;

import engine.events.EventScheduler;
import engine.events.LambdaEvent;
import engine.events.ProgressBarChangeEvent;
import engine.events.TypewriterEvent;
import game.battle.ActionResult;
import game.battle.BattleContext;
import game.battle.Trainer;
import game.creature.move.Move;
import game.creature.StatType;
public class MoveAction extends CombatAction{
    private Move move;
    public MoveAction(Move move,Trainer actor){
        super(actor);
        this.move=move;
    }
    public Move getMove(){return move;}
    public void setMove(Move move){this.move=move;}
    @Override
    public ActionResult execute(BattleContext context, EventScheduler scheduler){
        var user = getActor().getCurrent();
        var target = move.isSelfTarget() ? user : getOtherTrainer(context).getCurrent();
        var result=move.execute(user,target,context);
        var messages = result.getResultMessages();
        for (String txt : messages)
            scheduler.enqueue(new TypewriterEvent(context.getHud().getConsole(), txt, 0.1, 2));

        if (result.getDamageApplied() > 0)
        {
            var barTarget = getActor() == context.getPlayer() ? context.getHud().getOpponentPokemonIcon() : context.getHud().getPlayerPokemonIcon();
            scheduler.enqueue(new LambdaEvent(() -> barTarget.getHpBar().setFillColor(Color.red)));
            scheduler.enqueue(new ProgressBarChangeEvent(
                    barTarget.getHpBar(),
                    Double.max(0.0, (double)target.getCurrentHp() / target.getEffectiveStat(StatType.HP, context)),
            1
            ));
            scheduler.enqueue(new LambdaEvent(() -> barTarget.getHpBar().setFillColor(Color.green)));
        }
        return result.getHit() ? ActionResult.SUCCESS : ActionResult.MISSED;
    }
    @Override
    public Integer getPriority(){
        return move.getPriority() * getActor().getCurrent().getCurrentStats().getValue(StatType.SPEED);
    }
}