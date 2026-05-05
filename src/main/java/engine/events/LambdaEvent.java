package engine.events;

public class LambdaEvent extends GameEvent
{
    private final Runnable action;

    public LambdaEvent(Runnable action)
    {
        super(null);
        this.action = action;
    }

    @Override public boolean isFinished() { return true; }
    @Override public void update(double dt) { }
    @Override public void init() { action.run(); }
}