package game.ui.battle;

import java.awt.Color;

import engine.events.EventScheduler;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.battle.Trainer;

public class BattleHud extends UiImage 
{
    private final UiButton exitButton = new UiButton("Exit", () -> onExit());
    private final EventScheduler scheduler;

    private TrainerUiIcon playerIcon;
    private TrainerUiIcon opponentIcon;
    private PokemonInBattleIcon playerPokemonIcon;
    private PokemonInBattleIcon opponentPokemonIcon;
    private UiImage console = new UiImage(150, 50, Color.black);
    private UiText consoleTxt;

    public BattleHud(int sizeX, int sizeY, Color color, EventScheduler scheduler, Trainer player, Trainer opponent) 
    {
        super(sizeX, sizeY, color);
        getUiTransform().setAnchor(Anchor.CENTER);
        addChild(exitButton);
        this.scheduler = scheduler;

        playerIcon = new TrainerUiIcon(player);
        opponentIcon = new TrainerUiIcon(opponent);
        playerPokemonIcon = PokemonInBattleIcon.buildPlayerIcon(player.getTeam().getActiveMember());
        opponentPokemonIcon = PokemonInBattleIcon.buildPlayerIcon(opponent.getTeam().getActiveMember());
        consoleTxt.setText("Iniciando batalha contra " + opponent.getDisplayName());
        console.addChild(consoleTxt);

        addChild(console);
        addChild(playerIcon);
        addChild(opponentIcon);
        addChild(playerPokemonIcon);
        addChild(opponentPokemonIcon);
    }

    private void onExit()
    {
        
    }
}
