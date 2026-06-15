package game.creature.move;

import game.battle.BattleContext;
import game.creature.ElementType;
import game.creature.Pokemon;
import game.creature.StatType;

public class DamageMove extends Move 
{
    public DamageMove(int id, String name, Integer power, Double accuracy, Integer priority, ElementType elementType, MoveCategory category) {
        super(id, name, power, accuracy, priority, elementType, category, false);
    }

    public int calculateDamage(Pokemon attacker, Pokemon target, BattleContext context)
    {
        int attackStat = category == MoveCategory.PHYSICAL
                ? attacker.getEffectiveStat(StatType.ATTACK, context)
                : attacker.getEffectiveStat(StatType.SPECIAL_ATTACK, context);

        int defenseStat = category == MoveCategory.PHYSICAL
                ? target.getEffectiveStat(StatType.DEFENSE, context)
                : target.getEffectiveStat(StatType.SPECIAL_DEFENSE, context);

        attackStat = Math.max(1, attackStat);
        defenseStat = Math.max(1, defenseStat);

        double base = (((2.0 * attacker.getCurrentLevel() / 5.0) + 2.0)
                * getPower()
                * ((double) attackStat / defenseStat)) / 30.0;

        double stab = attacker.getSpecie().hasType(elementType) ? 1.5 : 1.0;

        double type = target.getSpecie().weaknessTo(elementType);
        double rand = 0.85 + Math.random() * 0.15;

        double damage = (base * stab * type * rand) + 2;

        return Math.max(1, (int) Math.round(damage));
    }

    @Override
    public MoveResult execute(Pokemon attacker, Pokemon target, BattleContext context)
    {
        if(!canUse(context))
            return new MoveResult("Sem PP suficiente!", false);
        
        MoveResult result = new MoveResult(attacker.getNickname() + " utilizou " + getName() + "!", false, 0, false);

        if(Math.random()<=getAccuracy() * (double)attacker.getCurrentAccuracy()/100.0)
        {
            result.setDamageApplied(calculateDamage(attacker, target, context));
            result.setHit(true);
            target.receiveDamage(result.getDamageApplied());
            if (target.getSpecie().weaknessTo(elementType) > 1) 
                result.addMessage("Foi super efetivo!!");
            result.addMessage("Causou " + result.getDamageApplied() + " de dano!!");
        }
        else {
            result.addMessage("Errou!!!");
        }
        return result;
    }
}
