package game.ui.battle;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;

import game.battle.BattleSession;
import game.battle.Trainer;
import game.battle.actions.FleeAction;
import game.battle.actions.ItemAction;
import game.battle.actions.MoveAction;
import game.creature.Pokemon;
import game.creature.move.Move;
import game.itemsystem.Inventory;
import game.itemsystem.items.CaptureItem;
import game.ui.common.InventoryUiPanel;

import java.awt.Color;
import java.awt.Font;

public class BattleHud extends UiImage 
{
    private final BattleSession session;

    private PokemonInBattleIcon playerPokemonIcon;
    private PokemonInBattleIcon opponentPokemonIcon;
    
    private TrainerUiIcon playerIcon;
    private TrainerUiIcon opponentIcon;
    private InventoryUiPanel battleInventoryPanel;

    private UiText consoleTxt;
    
    private UiImage actionMenu;
    private UiButton fightBtn;
    private UiButton bagBtn;
    private UiButton pokemonBtn;
    private UiButton runBtn;
    private UiButton cancelMoveBtn;

    private UiImage moveMenu;
    private final UiButton[] moveButtons = new UiButton[4];

    public BattleHud(BattleSession session, int sizeX, int sizeY, Color color, Trainer player, Trainer opponent)
    {
        super(sizeX, sizeY, color);
        this.session = session;
        getUiTransform().setAnchor(Anchor.CENTER);
        getUiTransform().setPosition(0, 0);
        setVisible(false);

        setupArena(player, opponent);
        setupBottomBar(opponent);
        setupInventory(player.getInventory());
    }

    private void setupArena(Trainer player, Trainer opponent) 
    {
        playerIcon = new TrainerUiIcon(player);
        opponentIcon = new TrainerUiIcon(opponent);
        
        playerPokemonIcon = PokemonInBattleIcon.buildPlayerIcon(player.getCurrent());
        opponentPokemonIcon = PokemonInBattleIcon.buildNpcIcon(opponent.getCurrent());

        playerIcon.getTransform().setScale(160, 160);
        opponentIcon.getTransform().setScale(160, 160);


        opponentIcon.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        opponentIcon.getUiTransform().setPosition(-40, 50);

        opponentPokemonIcon.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        opponentPokemonIcon.getUiTransform().setPosition(-200, 50);

        playerIcon.getUiTransform().setAnchor(Anchor.BOTTOM_LEFT);
        playerIcon.getUiTransform().setPosition(60, -220);

        playerPokemonIcon.getUiTransform().setAnchor(Anchor.BOTTOM_LEFT);
        playerPokemonIcon.getUiTransform().setPosition(240, -220); 

        addChild(opponentIcon);
        addChild(opponentPokemonIcon);
        addChild(playerIcon);
        addChild(playerPokemonIcon);

        setPokemonStageVisible(true);
        setTrainerStageVisible(true);
    }

    private void setupInventory(Inventory playerInventory)
    {
        battleInventoryPanel = new InventoryUiPanel(480, 320, new Color(40, 46, 58), playerInventory);
        battleInventoryPanel.getUiTransform().setAnchor(Anchor.CENTER);
        battleInventoryPanel.getUiTransform().setPosition(0, -40);
        battleInventoryPanel.setVisible(false);
        addChild(battleInventoryPanel);
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
        UiImage bottomBar = new UiImage(760, 150, new Color(58, 63, 76));
        bottomBar.getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
        bottomBar.getUiTransform().setPosition(0, -20);
        addChild(bottomBar);

        UiImage consoleBox = new UiImage(440, 120, new Color(24, 26, 32));
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

        moveMenu = new UiImage(260, 120, new Color(62, 73, 94));
        moveMenu.getUiTransform().setAnchor(Anchor.CENTER_RIGHT);
        moveMenu.getUiTransform().setPosition(-15, 0);
        moveMenu.setVisible(false);
        bottomBar.addChild(moveMenu);

        cancelMoveBtn = new UiButton("VOLTAR", () -> {
            moveMenu.setVisible(false);
            cancelMoveBtn.setVisible(false);
            actionMenu.setVisible(true);
            consoleTxt.setText("O que " + playerIcon.getSource().getCurrent().getSpecie().getName() + " fará?");
        });

        cancelMoveBtn.getTransform().setScale(70, 24);
        cancelMoveBtn.getUiTransform().setAnchor(Anchor.CENTER_RIGHT);
        cancelMoveBtn.getUiTransform().setPosition(-15, -77);
        cancelMoveBtn.setVisible(false);

        configureButtonTheme(cancelMoveBtn, Color.WHITE, Color.WHITE);
        for (var txt : cancelMoveBtn.getAllChildrenFromType(UiText.class)) {
            txt.setFont("Arial", Font.BOLD, 11);
        }
        bottomBar.addChild(cancelMoveBtn);

        setupActionButtons();
        setupMoveButtons();
    }

