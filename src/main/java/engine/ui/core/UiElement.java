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

    private final List<UiElement> toAdd = new ArrayList<>();
    private final List<UiElement> toRemove = new ArrayList<>();
    private boolean isPendingClear = false;

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

    @Override protected final Renderer createSwingRenderer() { return null; }
    public UiTransform getUiTransform() { return (UiTransform) transform; }
    public boolean isMouseOver() { return isMouseOver; }
    protected void beforeDraw(Graphics2D g2d) { }
    protected abstract void drawSelf(Graphics2D g2d);

    private void applyPendingModifications() 
    {
        if (isPendingClear) {
            child.clear();
            isPendingClear = false;
        }

        if (!toRemove.isEmpty()) {
            child.removeAll(toRemove);
            toRemove.clear();
        }

        if (!toAdd.isEmpty()) {
            child.addAll(toAdd);
            toAdd.clear();
        }
    }

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
        applyPendingModifications();
        AffineTransform oldTransform = g2d.getTransform();

        beforeDraw(g2d);

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

    public void removeChild(UiElement element) { toRemove.add(element); }
    public void addChild(UiElement element) { toAdd.add(element); element.parent = this; }
    public <T extends UiElement> List<T> getAllChildrenFromType(Class<? extends T> type)
    {
        applyPendingModifications();
        List<T> out = new ArrayList<>();
        for (var el : child)
            if (type.isInstance(el))
                out.add(type.cast(el));

        return out;
    }

    @Override public void setup() { applyPendingModifications(); for (var el : child) { el.setup(); renderer = null; } }

    @Override
    public void update()
    {
        applyPendingModifications();
        Vec2d mousePos = Input.getMousePos();
        boolean nowOver = getBounds().contains(mousePos.x(), mousePos.y());

        if (isVisible && this instanceof OnMouseEnterListener listener && nowOver && !isMouseOver)
            listener.onPointerEnter();
        if (isVisible && this instanceof OnMouseExitListener listener && !nowOver && isMouseOver)
            listener.onPointerExit();

        isMouseOver = nowOver;
        boolean condition = isVisible && isMouseOver && Input.getMouseButtonDown(0);

        if (condition && this instanceof OnMouseClickListener listener && !GamePanel.getInstance().hasClicked())
        {
            listener.onPointerClick();
            GamePanel.getInstance().setClicked();
        }

        if (isVisible)
            for (var el : child) el.update();
    }

    public List<UiElement> getAllChildren() { applyPendingModifications(); return new ArrayList<>(child); }
    public Set<UiElement> getAllChildrenSet() { applyPendingModifications(); return child; }
    public UiElement getParent() { return parent; }

    public void removeAllChildren() 
    {
        isPendingClear = true;
        toAdd.clear();
        toRemove.clear();
    }

    public UiElement getChild(int index)
    {
        applyPendingModifications();
        int i = 0;
        for (var el : child)
            if (i++ == index) return el;

        throw new IndexOutOfBoundsException(this + " does not has " + index + " children");
    }
}
