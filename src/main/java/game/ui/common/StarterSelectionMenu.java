package game.ui.common;

import engine.core.GamePanel;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiInputText;
import engine.ui.elements.UiText;
import game.loader.SpecieRegister;

import java.awt.Color;
import java.util.function.BiConsumer;

public class StarterSelectionMenu extends UiImage {
    private final BiConsumer<Integer, String> onStarterSelected;

    public StarterSelectionMenu(BiConsumer<Integer, String> onStarterSelected) 
    {
        super(700, 400, new Color(20, 24, 30));
        this.onStarterSelected = onStarterSelected;
        getUiTransform().setAnchor(Anchor.CENTER);
        setLayer(120);

        UiText title = new UiText("Escolha o seu Pokémon Inicial:");
        title.setColor(Color.WHITE);
        title.setFont("Arial", 1, 24);
        title.getUiTransform().setAnchor(Anchor.CENTER);
        title.getUiTransform().setPosition(0, -130);
        addChild(title);

        StarterCard grassCard = new StarterCard("Bulbassauro", "front_sprites/front_pokemon_000.png", () -> select(1));
        grassCard.getUiTransform().setPosition(-200, 20);
        addChild(grassCard);

        StarterCard fireCard = new StarterCard("Charmander", "front_sprites/front_pokemon_003.png", () -> select(4));
        fireCard.getUiTransform().setPosition(0, 20);
        addChild(fireCard);

        StarterCard waterCard = new StarterCard("Squirtle", "front_sprites/front_pokemon_006.png", () -> select(7));
        waterCard.getUiTransform().setPosition(200, 20);
        addChild(waterCard);
    }

    private void select(int specieId) 
    {
        removeAllChildren(); 

        var specie = SpecieRegister.getSpecie(specieId);

        UiText nicknameLabel = new UiText("Deseja dar um apelido para seu " + specie.getName() + "? (ENTER para pular)");
        nicknameLabel.setColor(Color.WHITE);
        nicknameLabel.setFont("Arial", 1, 16);
        nicknameLabel.getUiTransform().setAnchor(Anchor.CENTER);
        nicknameLabel.getUiTransform().setPosition(0, -30);
        addChild(nicknameLabel);

        UiInputText nicknameInput = new UiInputText(text -> {
            String nickname = text.trim();
            String finalName = nickname.isEmpty() ? null : nickname; 
            
            hide();
            if (onStarterSelected != null) {
                this.onStarterSelectedWithNickname(specieId, finalName);
            }
        });
        nicknameInput.getTransform().setScale(300, 35);
        nicknameInput.getUiTransform().setAnchor(Anchor.CENTER);
        nicknameInput.getUiTransform().setPosition(0, 20);
        addChild(nicknameInput);
    }

    private void onStarterSelectedWithNickname(int specieId, String nickname) {
        if (onStarterSelected != null) {
            onStarterSelected.accept(specieId, nickname);
        }
    }

    public void show() {
        GamePanel.getInstance().addElement(this);
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
        GamePanel.getInstance().removeElement(this);
    }

    private static class StarterCard extends UiImage {
        
        public StarterCard(String name, String spritePath, Runnable onSelectAction) 
        {
            super(160, 220, new Color(32, 38, 46));
            getUiTransform().setAnchor(Anchor.CENTER);

            UiImage pokemonSprite = new UiImage(spritePath);
            pokemonSprite.getTransform().setScale(96, 96);
            pokemonSprite.getUiTransform().setAnchor(Anchor.CENTER);
            pokemonSprite.getUiTransform().setPosition(0, -45);
            addChild(pokemonSprite);

            UiText pokemonName = new UiText(name);
            pokemonName.setColor(Color.WHITE);
            pokemonName.setFont("Arial", 1, 14);
            pokemonName.getUiTransform().setAnchor(Anchor.CENTER);
            pokemonName.getUiTransform().setPosition(0, 20);
            addChild(pokemonName);

            UiButton selectBtn = new UiButton("Escolher", onSelectAction);
            selectBtn.getTransform().setScale(130, 35);
            selectBtn.getUiTransform().setAnchor(Anchor.CENTER);
            selectBtn.getUiTransform().setPosition(0, 70);
            addChild(selectBtn);
        }
    }
}