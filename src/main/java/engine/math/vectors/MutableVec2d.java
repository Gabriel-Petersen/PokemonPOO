package engine.math.vectors;

public class MutableVec2d implements Vec2d
{
    public double x;
    public double y;

    public MutableVec2d() {}
    public MutableVec2d(double x, double y) { this.x = x; this.y = y; }
    public MutableVec2d(Vec2d other) { this(other.x(), other.y()); }

    @Override public double x() { return x; }
    @Override public double y() { return y; }

    public MutableVec2d set(double x, double y)
    {
        this.x = x; this.y = y;
        return this;
    }

    public MutableVec2d set(Vec2d pos) { return set(pos.x(), pos.y()); }

    @Override
    public MutableVec2d add(Vec2d other)
    {
        this.x += other.x();
        this.y += other.y();
        return this;
    }

    @Override
    public MutableVec2d add(double x, double y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    @Override
    public MutableVec2d sub(Vec2d other)
    {
        this.x -= other.x();
        this.y -= other.y();
        return this;
    }

    @Override
    public MutableVec2d sub(double x, double y)
    {
        this.x -= x;
        this.y -= y;
        return this;
    }

    @Override
    public MutableVec2d mul(double scalar)
    {
        x *= scalar;
        y *= scalar;
        return this;
    }

    @Override
    public MutableVec2d normalized() {
        double mag = magnitude();
        if (mag > EPSILON) {
            x /= mag;
            y /= mag;
        }
        return this;
    }

    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }
}
