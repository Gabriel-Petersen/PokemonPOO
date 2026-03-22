package engine.core;

import engine.input.Input;
import engine.lifecycle.Renderable;
import engine.lifecycle.Updatable;

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
    private final List<Updatable> logics = new ArrayList<>();

    private final List<Renderable> toAddRend = new ArrayList<>();
    private final List<Renderable> toRemoveRend = new ArrayList<>();
    private final List<Updatable> toAddUp = new ArrayList<>();
    private final List<Updatable> toRemoveUp = new ArrayList<>();

    private final Camera cam = new Camera();
    private final AffineTransform camView = new AffineTransform();

    public GamePanel()
    {
        addKeyListener(Input.keyListener);
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
    }

    public void updateAll()
    {
        for (Renderable rd : toRemoveRend)
            scene.remove(rd);
        for (Updatable up : toRemoveUp) {
            up.onDestroy();
            logics.remove(up);
        }
        scene.addAll(toAddRend);
        for (Updatable up : toAddUp)
            up.setup();
        logics.addAll(toAddUp);
        toRemoveRend.clear();
        toRemoveUp.clear();
        toAddRend.clear();
        toAddUp.clear();

        for (var up : logics) up.update();
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

    public static GamePanel getInstance() {
        return INSTANCE;
    }
    public static Camera getCamera() { return INSTANCE.cam; }
}