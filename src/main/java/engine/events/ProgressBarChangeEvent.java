package engine.events;

import engine.ui.elements.UiProgressBar;

public class ProgressBarChangeEvent extends GameEvent
{
    private final UiProgressBar object;
    private final double startValue;
    private final double targetValue;
    private final double duration;
    private double elapsed = 0;

    public ProgressBarChangeEvent(UiProgressBar bar, double targetValue, double duration)
    {
        super(null);
        this.object = bar;
        this.startValue = bar.getProgress();
        this.targetValue = targetValue;
        this.duration = duration;

        if (targetValue > 1 || targetValue < 0)
            throw new IllegalArgumentException("Target value must be between 0.0 and 1.0");
    }

    @Override
    public void update(double dt)
    {
        elapsed += dt;

        double t = Math.min(1.0, elapsed / duration);
        double current = startValue + (targetValue - startValue) * t;

        object.setProgress(current);
    }

    @Override public boolean isFinished() { return elapsed >= duration; }
    @Override public void init() { elapsed = 0; }
}