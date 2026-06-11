package game.ui.battle;

import engine.core.GamePanel;
import engine.events.EventScheduler;
import engine.events.LambdaEvent;
import engine.events.TypewriterEvent;
import engine.lifecycle.Updatable;
import engine.ui.elements.UiText;
import game.battle.ActionResult;
import game.battle.Battle;
import game.battle.Trainer;
import game.battle.actions.CombatAction;
import game.player.Player;

import java.awt.Color;
import java.util.List;

public class BattleSession implements Updatable
{
    private final EventScheduler scheduler = new EventScheduler();
    private final Battle battle;
    private final BattleHud battleHud;
    private final Trainer player;
    private final Trainer opponent;

    private CombatAction playerAction;
    private CombatAction opponentAction;
    private boolean isEnding = false;
    
    public BattleSession(Trainer player, Trainer opponent)
    {
        this.player = player;
        this.opponent = opponent;

        GamePanel.getInstance().addScheduler(scheduler);

        battleHud = new BattleHud(this, 800, 600, new Color(40, 44, 52), scheduler, player, opponent);
        battle = new Battle(battleHud, player, opponent);
        
        GamePanel.getInstance().addElement(battleHud);
    }

    public Battle getBattle() { return battle; }

    @Override public void setup() { }
    
    @Override
    public void update() 
    {
        if (scheduler.isResolving()) return;

        if (battle.isFinished() && !isEnding) 
        {
            isEnding = true;
            battleHud.setVisible(false);
            if (player instanceof Player) {
                ((Player) player).setBattling(false);
            }
            return;
        }

        if (opponentAction == null && playerAction != null) 
            opponentAction = opponent.selectAction(battle.getContext());
        

        if (playerAction != null && opponentAction != null)
        {
            processTurn(playerAction, opponentAction);
            playerAction = null;
            opponentAction = null;
        }
    }

    public boolean processTurn(CombatAction playerAction, CombatAction opponentAction) 
    {
        battleHud.setActionButtonsEnabled(false);
        List<CombatAction> orderedActions = battle.determineOrder(playerAction, opponentAction);

        boolean roundInterrupted = false;

        for (CombatAction action : orderedActions) 
        {
            ActionResult result = action.execute(battle.getContext(), scheduler);
            
            if (result == ActionResult.INVALID_ACTION) {
                roundInterrupted = true;
                break; 
            }

            if (result == ActionResult.FLED) {
                 ((Player)player).setBattling(false);
                enqueueBattleOverVisuals();
                break;
            }

            if (battle.isFinished()) {
                enqueueBattleOverVisuals();
                break;
            }
        }

        if (roundInterrupted) 
        {
            scheduler.clear(); 
            
            battleHud.setActionButtonsEnabled(true);
            battleHud.updateConsoleMessage("Ação inválida! Escolha outra opção.");
        } 
        else 
        {
            scheduler.enqueue(new LambdaEvent(() -> {
                if (!battle.isFinished()) {
                    battleHud.setActionButtonsEnabled(true);
                    battleHud.updateConsoleMessage("O que você fará a seguir?");
                }
            }));
            
            scheduler.resolve();
        }

        return true;
    }

    public void startBattle()
    {
        battleHud.setVisible(true);
        
        String pActive = player.getTeam().getActiveMember().getSpecie().getName();
        String oActive = opponent.getTeam().getActiveMember().getSpecie().getName();
        
        battleHud.updateConsoleMessage(opponent.getDisplayName() + " enviou " + oActive + "! Vai, " + pActive + "!");
    }

    private void enqueueBattleOverVisuals() 
    {
        UiText console = battleHud.getConsole();
        scheduler.enqueue(new TypewriterEvent(console, "A batalha terminou!", 0.05, 1.0));
        scheduler.enqueue(new LambdaEvent(() -> {
            System.out.println("Sessão finalizada visivelmente. Retornando ao Overworld.");
            ((Player)player).setBattling(false);
        }));
    }

    public BattleHud getBattleHud() { return battleHud; }
    public Trainer getPlayer() { return player; }
    public Trainer getOpponent() { return opponent; }

    public void submitPlayerAction(CombatAction action) { 
        if (scheduler.isResolving() || battle.isFinished()) return;
        this.playerAction = action; 
    }
}