package engine.events;

public abstract class GameEvent
{
    protected EventScheduler scheduler;
    protected boolean isStarted = false;

    protected GameEvent(EventScheduler fatherScheduler) {
        scheduler = fatherScheduler;
    }

    public abstract boolean isFinished();
    public abstract void update(double dt);
    public abstract void init();

    public boolean isStarted() { return isStarted; }
    public void setStarted(boolean started) { isStarted = started; }
}
