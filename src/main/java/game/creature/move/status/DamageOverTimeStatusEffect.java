package game.creature.move.status;

import game.battle.BattleContext;
import game.creature.Pokemon;
import game.creature.StatType;

import java.util.List;

public class DamageOverTimeStatusEffect extends PersistentStatusEffect 
{
    private final String applyMessage;
    private final String turnEndMessage;
    private final StatModifier damageFormula;

    public DamageOverTimeStatusEffect(
        String name, 
        List<StatModifierRule> modifiers, 
        String applyMessage, 
        String turnEndMessage,
        StatModifier damageFormula
    ) {
        super(name, modifiers);
        this.applyMessage = applyMessage;
        this.turnEndMessage = turnEndMessage;
        this.damageFormula = damageFormula != null ? damageFormula : (hpMax, ctx) -> hpMax / 8;
    }

    @Override
    public void onApply(Pokemon target, BattleContext context) {
        if (applyMessage != null) context.getHud().updateConsoleMessage(target.getNickname() + " " + applyMessage);
    }

    @Override
    public void onTurnEnd(Pokemon target, BattleContext context) 
    {
        int maxHp = target.getCurrentStats().getValue(StatType.HP);
        int damage = Integer.max(1, damageFormula.modify(maxHp, context));

        target.receiveDamage(damage);
        
        if (turnEndMessage != null)
            context.getHud().updateConsoleMessage(target.getNickname() + " " + turnEndMessage);
    }
}