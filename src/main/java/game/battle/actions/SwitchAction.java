package game.battle.actions;
import engine.events.EventScheduler;
import engine.events.LambdaEvent;
import engine.events.TypewriterEvent;
import game.battle.ActionResult;
import game.battle.BattleContext;
import game.battle.Team;
import game.battle.Trainer;
import game.creature.Pokemon;
import java.util.List;
public class SwitchAction extends CombatAction{
    private Integer targetIndex;
    public SwitchAction(Integer targetIndex,Trainer actor){
        super(actor);
        this.targetIndex=targetIndex;
    }
    public Integer getTargetIndex(){return targetIndex;}
    public void setTargetIndex(Integer targetIndex){this.targetIndex=targetIndex;}
    @Override
    public ActionResult execute(BattleContext context, EventScheduler scheduler){
        Team team=getActor().getTeam();
        List<Pokemon>members=team.getMembers();
        if(!team.hasIndex(targetIndex)||!members.get(targetIndex).isAlive())return ActionResult.INVALID_ACTION;
        
        team.switchActive(targetIndex);
        scheduler.enqueue(new TypewriterEvent(
            context.getHud().getConsole(), 
            "Trocando para " + team.getActiveMember().getNickname(),
            0.1, 
            1.5
        ));
        var newActive = team.getActiveMember();
        
        scheduler.enqueue(new LambdaEvent(() -> {
            var hud = context.getHud();
            if (getActor().equals(context.getPlayer()))
                hud.getPlayerPokemonIcon().setSource(newActive);
            else
                hud.getOpponentPokemonIcon().setSource(newActive);
            hud.updateActivePokemonSprites();
        }));
        newActive.setupForBattle();
        return ActionResult.SUCCESS;
    }
    @Override
    public Integer getPriority(){return Integer.MAX_VALUE;}
}