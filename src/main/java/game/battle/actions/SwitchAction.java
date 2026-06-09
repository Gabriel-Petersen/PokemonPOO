package game.battle.actions;
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
    public ActionResult execute(BattleContext context){
        Team team=getActor().getTeam();
        List<Pokemon>members=team.getMembers();
        if(!team.hasIndex(targetIndex)||!members.get(targetIndex).isAlive())return ActionResult.INVALID_ACTION;
        team.switchActive(targetIndex);
        return ActionResult.SUCCESS;
    }
    @Override
    public Integer getPriority(){return Integer.MAX_VALUE;}
}