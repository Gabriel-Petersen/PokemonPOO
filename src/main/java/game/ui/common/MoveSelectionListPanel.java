package game.ui.common;

import game.creature.Pokemon;
import game.creature.move.Move;

import java.awt.Color;
import java.util.Collection;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;

public class MoveSelectionListPanel extends UiImage 
{
    private final TeamUiPanel parentPanel;
    private int targetSlot = 0;
    private Pokemon currentPokemon;

    public MoveSelectionListPanel(int sizeX, int sizeY, TeamUiPanel parentPanel) 
    {
        super(sizeX, sizeY / 2, new Color(50, 28, 35, 120));
        this.parentPanel = parentPanel;
        getUiTransform().setAnchor(Anchor.CENTER_TOP);
        
        setVisible(false);
    }

    public void openForSlot(Pokemon pokemon, int slot) 
    {
        this.currentPokemon = pokemon;
        this.targetSlot = slot;
        this.removeAllChildren(); 

        UiButton cancelBtn = new UiButton("Cancelar", () -> setVisible(false));
        cancelBtn.getTransform().setScale(90, 25);
        cancelBtn.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        cancelBtn.getUiTransform().setPosition(-10, 10);
        cancelBtn.setBackgroundColor(new Color(150, 50, 50));
        for (var txt : cancelBtn.getAllChildrenFromType(UiText.class))
            txt.setFont("Arial", 0, 11);
        addChild(cancelBtn);

        Collection<Move> availableMoves = pokemon.getSpecie().resolveMovesForLevel(pokemon.getCurrentLevel());

        int i = 0;
        int startY = 45;
        int btnW = (int) (getTransform().getScale().x() - 50);
        int btnH = 26;

        for (Move move : availableMoves) {
            if (move == null) continue;

            String label = move.getName() + " (PP: " + move.getPp() + ")";
            UiButton moveBtn = new UiButton(label, () -> {
                currentPokemon.replaceMove(targetSlot, move);
                setVisible(false);
                parentPanel.refresh();
            }){
                @Override
                public void onPointerEnter() {
                    super.onPointerEnter();
                    parentPanel.getTooltip().displayMove(move);
                }
            };

            moveBtn.getTransform().setScale(btnW, btnH);
            moveBtn.getUiTransform().setAnchor(Anchor.CENTER_TOP);
            moveBtn.getUiTransform().setPosition(0, startY + (i * (btnH + 6)));
            moveBtn.setBackgroundColor(new Color(48, 54, 66));
            moveBtn.setForegroundColor(Color.WHITE);


            addChild(moveBtn);
            i++;
        }

        setVisible(true);
    }
}