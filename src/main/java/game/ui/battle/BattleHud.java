package game.ui.battle;

import java.awt.Color;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;

public class BattleHud extends UiImage 
{
    private final UiButton exitButton = new UiButton("Exit", () -> onExit());

    public BattleHud(int sizeX, int sizeY, Color color) {
        super(sizeX, sizeY, color);
        getUiTransform().setAnchor(Anchor.CENTER);
        addChild(exitButton);
    }

    private void onExit()
    {
        
    }
}
