package engine.events;

public class WaitEvent extends GameEvent
{
    private final double duration;
    private double elapsed = 0;

    public WaitEvent(double duration) {
        super(null);
        this.duration = duration;
    }

    @Override public void init() { }
    @Override public void update(double dt) { elapsed += dt; }
    @Override public boolean isFinished() { return elapsed >= duration; }
}