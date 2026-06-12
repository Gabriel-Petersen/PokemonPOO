package game.ui.common;

import java.awt.Color;
import java.awt.Font;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.creature.move.Move;

public class MoveDetailsTooltip extends UiImage 
{
    private final UiText line1;
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
        
        line1.setText(String.format("Poder: %s   |   Precisão: %d%%   |   Tipo: %s", 
            powerStr, accPercent, move.getElementType()));
        
        line2.setText(String.format("PP: %d / %d   |   Categoria: %s", 
            move.getPp(), Move.getMaxPp(), move.getCategory()));
        
        line1.setColor(Color.WHITE);
        line2.setColor(Color.WHITE);
    }

    public void clear() {
        line1.setText("Selecione um movimento para detalhes.");
        line2.setText("");
        line1.setColor(new Color(170, 180, 195));
    }
}
