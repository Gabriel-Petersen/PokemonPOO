package game.player;

import engine.math.vectors.Vec2d;

public interface Interactable{
    void onInteract(Player player);
    Vec2d getPosition();
}