package game.ui.common;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.creature.move.Move;
import java.awt.Color;
import java.awt.Font;

public class MoveDetailsTooltip extends UiImage
{
    private final UiText line1;
    private final UiText typeLabelTxt;
    private final UiText typeValueTxt;
    private final UiText line2;

    public MoveDetailsTooltip(int sizeX, int sizeY)
    {
        super(sizeX, sizeY, new Color(26, 30, 38));

        line1 = new UiText("Selecione um movimento para detalhes.");
        line1.setFont("Arial", Font.PLAIN, 12);
        line1.setColor(new Color(170, 180, 195));
        line1.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        line1.getUiTransform().setPosition(12, 12);
        addChild(line1);

        typeLabelTxt = new UiText("   |   Tipo: ");
        typeLabelTxt.setFont("Arial", Font.PLAIN, 12);
        typeLabelTxt.setColor(Color.WHITE);
        typeLabelTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        typeLabelTxt.getUiTransform().setPosition(175, 12);
        typeLabelTxt.setVisible(false);
        addChild(typeLabelTxt);

        typeValueTxt = new UiText("");
        typeValueTxt.setFont("Arial", Font.BOLD, 12);
        typeValueTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        typeValueTxt.getUiTransform().setPosition(232, 12);
        typeValueTxt.setVisible(false);
        addChild(typeValueTxt);

        line2 = new UiText("");
        line2.setFont("Arial", Font.PLAIN, 12);
        line2.setColor(new Color(170, 180, 195));
        line2.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        line2.getUiTransform().setPosition(12, 34);
        addChild(line2);
    }

    public void displayMove(Move move)
    {
        if (move == null) {
            clear();
            return;
        }

        int accPercent = (int) (100 * move.getAccuracy());
        String powerStr = move.getPower() <= 0 ? "---" : String.valueOf(move.getPower());

        line1.setText(String.format("Poder: %s   |   Precisão: %d%%", powerStr, accPercent));
        line2.setText(String.format("PP: %d / %d   |   Categoria: %s", move.getPp(), Move.getMaxPp(), move.getCategory()));

        typeLabelTxt.setVisible(true);
        typeValueTxt.setVisible(true);
        typeValueTxt.setText(move.getElementType().name());
        typeValueTxt.setColor(move.getElementType().getDisplayColor());

        line1.setColor(Color.WHITE);
        line2.setColor(Color.WHITE);
    }

    public void clear() {
        line1.setText("Selecione um movimento para detalhes.");
        line2.setText("");
        typeLabelTxt.setVisible(false);
        typeValueTxt.setVisible(false);
        line1.setColor(new Color(170, 180, 195));
    }
}