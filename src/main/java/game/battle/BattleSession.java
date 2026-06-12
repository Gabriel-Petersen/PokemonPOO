package game.battle;

import engine.core.GamePanel;
import engine.events.EventScheduler;
import engine.events.LambdaEvent;
import engine.events.TypewriterEvent;
import engine.lifecycle.Updatable;
import engine.ui.elements.UiText;
import game.battle.actions.CombatAction;
import game.entities.NpcTrainer;
import game.player.Player;
import game.ui.battle.BattleHud;

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



        battleHud = new BattleHud(this, 800, 600, new Color(40, 44, 52), player, opponent);
        battle = new Battle(battleHud, player, opponent);

        scheduler.setOnEndResolving(() -> {
            System.out.println("Fim do processamento visual do turno!");
            player.getCurrent().resolveStatusAtTurnEnd(battle.getContext());
            opponent.getCurrent().resolveStatusAtTurnEnd(battle.getContext());
        });

        GamePanel.getInstance().addScheduler(scheduler);
        GamePanel.getInstance().addElement(battleHud);
    }

    public Battle getBattle() { return battle; }

    @Override
    public void setup()
    {
        battleHud.setVisible(true);

        String pActive = player.getCurrent().getSpecie().getName();
        String oActive = opponent.getCurrent().getSpecie().getName();

        battleHud.updateConsoleMessage(opponent.getDisplayName() + " enviou " + oActive + "! Vai, " + pActive + "!");

    }
    
    @Override
    public void update() 
    {
        if (battle.isFinished() && !isEnding) 
        {
            isEnding = true;
            battleHud.setVisible(false);
            if (player instanceof Player p) {
                p.setBattling(false);
                if (opponent instanceof NpcTrainer t) {
                    System.out.println("Entrou aqui!");
                    p.getMetadata().addMoney(NpcTrainer.MONEY_PRIZE * t.getTeam().getMembers().size());
                }
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

    public void processTurn(CombatAction playerAction, CombatAction opponentAction)
    {
        System.out.println("Processing turn...");
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

    public Trainer getPlayer() { return player; }
    public Trainer getOpponent() { return opponent; }

    public void submitPlayerAction(CombatAction action) { 
        if (scheduler.isResolving() || battle.isFinished()) return;
        this.playerAction = action; 
    }
}