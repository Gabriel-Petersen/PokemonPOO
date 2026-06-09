package game.ui.battle;

import java.awt.Color;

import engine.core.GamePanel;
import engine.events.EventScheduler;
import engine.lifecycle.Updatable;
import game.battle.ActionResult;
import game.battle.Battle;
import game.battle.Trainer;

public class BattleSession implements Updatable
{
    private final EventScheduler scheduler = new EventScheduler();
    
    private Battle battle;
    
    private BattleHud battleHud;

    public BattleSession(Trainer player, Trainer opponent)
    {
        GamePanel.getInstance().addScheduler(scheduler);

        battle = new Battle(player, opponent);
        battleHud = new BattleHud(500, 300, Color.lightGray, scheduler, player, opponent);
        
        GamePanel.getInstance().addElement(battleHud);
    }

    public Battle getBattle() { return battle; }

    @Override public void setup() { }
    
    @Override
    public void update() {
        if (scheduler.isResolving()) return;
    }

    public void startBattle()
    {
        battleHud.setVisible(true);
    }
}
