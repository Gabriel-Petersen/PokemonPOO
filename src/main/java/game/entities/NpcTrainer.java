package game.entities;

import engine.assets.AssetManager;
import game.battle.BattleContext;
import game.battle.BattleSession;
import game.battle.Team;
import game.battle.Trainer;
import game.battle.actions.CombatAction;
import game.itemsystem.Inventory;
import game.player.Player;
import game.ui.common.DialogueBox;

import java.awt.image.BufferedImage;

public class NpcTrainer extends Npc implements Trainer 
{
    private BufferedImage battleSprite;
    private Team team;
    private Inventory inventory;

    public NpcTrainer(Inventory inventory, Team team, String name, BufferedImage sprite, BufferedImage battleSprite) 
    {
        super(name, sprite);
        this.inventory = inventory;
        this.team = team;
        this.battleSprite = battleSprite;
    }

    public NpcTrainer(Inventory inventory, Team team, String name, String spritePath, String battleSpritePath) 
    {
        super(name, spritePath);
        this.inventory = inventory;
        this.team = team;
        this.battleSprite = AssetManager.getSprite(battleSpritePath);
    }

    @Override public String getDisplayName() { return name; }
    @Override public Team getTeam() { return team; }
    @Override public Boolean isWild() { return false; }
    @Override public Inventory getInventory() { return inventory; }
    @Override public BufferedImage getOnBattleSprite() { return battleSprite; }

    public void setTeam(Team team) { this.team = team; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    public void setBattleSprite(BufferedImage battleSprite) { this.battleSprite = battleSprite; }

    @Override
    public CombatAction selectAction(BattleContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAction'");
    }

    @Override
    public void onInteract(Player player) 
    {
        System.out.println(getDisplayName() + " desafiou o jogador!");
        player.setTalking(true);
        
        var db = DialogueBox.getInstance();
        db.setVisible(true);
        db.getEventQueue().clear();

        if (getMessage() != null && getMessage().length > 0 && getMessage()[0] != null)
            for (String s : getMessage()) db.showText(s);
        else
            db.showText(getDisplayName() + " quer batalhar!");

        db.getEventQueue().setOnEndResolving(() -> {
            player.setTalking(false);
            db.setVisible(false);
            player.setBattling(true);
            
            BattleSession session = new BattleSession(player, this);
            session.startBattle();
        });
        
        this.pending = 1; 
    }
}
