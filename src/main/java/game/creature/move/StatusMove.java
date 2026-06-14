package game.creature.move;

import game.battle.BattleContext;
import game.creature.Pokemon;
import game.creature.move.status.StatusEffect;

public class StatusMove extends Move {

    private StatusEffect effect;

    public StatusMove(String name, Integer power, Double accuracy, Integer priority, ElementType elementType,
            MoveCategory category) {
        super(name, power, accuracy, priority, elementType, category);
    }

    @Override
    public MoveResult execute(Pokemon attacker, Pokemon target, BattleContext context) {

        if (!canUse(context)) {
            return new MoveResult("Sem PP suficiente!", false);
        }

        MoveResult result = new MoveResult(attacker.getNickname() + " utilizou " + getName() + "!", false, 0, false);
        if (Math.random() <= getAccuracy() * (double) attacker.getCurrentAccuracy() / 100.0) {
            result.setHit(true);
            result.setStatusApplied(true);
            effect.onApply(target, context);
        } else {
            result.addMessage("Errou!!!");
        }

        return result;
    }

}
