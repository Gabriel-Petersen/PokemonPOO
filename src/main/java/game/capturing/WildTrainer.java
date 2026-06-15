package game.capturing;

import game.battle.BattleContext;
import game.battle.Team;
import game.battle.Trainer;
import game.battle.actions.CombatAction;
import game.battle.ai.BattleAI;
import game.battle.ai.SmartDamageAI;
import game.creature.Pokemon;
import game.itemsystem.Inventory;

import java.awt.image.BufferedImage;

public class WildTrainer implements Trainer
{
    private final Team team;
    private final BattleAI ai;

    public WildTrainer(Pokemon pokemon) { this(pokemon, new SmartDamageAI()); }
    public WildTrainer(Pokemon pokemon, BattleAI ai)
    {
        team = new Team();
        team.addMember(pokemon);
        this.ai = ai;
    }

    @Override  public String getDisplayName() { return ""; }
    @Override public Team getTeam() { return team; }
    @Override public Boolean isWild() { return true; }

    @Override
    public CombatAction selectAction(BattleContext context) {
        return ai.selectAction(context, this);
    }

    @Override public Inventory getInventory() { return null; }
    @Override public BufferedImage getOnBattleSprite() { return null; }
}
