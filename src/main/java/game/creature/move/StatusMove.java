package game.creature.move;

import game.battle.BattleContext;
import game.creature.ElementType;
import game.creature.Pokemon;
import game.creature.move.status.StatusEffect;

public class StatusMove extends Move {

    private final StatusEffect effect;

    public StatusMove(int id, String name, Double accuracy, Integer priority, boolean isSelfTarget, StatusEffect effect) {
        super(id, name, 0, accuracy, priority, ElementType.NONE, MoveCategory.STATUS, isSelfTarget);
        this.effect = effect;
    }

    @Override
    public MoveResult execute(Pokemon attacker, Pokemon target, BattleContext context) {

        if (!canUse(context)) {
            return new MoveResult("Sem PP suficiente!", false);
        }

        MoveResult result = new MoveResult(attacker.getNickname() + " utilizou " + getName() + "!", false, 0, false);
        if (mayAttack(attacker, context)) {
            result.setHit(true);
            result.setStatusApplied(true);
            target.applyStatus(this.effect, context);
        } else {
            result.addMessage("Errou!!!");
        }

        return result;
    }

    public StatusEffect getEffect() { return effect; }
}
