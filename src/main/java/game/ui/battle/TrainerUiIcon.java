package game.ui.battle;

import java.awt.Color;

import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.battle.Trainer;

public class TrainerUiIcon extends UiImage 
{
    private UiText nameText;
    private Trainer source;

    public TrainerUiIcon(Trainer source) {
        super(source.getOnBattleSprite());
        this.source = source;
        nameText = new UiText(source.getDisplayName());
        nameText.setColor(Color.white);
        addChild(nameText);
    }

    public Trainer getSource() { return source; }
    public UiText getNameText() { return nameText; }
}
