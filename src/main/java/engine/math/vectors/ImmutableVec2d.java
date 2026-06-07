package engine.math.vectors;

record ImmutableVec2d(double x, double y) implements Vec2d
{
    @Override
    public String toString() {
        return "(" + x + ", " + y + ") [Const]";
    }
}

