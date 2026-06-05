package game.entities;

import game.battle.BattleContext;
import game.battle.Team;
import game.battle.Trainer;
import game.battle.actions.CombatAction;
import game.itemsystem.Inventory;
import game.player.Player;
import java.awt.image.BufferedImage;

public class NpcTrainer extends Npc implements Trainer 
{
    private Team team;
    private Inventory inventory;

    public NpcTrainer(Inventory inventory, Team team, String name, BufferedImage sprite) {
        super(name, sprite);
        this.inventory = inventory;
        this.team = team;
    }

    public NpcTrainer(Inventory inventory, Team team, String name, String spritePath) {
        super(name, spritePath);
        this.inventory = inventory;
        this.team = team;
    }

    @Override public String getDisplayName() { return name; }
    @Override public Team getTeam() { return team; }
    @Override public Boolean isWild() { return false; }
    @Override public Inventory getInventory() { return inventory; }

    public void setTeam(Team team) { this.team = team; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    @Override
    public CombatAction selectAction(BattleContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAction'");
    }

    @Override
    public void onInteract(Player player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
