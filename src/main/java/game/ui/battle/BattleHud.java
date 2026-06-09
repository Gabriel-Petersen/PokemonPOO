package game.ui.battle;

import java.awt.Color;
import java.awt.Font;

import engine.events.EventScheduler;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.battle.Trainer;

public class BattleHud extends UiImage 
{
    private PokemonInBattleIcon playerPokemonIcon;
    private PokemonInBattleIcon opponentPokemonIcon;
    
    private TrainerUiIcon playerIcon;
    private TrainerUiIcon opponentIcon;
    
    private UiImage bottomBar;
    private UiImage consoleBox;
    private UiText consoleTxt;
    
    private UiImage actionMenu;
    private UiButton fightBtn;
    private UiButton bagBtn;
    private UiButton pokemonBtn;
    private UiButton runBtn;

    public BattleHud(int sizeX, int sizeY, Color color, EventScheduler scheduler, Trainer player, Trainer opponent) 
    {
        super(sizeX, sizeY, color);
        getUiTransform().setAnchor(Anchor.CENTER);
        getUiTransform().setPosition(0, 0);
        setVisible(false);

        setupArena(player, opponent);
        setupBottomBar(opponent);
    }

    private void setupArena(Trainer player, Trainer opponent) 
    {
        playerIcon = new TrainerUiIcon(player);
        opponentIcon = new TrainerUiIcon(opponent);
        
        playerPokemonIcon = PokemonInBattleIcon.buildPlayerIcon(player.getTeam().getActiveMember());
        opponentPokemonIcon = PokemonInBattleIcon.buildNpcIcon(opponent.getTeam().getActiveMember());

        playerIcon.getTransform().setScale(160, 160);
        opponentIcon.getTransform().setScale(160, 160);

        opponentIcon.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        opponentIcon.getUiTransform().setPosition(-60, 50);

        opponentPokemonIcon.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        opponentPokemonIcon.getUiTransform().setPosition(-60, 50);

        playerIcon.getUiTransform().setAnchor(Anchor.BOTTOM_LEFT);
        playerIcon.getUiTransform().setPosition(60, -220);

        playerPokemonIcon.getUiTransform().setAnchor(Anchor.BOTTOM_LEFT);
        playerPokemonIcon.getUiTransform().setPosition(60, -220);

        addChild(opponentIcon);
        addChild(opponentPokemonIcon);
        addChild(playerIcon);
        addChild(playerPokemonIcon);

        setPokemonStageVisible(false);
        setTrainerStageVisible(true);
    }

    public void setPokemonStageVisible(boolean visible) {
        playerPokemonIcon.setVisible(visible);
        opponentPokemonIcon.setVisible(visible);
    }

    public void setTrainerStageVisible(boolean visible) {
        playerIcon.setVisible(visible);
        opponentIcon.setVisible(visible);
    }

    private void setupBottomBar(Trainer opponent) 
    {
        bottomBar = new UiImage(760, 150, new Color(58, 63, 76));
        bottomBar.getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
        bottomBar.getUiTransform().setPosition(0, -20);
        addChild(bottomBar);

        consoleBox = new UiImage(440, 120, new Color(24, 26, 32));
        consoleBox.getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        consoleBox.getUiTransform().setPosition(15, 0);
        bottomBar.addChild(consoleBox);

        consoleTxt = new UiText("Iniciando batalha contra " + opponent.getDisplayName() + "!");
        consoleTxt.setFont("Arial", Font.BOLD, 16);
        consoleTxt.setColor(Color.WHITE);
        consoleTxt.getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        consoleTxt.getUiTransform().setPosition(20, 0);
        consoleBox.addChild(consoleTxt);

        actionMenu = new UiImage(260, 120, new Color(77, 105, 157));
        actionMenu.getUiTransform().setAnchor(Anchor.CENTER_RIGHT);
        actionMenu.getUiTransform().setPosition(-15, 0);
        bottomBar.addChild(actionMenu);

        setupActionButtons();
    }

    private void setupActionButtons() 
    {
        fightBtn = new UiButton("FIGHT", this::onFightClick);
        bagBtn = new UiButton("BAG", this::onBagClick);
        pokemonBtn = new UiButton("POKÉMON", this::onPokemonClick);
        runBtn = new UiButton("RUN", this::onRunClick);

        int btnW = 110; int btnH = 45;
        fightBtn.getTransform().setScale(btnW, btnH);
        bagBtn.getTransform().setScale(btnW, btnH);
        pokemonBtn.getTransform().setScale(btnW, btnH);
        runBtn.getTransform().setScale(btnW, btnH);

        fightBtn.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        fightBtn.getUiTransform().setPosition(15, 12);
        bagBtn.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        bagBtn.getUiTransform().setPosition(-15, 12);
        pokemonBtn.getUiTransform().setAnchor(Anchor.BOTTOM_LEFT);
        pokemonBtn.getUiTransform().setPosition(15, -12);
        runBtn.getUiTransform().setAnchor(Anchor.BOTTOM_RIGHT);
        runBtn.getUiTransform().setPosition(-15, -12);

        configureButtonTheme(fightBtn, new Color(43, 56, 81), new Color(238, 242, 255));
        configureButtonTheme(bagBtn, new Color(43, 56, 81), new Color(238, 242, 255));
        configureButtonTheme(pokemonBtn, new Color(43, 56, 81), new Color(238, 242, 255));
        configureButtonTheme(runBtn, Color.red, Color.WHITE);

        actionMenu.addChild(fightBtn);
        actionMenu.addChild(bagBtn);
        actionMenu.addChild(pokemonBtn);
        actionMenu.addChild(runBtn);
    }

    private void configureButtonTheme(UiButton btn, Color bg, Color fg) 
    {
        btn.setBackgroundColor(bg);
        btn.setForegroundColor(fg);
        
        for (var txt : btn.getAllChildrenFromType(UiText.class))
            txt.setFont("Arial", Font.BOLD, 14);
    }

    public void updateConsoleMessage(String message) {
        if (this.consoleTxt == null)
            consoleTxt = new UiText(message);
        else
            this.consoleTxt.setText(message);
    }

    private void onFightClick() 
    {
        setTrainerStageVisible(false);
        setPokemonStageVisible(true);
        consoleTxt.setText("Vai, " + playerPokemonIcon.getSource().getSpecie().getName() + "!");
    }

    private void onBagClick() {}
    private void onPokemonClick() {}
    private void onRunClick() {}
}
