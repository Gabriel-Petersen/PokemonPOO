package engine.events;

import engine.ui.elements.UiText;

public class TypewriterEvent extends GameEvent
{
    private final UiText object;
    private final String finalMessage;
    private final double typewriteTime;
    private final WaitEvent idleWaitEvent;

    private double elapsed = 0;
    private String currentMessage = "";
    private int charIndex = 0;
    private boolean isIdle = false;

    public TypewriterEvent(
            UiText uiObject, String message,
            double typewriteTime, double idleTime
    )
    {
        super(null);
        object = uiObject;
        finalMessage = message;
        this.typewriteTime = typewriteTime;
        idleWaitEvent = new WaitEvent(idleTime);
    }

    @Override
    public boolean isFinished() {
        if (currentMessage.equals(finalMessage))
            return idleWaitEvent.isFinished();
        return false;
    }

    @Override
    public void update(double dt)
    {
        if (!isIdle)
        {
            elapsed += dt;
            while (elapsed >= typewriteTime && charIndex < finalMessage.length())
            {
                elapsed -= typewriteTime;
                currentMessage += finalMessage.charAt(charIndex++);
                object.setText(currentMessage);
            }

            if (charIndex == finalMessage.length())
            {
                isIdle = true;
                idleWaitEvent.init();
            }
        }
        else
            idleWaitEvent.update(dt);
    }

    @Override
    public void init() {
        object.setText("");
        currentMessage = "";
    }
}
