package engine.core;

import engine.math.vectors.MutableVec2d;
import engine.math.vectors.Vec2d;

public class Transform
{
    private final MutableVec2d position = new MutableVec2d(0, 0);
    private final MutableVec2d scale = new MutableVec2d(1, 1);
    public double rotation = 0; // graus

    public void translate(Vec2d velocity) {
        position.add(velocity);
    }
    public void translate(double x, double y) {
        position.add(x, y);
    }

    public void setPosition(double x, double y) {
        position.set(x, y);
    }
    public void setPosition(Vec2d position) {
        this.position.set(position);
    }

    public void setScale(double x, double y) {
        scale.set(x, y);
    }
    public void setScale(Vec2d scale) {
        this.scale.set(scale);
    }

    public double distanceSqrt(Transform other)
    {
        double dx = this.getPosition().x() - other.getPosition().x();
        double dy = this.getPosition().y() - other.getPosition().y();
        return dx*dx + dy*dy;
    }
     public double distanceTo(Transform other) { return Math.sqrt(distanceSqrt(other)); }

    public Vec2d getPosition() { return position; }
    public Vec2d getScale() { return scale; }
}