package game.ui.battle;

import engine.ui.core.UiElement;
import engine.ui.elements.UiInputText;
import game.creature.Pokemon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class NicknameOverlay extends UiElement
{
    private final UiInputText inputText;
    private final Pokemon targetPokemon;
    private Runnable onComplete;
    private final int width;
    private final int height;

    public NicknameOverlay(int width, int height, Pokemon targetPokemon, Runnable onComplete)
    {
        this.width = width;
        this.height = height;
        this.targetPokemon = targetPokemon;
        this.onComplete = onComplete;

        this.inputText = new UiInputText(this::handleNicknameSubmit);

        this.inputText.getTransform().setScale(300, 40);
        this.inputText.getTransform().setPosition(
                (width - 300) / 2.0,
                (height - 40) / 2.0 + 30
        );

        this.inputText.onPointerClick();
    }

    public void setOnComplete(Runnable onComplete) { this.onComplete = onComplete; }

    private void handleNicknameSubmit(String text)
    {
        if (text == null || text.isBlank())
            targetPokemon.setNickname(null);
        else
            targetPokemon.setNickname(text.trim());

        onComplete.run();
    }

    @Override
    public void update()
    {
        super.update();
        inputText.update();
    }

    @Override
    protected void drawSelf(Graphics2D g2d)
    {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, width, height);

        int boxW = 400;
        int boxH = 180;
        int boxX = (width - boxW) / 2;
        int boxY = (height - boxH) / 2;

        g2d.setColor(new Color(40, 44, 52));
        g2d.fillRect(boxX, boxY, boxW, boxH);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawRect(boxX, boxY, boxW, boxH);

        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String msg = "Deseja dar um apelido para " + targetPokemon.getSpecie().getName() + "?";
        int msgWidth = g2d.getFontMetrics().stringWidth(msg);
        g2d.drawString(msg, (width - msgWidth) / 2, boxY + 40);

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.LIGHT_GRAY);
        String subMsg = "(Pressione ENTER para confirmar. Deixe em branco para o nome padrão)";
        int subWidth = g2d.getFontMetrics().stringWidth(subMsg);
        g2d.drawString(subMsg, (width - subWidth) / 2, boxY + 65);

        var backup = g2d.getTransform();
        g2d.translate(inputText.getTransform().getPosition().x(), inputText.getTransform().getPosition().y());
        inputText.drawSelf(g2d);
        g2d.setTransform(backup);
    }
}