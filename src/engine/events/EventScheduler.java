package engine.events;

import engine.core.GamePanel;

import java.util.LinkedList;
import java.util.Queue;

public class EventScheduler
{
    private final Queue<GameEvent> events = new LinkedList<>();
    private boolean isResolving = false;

    public EventScheduler() {
        GamePanel.getInstance().addScheduler(this);
    }

    public void update(double dt)
    {
        if (events.isEmpty()) {
            isResolving = false;
            return;
        }

        GameEvent current = events.peek();

        if (!current.isStarted())
        {
            current.init();
            current.setStarted(true);
        }

        current.update(dt);

        if (current.isFinished())
        {
            events.poll();
            if (events.isEmpty())
                isResolving = false;
        }
    }

    public void enqueue(GameEvent event) {
        events.add(event);
    }

    public void resolve() {
        if (!events.isEmpty()) isResolving = true;
    }

    public boolean isResolving() { return isResolving; }
}
