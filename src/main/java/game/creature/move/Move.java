package game.creature.move;

import game.battle.BattleContext;
import game.creature.Pokemon;

public abstract class Move {
    public void restorePP(){
        
    }
    public abstract Integer getPriority();
    public abstract MoveResult execute(Pokemon attacker, Pokemon target, BattleContext context);
}