package game.ui.player;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.player.Player;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

public class InfoPauseTab extends PauseTab 
{
    public InfoPauseTab(int sizeX, int sizeY, Color color, Player player) 
    {
        super(PauseTabType.INFO, sizeX, sizeY, color, player);
        setupVisuals();
    }

    private void setupVisuals()
    {
        getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        getUiTransform().setPosition(80, 0);
        setVisible(false);

        var playerMeta = player.getMetadata();

        UiImage titleBox = new UiImage(400, 60, new Color(77, 105, 157));
        titleBox.getUiTransform().setPosition(25, 20);
        addChild(titleBox);

        UiText titleText = new UiText("Player Info");
        titleText.setFont("Arial", Font.BOLD, 24);
        titleText.setColor(Color.WHITE);
        titleText.getUiTransform().setAnchor(Anchor.CENTER);
        titleText.getUiTransform().setPosition(0, 0);
        titleBox.addChild(titleText);

        UiImage infoBox = new UiImage(400, 200, new Color(58, 63, 76));
        infoBox.getUiTransform().setPosition(25, 100);
        addChild(infoBox);

        String playerName = playerMeta.getName() != null ? playerMeta.getName() : "Unnamed";
        UiText nameLabel = new UiText("Name: " + playerName);
        nameLabel.setFont("Arial", Font.PLAIN, 16);
        nameLabel.setColor(Color.WHITE);
        nameLabel.getUiTransform().setPosition(20, 20);
        infoBox.addChild(nameLabel);

        UiText moneyLabel = new UiText("Money: $" + playerMeta.getMoney());
        moneyLabel.setFont("Arial", Font.PLAIN, 16);
        moneyLabel.setColor(Color.WHITE);
        moneyLabel.getUiTransform().setPosition(20, 60);
        infoBox.addChild(moneyLabel);

        UiText winsLabel = new UiText("Enemies Defeated: " + playerMeta.getEnemiesWinned());
        winsLabel.setFont("Arial", Font.PLAIN, 16);
        winsLabel.setColor(Color.WHITE);
        winsLabel.getUiTransform().setPosition(20, 100);
        infoBox.addChild(winsLabel);

        String saveCreatedAt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            .format(playerMeta.getGameStartTime());
        UiText saveCreatedLabel = new UiText("Save Created At: " + saveCreatedAt);
        saveCreatedLabel.setFont("Arial", Font.PLAIN, 16);
        saveCreatedLabel.setColor(Color.WHITE);
        saveCreatedLabel.getUiTransform().setPosition(20, 140);
        infoBox.addChild(saveCreatedLabel);
    }
}
