package engine.lifecycle;

import engine.core.EngineElement;

public interface Updatable extends EngineElement
{
    void setup();
    void update();
    default void onDestroy() {}
}