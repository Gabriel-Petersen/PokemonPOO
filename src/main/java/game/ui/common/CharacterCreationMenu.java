package game.ui.common;

import engine.core.GamePanel;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiInputText;
import engine.ui.elements.UiText;

import java.awt.Color;
import java.util.function.Consumer;

public class CharacterCreationMenu extends UiImage {
    private final Consumer<String> onNameConfirmed;

    public CharacterCreationMenu(Consumer<String> onNameConfirmed) 
    {
        super(700, 400, new Color(20, 24, 30));
        this.onNameConfirmed = onNameConfirmed;
        getUiTransform().setAnchor(Anchor.CENTER);
        setLayer(120);

        UiText label = new UiText("Digite o seu nome de Treinador e aperte ENTER:");
        label.setColor(Color.WHITE);
        label.setFont("Arial", 1, 16);
        label.getUiTransform().setAnchor(Anchor.CENTER);
        label.getUiTransform().setPosition(0, -40);
        addChild(label);

        UiInputText nameInput = new UiInputText(text -> {
            String typedName = text.trim();
            if (!typedName.isEmpty()) {
                hide();
                if (this.onNameConfirmed != null) {
                    this.onNameConfirmed.accept(typedName);
                }
            }
        });
        
        nameInput.getTransform().setScale(300, 35);
        nameInput.getUiTransform().setAnchor(Anchor.CENTER);
        nameInput.getUiTransform().setPosition(0, 10);
        addChild(nameInput);
    }

    public void show() {
        GamePanel.getInstance().addElement(this);
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
        GamePanel.getInstance().removeElement(this);
    }
}