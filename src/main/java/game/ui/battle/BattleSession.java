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

    public BattleSession(Trainer player, Trainer opponent)
    {
        this.player = player;
        this.opponent = opponent;

        GamePanel.getInstance().addScheduler(scheduler);

        battleHud = new BattleHud(800, 600, new Color(40, 44, 52), scheduler, player, opponent);
        battle = new Battle(battleHud, player, opponent);
       
        
        GamePanel.getInstance().addElement(battleHud);
    }

    public Battle getBattle() { return battle; }

    @Override public void setup() { }
    
    @Override
    public void update() {
        if (scheduler.isResolving()) return;
        
        // Aqui no Ciclo 3 entrará a máquina de estados que verifica se ambos os treinadores 
        // já selecionaram suas CombatActions e disparará o processador de turnos.
    }

    public void startBattle()
    {
        battleHud.setVisible(true);
        
        String pActive = player.getTeam().getActiveMember().getSpecie().getName();
        String oActive = opponent.getTeam().getActiveMember().getSpecie().getName();
        
        battleHud.updateConsoleMessage(opponent.getDisplayName() + " enviou " + oActive + "! Vai, " + pActive + "!");
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

        scheduler.enqueue(new LambdaEvent(() -> {
            if (!battle.isFinished()) {
                battleHud.setActionButtonsEnabled(true);
                battleHud.updateConsoleMessage("O que você fará a seguir?");
            }
        }));

        scheduler.resolve();
        return true;
    }

    private void enqueueBattleOverVisuals() 
    {
        UiText console = battleHud.getConsole();
        scheduler.enqueue(new TypewriterEvent(console, "A batalha terminou!", 0.05, 1.0));
        scheduler.enqueue(new LambdaEvent(() -> {
            System.out.println("Sessão finalizada. Retornando ao Overworld.");
            ((Player)player).setBattling(false);
        }));
    }

    public BattleHud getBattleHud() {
        return battleHud;
    }

    public Trainer getPlayer() {
        return player;
    }

    public Trainer getOpponent() {
        return opponent;
    }
}
