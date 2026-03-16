package engine.math.vectors;

public interface Vec2d
{
    double EPSILON = 1e-10;
    double x();
    double y();

    Vec2d ZERO = new ImmutableVec2d(0, 0);
    Vec2d UP = new ImmutableVec2d(0, -1);
    Vec2d DOWN = new ImmutableVec2d(0, 1);
    Vec2d RIGHT = new ImmutableVec2d(1, 0);
    Vec2d LEFT = new ImmutableVec2d(-1, 0);

    default double magnitudeSqrt() { return x()*x() + y()*y(); }
    default double magnitude() { return Math.sqrt(magnitudeSqrt()); }

    default double angleBtw(Vec2d other)
    {
        double dotProduct = x()*other.x() + y()*other.y();
        double mags = Math.sqrt(magnitudeSqrt() * other.magnitudeSqrt());
        return Math.acos(dotProduct / mags);
    }

    default MutableVec2d normalized()
    {
        double mag = magnitude();
        double x = x();
        double y = y();
        if (mag > EPSILON) {
            x /= mag;
            y /= mag;
        }
        return new MutableVec2d(x, y);
    }

    default MutableVec2d add(double x, double y) { return new MutableVec2d(x() + x, y() + y); }
    default MutableVec2d add(Vec2d other) { return new MutableVec2d(x() + other.x(), y() + other.y()); }
    default MutableVec2d sub(double x, double y) { return new MutableVec2d(x() - x, y() - y); }
    default MutableVec2d sub(Vec2d other) { return new MutableVec2d(x() - other.x(), y() - other.y()); }
    default MutableVec2d mul(double scalar) { return new MutableVec2d(x() * scalar, y() * scalar); }
}
