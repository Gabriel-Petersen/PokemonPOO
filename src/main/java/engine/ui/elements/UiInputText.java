package engine.ui.elements;

import engine.input.Input;
import engine.ui.core.OnTextSubmit;
import engine.ui.core.UiElement;
import engine.ui.mouselisteners.OnMouseClickListener;

import java.awt.*;
import java.awt.event.KeyEvent;

public class UiInputText extends UiElement implements OnMouseClickListener
{
    private String content = "";
    private boolean isFocused = false;
    private Color boxColor = Color.WHITE;
    private final OnTextSubmit onTextSubmit;

    public UiInputText(OnTextSubmit submitAction) {
        onTextSubmit = submitAction;
        transform.setScale(10, 10);
    }

    @Override
    public void onPointerClick()
    {
        isFocused = true;
        Input.clearInputString();
    }

    @Override
    public void update()
    {
        super.update();

        if (Input.getMouseButtonDown(0) && !isMouseOver())
            isFocused = false;

        if (isFocused)
        {
            content = Input.getInputString();

            if (Input.getKeyDown(KeyEvent.VK_ENTER))
            {
                isFocused = false;
                onConfirm(content);
            }
        }
    }

    @Override
    protected void drawSelf(Graphics2D g2d)
    {
        g2d.setColor(isFocused ? Color.LIGHT_GRAY : Color.WHITE);
        g2d.fillRect(0, 0, (int)transform.getScale().x(), (int)transform.getScale().y());
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, (int)transform.getScale().x(), (int)transform.getScale().y());


        g2d.drawString(content + (isFocused ? "|" : ""), 5, 20);
    }

    protected void onConfirm(String text) {
        onTextSubmit.submitText(text);
    }

    public String getContent() {
        return content;
    }

    public Color getBoxColor() {
        return boxColor;
    }

    public void setBoxColor(Color boxColor) {
        this.boxColor = boxColor;
    }
}