    private void setupMoveButtons()
    {
        int btnW = 115; int btnH = 36;

        Anchor[] anchors = { Anchor.TOP_LEFT, Anchor.TOP_RIGHT, Anchor.BOTTOM_LEFT, Anchor.BOTTOM_RIGHT };
        int[] posX = { 12, -12, 12, -12 };
        int[] posY = { 12, 12, -12, -12 };

        for (int i = 0; i < 4; i++)
        {
            final int slot = i;

            moveButtons[i] = new UiButton("", () -> onMoveButtonSelected(slot)) {
                @Override
                public void onPointerClick() {
                    Pokemon active = playerIcon.getSource().getCurrent();
                    Move[] moves = active.getMoves();

                    if (slot < moves.length && moves[slot] != null && moves[slot].canUse(session.getBattle().getContext()))
                        super.onPointerClick();
                }
            };

            moveButtons[i].getTransform().setScale(btnW, btnH);
            moveButtons[i].getUiTransform().setAnchor(anchors[i]);
            moveButtons[i].getUiTransform().setPosition(posX[i], posY[i]);
            configureButtonTheme(moveButtons[i], new Color(48, 56, 74), Color.WHITE);
            moveMenu.addChild(moveButtons[i]);
        }
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

    public void setActionButtonsEnabled(boolean enabled)
    {
        actionMenu.setVisible(enabled);
        moveMenu.setVisible(false);
        cancelMoveBtn.setVisible(false);
        fightBtn.setVisible(enabled);
        bagBtn.setVisible(enabled);
        pokemonBtn.setVisible(enabled);
        runBtn.setVisible(enabled);
    }

    public void updateActivePokemonSprites() 
    {
        removeChild(playerPokemonIcon);
        removeChild(opponentPokemonIcon);

        playerPokemonIcon = PokemonInBattleIcon.buildPlayerIcon(playerIcon.getSource().getCurrent());
        opponentPokemonIcon = PokemonInBattleIcon.buildNpcIcon(opponentIcon.getSource().getCurrent());

        opponentPokemonIcon.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        opponentPokemonIcon.getUiTransform().setPosition(-200, 50);

        playerPokemonIcon.getUiTransform().setAnchor(Anchor.BOTTOM_LEFT);
        playerPokemonIcon.getUiTransform().setPosition(240, -220);

        addChild(playerPokemonIcon);
        addChild(opponentPokemonIcon);
        
        setPokemonStageVisible(true);
    }

    private void updateMoveMenuData()
    {
        Pokemon active = playerIcon.getSource().getCurrent();
        Move[] moves = active.getMoves();

        for (int i = 0; i < 4; i++) 
        {
            if (i < moves.length && moves[i] != null) 
            {
                Move move = moves[i];
                moveButtons[i].getAllChildrenFromType(UiText.class).getFirst().setText(move.getName() + " (" + move.getPp() + ")");
                moveButtons[i].setBackgroundColor(new Color(43, 81, 56)); // Verde para golpes utilizáveis
                moveButtons[i].setVisible(true);
            } 
            else 
            {
                moveButtons[i].getAllChildrenFromType(UiText.class).getFirst().setText("-");
                moveButtons[i].setBackgroundColor(new Color(28, 32, 41)); // Escuro para slots vazios
            }
        }
    }

    public PokemonInBattleIcon getPlayerPokemonIcon() { return playerPokemonIcon; }
    public PokemonInBattleIcon getOpponentPokemonIcon() { return opponentPokemonIcon; }
    public TrainerUiIcon getPlayerIcon() { return playerIcon; }
    public TrainerUiIcon getOpponentIcon() { return opponentIcon; }

    private void onFightClick()
    {
        if (battleInventoryPanel.isVisible()) battleInventoryPanel.setVisible(false);

        updateMoveMenuData();

        actionMenu.setVisible(false);
        moveMenu.setVisible(true);
        cancelMoveBtn.setVisible(true);

        consoleTxt.setText("O que " + playerPokemonIcon.getSource().getSpecie().getName() + " fará?");
    }

    private void onMoveButtonSelected(int slot)
    {
        Pokemon user = playerIcon.getSource().getCurrent();
        Move[] moves = user.getMoves();

        if (slot >= moves.length || moves[slot] == null) return;

        Pokemon target = opponentIcon.getSource().getCurrent();
        Move move = moves[slot];

        MoveAction action = new MoveAction(move, target, user, playerIcon.getSource());

        moveMenu.setVisible(false);
        cancelMoveBtn.setVisible(false); // Esconde o botão pequeno
        actionMenu.setVisible(true);

        session.submitPlayerAction(action);
    }

    private void onBagClick() 
    {
        System.out.println("Bag Clicked");
        if (battleInventoryPanel.isVisible()) 
        {
            battleInventoryPanel.setVisible(false);
            return;
        } 

        battleInventoryPanel.setVisible(true);
        
        battleInventoryPanel.setupContext(true, (item) -> {
            Pokemon target = item instanceof CaptureItem ? 
                opponentIcon.getSource().getCurrent() :
                playerIcon.getSource().getCurrent();
            
            if (!item.canUse(target)) {
                consoleTxt.setText("Não é o momento de usar isso!");
                return false;
            }

            ItemAction action = new ItemAction(item, target, playerIcon.getSource());
    
            if (session != null) 
                session.submitPlayerAction(action); 

            return true;
        });
        
    }

    private void onPokemonClick() {
        System.out.println("Pokemon Clicked");
        if(battleInventoryPanel.isVisible()) battleInventoryPanel.setVisible(false);
    }
    
    private void onRunClick() 
    {
        System.out.println("Run Clicked");
        if (battleInventoryPanel.isVisible()) 
            battleInventoryPanel.setVisible(false);
        session.submitPlayerAction(FleeAction.getInstance(playerIcon.getSource()));
    }

    public UiText getConsole() { return consoleTxt; }
}
