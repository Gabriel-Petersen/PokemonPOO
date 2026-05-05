package engine.ui.core;

import engine.core.Transform;
import engine.math.vectors.MutableVec2d;
import engine.math.vectors.Vec2d;

public class UiTransform extends Transform {
    public enum Anchor {
        TOP_LEFT (0f, 0f),
        TOP_RIGHT (1f, 0f),
        BOTTOM_LEFT(0f, 1f),
        BOTTOM_RIGHT(1f, 1f),
        CENTER (0.5f, 0.5f),
        CENTER_TOP (0.5f, 0f),
        CENTER_BOTTOM(0.5f, 1f),
        CENTER_LEFT(0f, 0.5f),
        CENTER_RIGHT(1f, 0.5f);

        private final float px, py;

        Anchor(float px, float py)
        {
            this.px = px;
            this.py = py;
        }

        public Vec2d calculate(Vec2d elementScale, Vec2d offset, Vec2d parentScale)
        {
            double anchorX = parentScale.x() * px;
            double anchorY = parentScale.y() * py;

            double pivotX = elementScale.x() * px;
            double pivotY = elementScale.y() * py;

            return new MutableVec2d(
                    anchorX - pivotX + offset.x(),
                    anchorY - pivotY + offset.y()
            );
        }
    }

    private Anchor anchor = Anchor.TOP_LEFT;

    public Anchor getAnchor() { return anchor; }
    public void setAnchor(Anchor anchor) { this.anchor = anchor; }
}
