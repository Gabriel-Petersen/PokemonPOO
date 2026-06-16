package game.battle;

import engine.core.GamePanel;
import engine.events.EventScheduler;
import engine.events.LambdaEvent;
import engine.events.TypewriterEvent;
import engine.input.Input;
import engine.lifecycle.Updatable;
import engine.ui.elements.UiText;
import game.battle.actions.CombatAction;
import game.battle.actions.SwitchAction;
import game.creature.Pokemon;
import game.creature.Specie;
import game.entities.NpcTrainer;
import game.player.Player;
import game.ui.battle.BattleHud;
import game.ui.battle.NicknameOverlay;

import java.awt.Color;
import java.awt.event.KeyEvent;
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
        if (isEnding) return;
        if (battle.isFinished())
        {
            isEnding = true;
            battleHud.setVisible(false);
            if (player instanceof Player p)
            {
                p.setBattling(false);
                if (!opponent.getTeam().hasAvailableMember() && !opponent.isWild())
                {
                    p.getMetadata().addMoney(NpcTrainer.MONEY_PRIZE * opponent.getTeam().getMembers().size());
                    p.getMetadata().addNewWin();
                }
                else opponent.getTeam().healAll();
            }
            return;
        }

        if (Input.getKeyDown(KeyEvent.VK_P))
        {
            System.out.println("Player Stats:");
            System.out.println(player.getCurrent().getEffectiveStats(battle.getContext()));
            System.out.println("\n--------------------------------------------------------------\n");
            System.out.println("Opponent Stats:");
            System.out.println(opponent.getCurrent().getEffectiveStats(battle.getContext()));
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

            var other = action.getActor().equals(player) ? opponent : player;
            if (result == ActionResult.INVALID_ACTION) {
                roundInterrupted = true;
                break;
            }

            if (!other.getCurrent().isAlive())
            {
                scheduler.enqueue(new TypewriterEvent(
                        battleHud.getConsole(),
                        other.getCurrent().getNickname() + " foi derrotado!",
                        0.05,
                        1
                ));

                if (other.equals(opponent))
                {
                    Pokemon defeatedOpponent = opponent.getCurrent();

                    int xpGained = defeatedOpponent.getCurrentLevel() * 25;

                    for (var member : player.getTeam().getMembers())
                    {
                        scheduler.enqueue(new TypewriterEvent(
                                battleHud.getConsole(),
                                member.getNickname() + " ganhou " + xpGained + " pontos de EXP!",
                                0.01,
                                1.2
                        ));

                        scheduler.enqueue(new LambdaEvent(() -> {
                            String oldName = member.getNickname();
                            Specie oldSpecie = member.getSpecie();

                            boolean leveled = member.gainExperience(xpGained);

                            if (leveled)
                            {
                                scheduler.enqueue(new TypewriterEvent(
                                        battleHud.getConsole(),
                                        member.getNickname() + " subiu para o Nível " + member.getCurrentLevel() + "!",
                                        0.01,
                                        1.5
                                ));

                                if (!member.getSpecie().equals(oldSpecie)) {
                                    scheduler.enqueue(new TypewriterEvent(
                                            battleHud.getConsole(),
                                            "Parabéns! Seu " + oldName + " evoluiu para " + member.getSpecie().getName() + "!",
                                            0.01,
                                            2.0
                                    ));
                                }

                                battleHud.updateActivePokemonSprites();
                            }
                        }));
                    }
                }

                if (battle.isFinished())
                    enqueueBattleOverVisuals();
                break;
            }

            if (result == ActionResult.CAPTURED)
            {
                Pokemon capturedPokemon = opponent.getCurrent();

                scheduler.enqueue(new LambdaEvent(() -> handleCaptureFlow(capturedPokemon)));
                break;
            }

            if (result == ActionResult.FLED)
            {
                isEnding = true;
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
                if (!battle.isFinished() && !isEnding)
                {
                    if (!opponent.getCurrent().isAlive())
                    {
                        if (opponent.getTeam().hasAvailableMember())
                        {
                            var members = opponent.getTeam().getMembers();
                            int nextAliveIndex = -1;
                            for (int i = 0; i < members.size(); i++)
                                if (members.get(i).isAlive()) {
                                    nextAliveIndex = i;
                                    break;
                                }

                            if (nextAliveIndex != -1)
                            {
                                SwitchAction opponentSwitch = new SwitchAction(nextAliveIndex, opponent);
                                opponentSwitch.execute(battle.getContext(), scheduler);

                                scheduler.enqueue(new TypewriterEvent(
                                        battleHud.getConsole(),
                                        opponent.getDisplayName() + " enviou " + members.get(nextAliveIndex).getNickname() + "!",
                                        0.05,
                                        1.2
                                ));

                                scheduler.enqueue(new LambdaEvent(battleHud::updateActivePokemonSprites));
                            }
                        }
                    }

                    if (!player.getCurrent().isAlive())
                    {
                        if (player.getTeam().hasAvailableMember())
                        {
                            battleHud.updateConsoleMessage(
                                    player.getCurrent().getNickname() + " desmaiou! Escolha um substituto."
                            );

                            battleHud.setActionButtonsEnabled(false);
                            triggerForcedSwitch();
                        }
                        else
                        {
                            battleHud.updateConsoleMessage("Todos os seus Pokémon desmaiaram! Você perdeu...");
                            enqueueBattleOverVisuals();
                        }
                    }
                    else if (opponent.getCurrent().isAlive())
                    {
                        battleHud.setActionButtonsEnabled(true);
                        battleHud.updateConsoleMessage("O que você fará a seguir?");
                    }
                }
            }));

            scheduler.resolve();
        }
    }

    private void handleCaptureFlow(Pokemon capturedPokemon)
    {
        this.isEnding = true;

        var nicknameOverlay = new NicknameOverlay(800, 600, capturedPokemon, null);
        nicknameOverlay.setOnComplete(() -> {
            System.out.println("Apelido processado. Finalizando sessão de batalha.");
            battleHud.setVisible(false);
            if (player instanceof Player p)
                p.setBattling(false);
            nicknameOverlay.setVisible(false);
            GamePanel.getInstance().removeElement(nicknameOverlay);
        });

        ((Player) player).setBattling(true);
        GamePanel.getInstance().addElement(nicknameOverlay);
    }

    private void triggerForcedSwitch()
    {
        battleHud.getPlayerPokemonIcon().setVisible(false);
        battleHud.getOpponentPokemonIcon().setVisible(false);
        var teamPanel = battleHud.getBattleTeamPanel();

        teamPanel.refresh();
        teamPanel.setVisible(true);

        teamPanel.setCustomClickAction((targetPokemon) -> {
            Team playerTeam = player.getTeam();
            int targetIndex = playerTeam.getMembers().indexOf(targetPokemon);

            if (!targetPokemon.isAlive()) {
                battleHud.updateConsoleMessage(targetPokemon.getNickname() + " está desmaiado!");
                return;
            }

            battleHud.getOpponentPokemonIcon().setVisible(true);
            teamPanel.setVisible(false);
            teamPanel.setCustomClickAction(null);

            SwitchAction forcedSwitch = new SwitchAction(targetIndex, player);
            forcedSwitch.execute(battle.getContext(), scheduler);

            scheduler.enqueue(new LambdaEvent(() -> {
                battleHud.setActionButtonsEnabled(true);
                battleHud.getPlayerPokemonIcon().setVisible(true);
                battleHud.updateConsoleMessage("O que você fará a seguir?");
                battleHud.updateActivePokemonSprites();
            }));
            scheduler.resolve();
        });
    }

    private void enqueueBattleOverVisuals() 
    {
        UiText console = battleHud.getConsole();
        scheduler.enqueue(new TypewriterEvent(console, "A batalha terminou!", 0.05, 1.0));
        scheduler.enqueue(new LambdaEvent(() -> {
            System.out.println("Sessão finalizada visivelmente. Retornando ao Overworld.");
            battleHud.setVisible(false);
            ((Player) player).setBattling(false);
        }));
    }

    public Trainer getPlayer() { return player; }
    public Trainer getOpponent() { return opponent; }

    public void submitPlayerAction(CombatAction action) { 
        if (scheduler.isResolving() || battle.isFinished()) return;
        this.playerAction = action; 
    }
}