package engine.ui.elements;

import engine.ui.core.UiElement;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.mouselisteners.OnMouseClickListener;
import engine.ui.mouselisteners.OnMouseEnterListener;
import engine.ui.mouselisteners.OnMouseExitListener;
import java.awt.*;

public class UiButton extends UiElement implements OnMouseClickListener, OnMouseEnterListener, OnMouseExitListener
{
    private final Runnable onClickAction;
    private final UiText txtChild;
    private Color mainColor;
    private Color backgroundColor = Color.DARK_GRAY;
    private Color foregroundColor = Color.WHITE;

    public UiButton(String text, Runnable onClick)
    {
        this.onClickAction = onClick;
        this.txtChild = new UiText(text);
        this.txtChild.getUiTransform().setAnchor(Anchor.CENTER);
        this.addChild(txtChild);
        mainColor = foregroundColor;
    }

    public UiButton(Runnable onClick) { this.onClickAction = onClick; txtChild = null; }

    public UiText getTxtChild() { return txtChild; }

    @Override
    protected void drawSelf(Graphics2D g2d)
    {
        g2d.setColor(mainColor);
        g2d.fillRect(0, 0, (int)transform.getScale().x(), (int)transform.getScale().y());

        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, (int)transform.getScale().x(), (int)transform.getScale().y());
    }

    @Override
    public void onPointerClick() {
        if (onClickAction != null) onClickAction.run();
    }

    @Override
    public void onPointerEnter() {
        mainColor = backgroundColor;
    }

    @Override
    public void onPointerExit() {
        mainColor = foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        mainColor = foregroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}