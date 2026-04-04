package engine.core;

import engine.events.EventScheduler;
import engine.input.Input;
import engine.lifecycle.Renderable;
import engine.lifecycle.Updatable;
import engine.ui.core.UiElement;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GamePanel extends JPanel
{
    private static final GamePanel INSTANCE = new GamePanel();
    private final Set<Renderable> scene = new TreeSet<>();
    private final Set<UiElement> uiElements = new TreeSet<>();
    private final List<Updatable> logics = new ArrayList<>();

    private final List<Renderable> toAddRend = new ArrayList<>();
    private final List<Renderable> toRemoveRend = new ArrayList<>();
    private final List<Updatable> toAddUp = new ArrayList<>();
    private final List<Updatable> toRemoveUp = new ArrayList<>();

    private final Camera cam = new Camera();
    private final AffineTransform camView = new AffineTransform();

    private final List<EventScheduler> eventSchedulers = new ArrayList<>();

    private double deltaTime = 0;

    public GamePanel()
    {
        addKeyListener(Input.keyListener);
        addMouseListener(Input.mouseListener);
        addMouseMotionListener(Input.mouseListener);
        setFocusable(true);
        requestFocusInWindow();
        setPreferredSize(new Dimension(1120, 630)); // resolução 16x9
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform original = g2d.getTransform();

        camView.setTransform(original);
        camView.translate((double) getWidth() / 2, (double) getHeight() / 2);
        camView.scale(cam.getZoom(), cam.getZoom());
        camView.translate(-cam.getPosition().x(), -cam.getPosition().y());

        g2d.setTransform(camView);

        for (var r : scene)
            r.draw(g2d);

        g2d.setTransform(original);

        for (var r : uiElements)
            r.draw(g2d);
    }

    public void updateAll(double deltaTime)
    {
        this.deltaTime = deltaTime;
        if (isAnySchedulerResolving())
        {
            for (var scheduler : eventSchedulers)
                if (scheduler.isResolving())
                    scheduler.update(deltaTime);
            return;
        }

        for (Renderable rd : toRemoveRend)
            scene.remove(rd);
        for (Updatable up : toRemoveUp) {
            up.onDestroy();
            logics.remove(up);
        }

        for (Renderable rd : toAddRend) if (!(rd instanceof UiElement)) scene.add(rd);

        for (Updatable up : toAddUp)
        {
            if (up instanceof UiElement uiEl)
            {
                if (uiEl.getParent() == null)
                    uiElements.add(uiEl);
                else continue;
            }
            up.setup();
        }
        logics.addAll(toAddUp);

        toRemoveRend.clear();
        toRemoveUp.clear();
        toAddRend.clear();
        toAddUp.clear();

        for (var up : logics) {
            if (up instanceof UiElement uiEl && uiEl.getParent() != null) continue;
            up.update();
        }
    }

    private boolean isAnySchedulerResolving()
    {
        for (var scheduler : eventSchedulers)
            if (scheduler.isResolving())
                return true;

        return false;
    }

    public void addRenderable(Renderable r) { toAddRend.add(r); }
    public void removeRenderable(Renderable r) { toRemoveRend.add(r); }

    public void addElement(EngineElement el)
    {
        if (el instanceof Updatable up)
            toAddUp.add(up);
        if (el instanceof Renderable rd)
            toAddRend.add(rd);
    }

    public void removeElement(EngineElement el)
    {
        if (el instanceof Updatable up)
            toRemoveUp.add(up);
        if (el instanceof Renderable rd)
            toRemoveRend.add(rd);
    }

    public void addScheduler(EventScheduler scheduler) { eventSchedulers.add(scheduler); }
    public double getDeltaTime() { return deltaTime; }

    public static GamePanel getInstance() { return INSTANCE; }
    public static Camera getCamera() { return INSTANCE.cam; }
}