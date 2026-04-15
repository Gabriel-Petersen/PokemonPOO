package examples;

import engine.core.GamePanel;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiInputText;
import engine.ui.elements.UiProgressBar;
import engine.ui.elements.UiText;
import java.awt.Color;
import java.awt.Font;

public final class UiElementExamples
{
    private UiElementExamples() { }

    public static void runExample()
    {
        GamePanel gamePanel = GamePanel.getInstance();

        // UiImage(width, height, color): painel simples de HUD sem sprite.
        UiImage panel = new UiImage(360, 190, new Color(28, 32, 42));
        panel.getTransform().setPosition(20, 20);
        gamePanel.addElement(panel);

        // UiText(text): rotulo para titulo e feedback de estado.
        UiText title = new UiText("UI Basica");
        title.setFont("Arial", Font.BOLD, 18);
        title.setColor(Color.WHITE);
        title.getTransform().setPosition(12, 24);
        panel.addChild(title);

        // UiProgressBar(direction): barra horizontal para vida/energia.
        UiProgressBar hpBar = new UiProgressBar(UiProgressBar.Direction.LEFT2RIGHT);
        hpBar.getTransform().setPosition(12, 42);
        hpBar.getTransform().setScale(240, 20);
        hpBar.setBackgroundColor(new Color(60, 60, 60));
        hpBar.setFillColor(new Color(80, 190, 90));
        hpBar.setProgress(0.60);
        panel.addChild(hpBar);

        UiText hpLabel = new UiText("HP: 60%");
        hpLabel.setColor(Color.WHITE);
        hpLabel.getTransform().setPosition(262, 57);
        panel.addChild(hpLabel);

        // UiInputText(callback): recebe texto e atualiza UI no Enter.
        UiInputText nameInput = new UiInputText(text -> {
            String name = text == null ? "" : text.trim();
            title.setText(name.isEmpty() ? "UI Basica" : "Jogador: " + name);
        });
        nameInput.getTransform().setPosition(12, 82);
        nameInput.getTransform().setScale(240, 30);
        panel.addChild(nameInput);

        // Segunda UiImage: retrato/status visual simples, alterado dinamicamente.
        UiImage statusBox = new UiImage(76, 76, new Color(95, 130, 200));
        statusBox.getTransform().setPosition(270, 82);
        panel.addChild(statusBox);

        final double[] hp = { 0.60 };

        // UiButton(label, action): acao imediata
        UiButton damageButton = new UiButton("Levar dano", () -> {
            hp[0] = Math.max(0.0, hp[0] - 0.10);
            hpBar.setProgress(hp[0]);
            hpLabel.setText("HP: " + (int) Math.round(hp[0] * 100) + "%");
            statusBox.setColor(new Color(190, 85, 85));
        });
        damageButton.getTransform().setPosition(12, 130);
        damageButton.getTransform().setScale(116, 32);
        panel.addChild(damageButton);

        UiButton healButton = new UiButton("Curar", () -> {
            hp[0] = Math.min(1.0, hp[0] + 0.10);
            hpBar.setProgress(hp[0]);
            hpLabel.setText("HP: " + (int) Math.round(hp[0] * 100) + "%");
            statusBox.setColor(new Color(95, 130, 200));
        });
        healButton.getTransform().setPosition(136, 130);
        healButton.getTransform().setScale(116, 32);
        panel.addChild(healButton);

        damageButton.setBackgroundColor(new Color(190, 90, 90));
        healButton.setBackgroundColor(new Color(80, 150, 90));

        damageButton.setForegroundColor(new Color(245, 245, 245));
        healButton.setForegroundColor(new Color(245, 245, 245));
    }
}
