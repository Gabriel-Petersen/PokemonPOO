package game.creature.move.status;

import game.battle.BattleContext;
import game.creature.Pokemon;
import game.creature.StatType;

import java.util.List;

public class DamageOverTimeStatusEffect extends PersistentStatusEffect
{
    public DamageOverTimeStatusEffect(String name, List<StatModifierRule> modifiers) {
        super(name, modifiers);
    }

    @Override
    public void onApply(Pokemon target, BattleContext context) {
        System.out.println("LOG: " + target.getNickname() + " foi envenenado!");
    }

    @Override public void onTurnStart(Pokemon target, BattleContext context) { }

    @Override
    public void onTurnEnd(Pokemon target, BattleContext context)
    {
        int maxHp = target.getCurrentStats().getValue(StatType.HP);
        int damage = Integer.max(1, maxHp / 8);

        target.receiveDamage(damage);
        context.getHud().updateConsoleMessage(target.getNickname() + " sofreu com o veneno!");
    }
}
