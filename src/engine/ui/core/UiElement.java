package engine.ui.core;

import engine.core.GameObject;
import engine.core.GamePanel;
import engine.input.Input;
import engine.math.vectors.MutableVec2d;
import engine.math.vectors.Vec2d;
import engine.rendering.Renderer;
import engine.ui.mouselisteners.OnMouseClickListener;
import engine.ui.mouselisteners.OnMouseEnterListener;
import engine.ui.mouselisteners.OnMouseExitListener;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class UiElement extends GameObject
{
    protected final TreeSet<UiElement> child = new TreeSet<>();
    protected UiElement parent = null;
    private boolean isMouseOver = false;

    private final Rectangle2D bounds = new Rectangle2D.Double();

    protected UiElement() { transform = new UiTransform(); }

    protected Rectangle2D getBounds()
    {
        Vec2d pos = getGlobalPosition();

        bounds.setRect(
                pos.x(),
                pos.y(),
                transform.getScale().x(),
                transform.getScale().y()
        );

        return bounds;
    }

    public UiTransform getUiTransform() { return (UiTransform) transform; }

    @Override
    protected final Renderer createSwingRenderer() {
        return null;
    }

    public boolean isMouseOver() { return isMouseOver; }

    @Override
    public void setLayer(int layer)
    {
        if (parent == null)
            super.setLayer(layer);
        else
        {
            parent.removeChild(this);
            this.layer = (byte) layer;
            parent.addChild(this);
        }
    }

    @Override
    protected void onDraw(Graphics2D g2d)
    {
        AffineTransform oldTransform = g2d.getTransform();

        Vec2d parentSize = (getParent() == null)
                ? new MutableVec2d(GamePanel.getInstance().getWidth(), GamePanel.getInstance().getHeight())
                : getParent().getTransform().getScale();

        Vec2d anchoredPos = getUiTransform().getAnchor().calculate(
                transform.getScale(),
                transform.getPosition(),
                parentSize
        );

        g2d.translate(anchoredPos.x(), anchoredPos.y());

        drawSelf(g2d);

        for (UiElement el : child)
            el.draw(g2d);

        g2d.setTransform(oldTransform);
    }

    protected abstract void drawSelf(Graphics2D g2d);

    public Vec2d getLocalAnchoredPosition()
    {
        Vec2d parentSize = (getParent() == null)
                ? new MutableVec2d(GamePanel.getInstance().getWidth(), GamePanel.getInstance().getHeight())
                : getParent().getTransform().getScale();

        return getUiTransform().getAnchor().calculate(
                transform.getScale(),
                transform.getPosition(),
                parentSize
        );
    }

    public Vec2d getGlobalPosition()
    {
        Vec2d localPos = getLocalAnchoredPosition();
        if (parent == null) return localPos;

        Vec2d pPos = parent.getGlobalPosition();
        return new MutableVec2d(pPos.x() + localPos.x(), pPos.y() + localPos.y());
    }

    public void removeChild(UiElement element) { child.remove(element); }
    public void addChild(UiElement element) { child.add(element); element.parent = this; }
    public <T extends UiElement> List<T> getAllChildrenFromType(Class<? extends T> type)
    {
        List<T> out = new ArrayList<>();
        for (var el : child)
            if (type.isInstance(el))
                out.add(type.cast(el));

        return out;
    }

    @Override
    public void setup() {
        for (var el : child) { el.setup(); renderer = null; }
    }

    @Override
    public void update()
    {
        Vec2d mousePos = Input.getMousePos();
        boolean nowOver = getBounds().contains(mousePos.x(), mousePos.y());

        if (this instanceof OnMouseEnterListener listener && nowOver && !isMouseOver)
            listener.onPointerEnter();
        if (this instanceof OnMouseExitListener listener && !nowOver && isMouseOver)
            listener.onPointerExit();

        isMouseOver = nowOver;

        if (this instanceof OnMouseClickListener listener && isMouseOver && Input.getMouseButtonDown(0))
            listener.onPointerClick();

        for (var el : child) el.update();
    }

    public List<UiElement> getAllChildren() { return new ArrayList<>(child); }
    public Set<UiElement> getAllChildrenSet() { return child; }

    public UiElement getParent() { return parent; }

    public UiElement getChild(int index)
    {
        int i = 0;
        for (var el : child)
            if (i++ == index) return el;

        throw new IndexOutOfBoundsException(this + " does not has " + index + " children");
    }
}
