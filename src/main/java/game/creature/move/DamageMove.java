package game.creature.move;

import game.battle.BattleContext;
import game.creature.Pokemon;

public class DamageMove extends Move {

    private MoveCategory category;

    public DamageMove(String name,Integer power,Double accuracy,Integer priority,ElementType elementType,MoveCategory category) {
        super(name, power, accuracy, priority, elementType, category);
        this.category = category;
    }
    @Override
    public MoveCategory getCategory() {
        return category;
    }

    public int calculateDamage(Pokemon attacker, Pokemon target, BattleContext context){
        int attackStat = category == MoveCategory.PHYSICAL ? attacker.getEffectiveStat(StatType.ATTACK, context) : attacker.getEffectiveStat(StatType.SPECIAL_ATTACK, context);
        int defenseStat = category == MoveCategory.PHYSICAL ? target.getEffectiveStat(StatType.DEFENSE, context) : target.getEffectiveStat(StatType.SPECIAL_DEFENSE, context);
        int damage=getPower();
        damage*= target.getSpecie().weaknessTo(elementType);
        damage*=attackStat*2/defenseStat;
        return damage;
    }

    @Override
    public MoveResult execute(Pokemon attacker, Pokemon target, BattleContext context){
        if(!canUse(context)){
            return new MoveResult("Sem PP suficiente!", false);
        }
        
        MoveResult result = new MoveResult(attacker.getNickname() + " utilizou " + getName() + "!", false, 0, false);

        if(Math.random()<=getAccuracy() * (double)attacker.getCurrentAccuracy()/100.0){
            result.setDamageApplied(calculateDamage(attacker, target, context));
            result.setHit(true);
            target.receiveDamage(result.getDamageApplied());
            if (target.getSpecie().weaknessTo(elementType) > 1) 
                result.addMessage("Foi superefetivo!!");
            result.addMessage("Causou " + result.getDamageApplied() + " de dano!!");

            if (target.isAlive())
                result.addMessage(target.getNickname() + " foi derrotado!!");
        }
        else {
            result.addMessage("Errou!!!");
        }
        return result;
    }
}
