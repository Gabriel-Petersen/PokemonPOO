package game.entities;

import engine.assets.AssetManager;
import engine.core.GamePanel;
import game.battle.BattleContext;
import game.battle.BattleSession;
import game.battle.Team;
import game.battle.Trainer;
import game.battle.actions.CombatAction;
import game.battle.ai.BattleAI;
import game.battle.ai.SmartDamageAI;
import game.itemsystem.Inventory;
import game.player.Player;
import game.ui.common.DialogueBox;

import java.awt.image.BufferedImage;

public class NpcTrainer extends Npc implements Trainer 
{
    public static final int MONEY_PRIZE = 10;
    private BufferedImage battleSprite;
    private Team team;
    private Inventory inventory;
    private BattleAI ai;

    public NpcTrainer(Inventory inventory, Team team, String name, String spritePath, String battleSpritePath, BattleAI ai)
    {
        super(name, spritePath);
        this.inventory = inventory;
        this.team = team;
        this.battleSprite = AssetManager.getSprite(battleSpritePath);
        this.ai = ai == null ? new SmartDamageAI() : ai;
    }

    public NpcTrainer(Inventory inventory, Team team, String name, String spritePath, String battleSpritePath) {
        this(inventory, team, name, spritePath, battleSpritePath, new SmartDamageAI());
    }

    @Override public String getDisplayName() { return name; }
    @Override public Team getTeam() { return team; }
    @Override public Boolean isWild() { return false; }
    @Override public Inventory getInventory() { return inventory; }
    @Override public BufferedImage getOnBattleSprite() { return battleSprite; }
    public BattleAI getAi() { return ai; }

    public void setTeam(Team team) { this.team = team; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    public void setBattleSprite(BufferedImage battleSprite) { this.battleSprite = battleSprite; }
    public void setAi(BattleAI ai) { if(ai != null) this.ai = ai; else System.err.println("Tentou anexar AI nula!"); }

    @Override
    public CombatAction selectAction(BattleContext context) {
        return ai.selectAction(context, this);
    }

    @Override
    public void onInteract(Player player) 
    {
        System.out.println(getDisplayName() + " desafiou o jogador!");
        player.setTalking(true);
        
        var db = DialogueBox.getInstance();
        db.setVisible(true);
        db.getEventQueue().clear();

        if (team.hasAvailableMember()) {
            if (getMessage() != null && getMessage().length > 0 && getMessage()[0] != null)
                for (String s : getMessage()) db.showText(s);
            else
                db.showText(getDisplayName() + " quer batalhar!");
        }
        else db.showText("Você já me derrotou...");

        db.getEventQueue().setOnEndResolving(() -> {
            player.setTalking(false);
            db.setVisible(false);
            player.setBattling(true);
            
            BattleSession session = new BattleSession(player, this);
            GamePanel.getInstance().addElement(session);
        });
        
        this.pending = 1; 
    }
}
