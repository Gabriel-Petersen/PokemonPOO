package game.battle.actions;

import engine.events.EventScheduler;
import engine.events.TypewriterEvent;
import game.battle.ActionResult;
import game.battle.BattleContext;
import game.battle.Trainer;
import game.entities.NpcTrainer;

public class FleeAction extends CombatAction 
{
    private static final FleeAction INSTANCE = new FleeAction();

    private FleeAction() { super(null); }

    @Override public Integer getPriority() { return Integer.MAX_VALUE; }

    @Override
    public ActionResult execute(BattleContext context, EventScheduler scheduler) 
    {
        if (context.getOpponent() instanceof NpcTrainer) 
        {
            scheduler.enqueue(new TypewriterEvent(context.getHud().getConsole(), "Cant Flee!", 0.1, 1.5));
            return ActionResult.INVALID_ACTION;
        }
        scheduler.enqueue(new TypewriterEvent(context.getHud().getConsole(), "Running away!", 0.1, 2));
        return ActionResult.FLED;
    }

    public static FleeAction getInstance(Trainer player) {
        if (INSTANCE.getActor() == null) INSTANCE.setActor(player);
        return INSTANCE;
    }
}
