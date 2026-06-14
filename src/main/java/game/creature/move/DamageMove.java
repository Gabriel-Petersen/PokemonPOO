package game.creature.move;

import game.battle.BattleContext;
import game.creature.ElementType;
import game.creature.Pokemon;
import game.creature.StatType;

public class DamageMove extends Move 
{
    public DamageMove(int id, String name, Integer power, Double accuracy, Integer priority, ElementType elementType, MoveCategory category) {
        super(id, name, power, accuracy, priority, elementType, category);
    }

    public int calculateDamage(Pokemon attacker, Pokemon target, BattleContext context)
    {
        int attackStat = category == MoveCategory.PHYSICAL ?
                attacker.getEffectiveStat(StatType.ATTACK, context) :
                attacker.getEffectiveStat(StatType.SPECIAL_ATTACK, context);

        int defenseStat = category == MoveCategory.PHYSICAL ?
                target.getEffectiveStat(StatType.DEFENSE, context) :
                target.getEffectiveStat(StatType.SPECIAL_DEFENSE, context);

        if (defenseStat <= 0) defenseStat = 1;

        int baseDamage = getPower() * target.getSpecie().weaknessTo(elementType);
        double rand = 1 + Math.random() / 2.0;
        double exactDamage = rand * (double)(baseDamage + attackStat - defenseStat);
        int finalDamage = (int) Math.round(exactDamage);
        return Math.max(0, finalDamage);
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
