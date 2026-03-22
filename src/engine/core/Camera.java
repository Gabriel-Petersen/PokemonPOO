package engine.core;

import engine.math.vectors.MutableVec2d;
import engine.math.vectors.Vec2d;

public class Camera
{
    private final MutableVec2d position = new MutableVec2d();
    private double zoom = 1;

    public void lookAt(double x, double y) { position.set(x, y); }
    public void lookAt(Vec2d position) { this.position.set(position); }
    public Vec2d getPosition() { return position; }

    public double getZoom() { return zoom; }
    public void setZoom(double zoom) { this.zoom = zoom; }
}
