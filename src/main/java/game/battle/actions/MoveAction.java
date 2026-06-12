package game.battle.actions;
import java.awt.Color;

import engine.events.EventScheduler;
import engine.events.LambdaEvent;
import engine.events.ProgressBarChangeEvent;
import engine.events.TypewriterEvent;
import game.battle.ActionResult;
import game.battle.BattleContext;
import game.battle.Trainer;
import game.creature.Pokemon;
import game.creature.move.Move;
import game.creature.move.StatType;
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
        var messages = result.getResultMessages();
        for (String txt : messages)
            scheduler.enqueue(new TypewriterEvent(context.getHud().getConsole(), txt, 0.1, 2));

        if (result.getDamageApplied() > 0) {
            var barTarget = getActor() == context.getPlayer() ? context.getHud().getOpponentPokemonIcon() : context.getHud().getPlayerPokemonIcon();
            scheduler.enqueue(new LambdaEvent(() -> barTarget.getHpBar().setFillColor(Color.red)));
            scheduler.enqueue(new ProgressBarChangeEvent(
                    barTarget.getHpBar(),
                    Double.max(0.0, (double)target.getCurrentHp() / target.getEffectiveStat(StatType.HP, context)),
            1
            ));
            scheduler.enqueue(new LambdaEvent(() -> barTarget.getHpBar().setFillColor(Color.green)));

            var opponent = context.getOpponent().getCurrent();
            if (!opponent.isAlive())
                scheduler.enqueue(new TypewriterEvent(
                        context.getHud().getConsole(),
                        opponent.getNickname() + " foi derrotado!!",
                        0.1,
                        2
                ));
        }
        return result.getHit() ? ActionResult.SUCCESS : ActionResult.MISSED;
    }
    @Override
    public Integer getPriority(){return move.getPriority() * user.getCurrentStats().getValue(StatType.SPEED);}
}