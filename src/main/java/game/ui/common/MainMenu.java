package game.ui.common;

import engine.core.GamePanel;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import java.awt.Color;

public class MainMenu extends UiImage {
    private static final MainMenu INSTANCE = new MainMenu();
    private boolean isAdded = false;
    private Runnable onStart;

    private MainMenu() {
        super(700, 400, new Color(20, 24, 30));
        getUiTransform().setAnchor(Anchor.CENTER);
        setVisible(true);

        UiImage logo = new UiImage("scenario/Logo_POOkemon.png");
        logo.getTransform().setScale(560, 160);
        logo.getUiTransform().setAnchor(Anchor.CENTER);
        logo.getUiTransform().setPosition(logo.getUiTransform().getPosition().add(0, -120));
        addChild(logo);

        UiButton start = new UiButton("Iniciar", () -> {
            if (onStart != null) onStart.run();
        });
        start.getTransform().setScale(220, 50);
        start.getUiTransform().setAnchor(Anchor.CENTER);
        start.getUiTransform().setPosition(start.getUiTransform().getPosition().add(0, -20));
        addChild(start);

        UiButton exit = new UiButton("Sair", () -> System.exit(0));
        exit.getTransform().setScale(220, 50);
        exit.getUiTransform().setAnchor(Anchor.CENTER);
        exit.getUiTransform().setPosition(exit.getUiTransform().getPosition().add(0, 40));
        addChild(exit);

        setLayer(120);
    }

    public static MainMenu getInstance() { return INSTANCE; }

    public void show() {
        if (!isAdded) {
            GamePanel.getInstance().addElement(this);
            isAdded = true;
        }
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
        GamePanel.getInstance().removeElement(this);
        isAdded = false;
    }

    public void setOnStart(Runnable r) { this.onStart = r; }
}
