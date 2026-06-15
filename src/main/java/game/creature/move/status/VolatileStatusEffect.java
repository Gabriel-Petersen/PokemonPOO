package game.creature.move.status;

import game.battle.BattleContext;
import game.creature.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class VolatileStatusEffect implements StatusEffect {

    private final String name;
    private Integer remainingTurns;
    private final List<StatModifierRule> modifiers;

    public VolatileStatusEffect(String name, Integer durationTurns, List<StatModifierRule> modifiers)
    {
        this.name = name;
        this.remainingTurns = durationTurns;
        this.modifiers = modifiers != null ? modifiers : new ArrayList<>();
    }

    @Override public Boolean isExpired() { return remainingTurns <= 0; }

    @Override
    public void onApply(Pokemon target, BattleContext context) {
        System.out.println("LOG: Efeito volátil '" + name + "' aplicado em " + target.getNickname());
    }

    @Override public void onTurnStart(Pokemon target, BattleContext context) { }

    @Override
    public void onTurnEnd(Pokemon target, BattleContext context)
    {
        if (remainingTurns > 0) {
            remainingTurns--;
            System.out.println("LOG: Efeito '" + name + "' em " + target.getNickname() + " reduziu para " + remainingTurns + " turnos.");
        }
    }

    @Override
    public List<StatModifierRule> getStatModifierRules()
    {
        if (isExpired())
            return new ArrayList<>();
        return modifiers;
    }

    public String getName() { return name; }
    public Integer getRemainingTurns() { return remainingTurns; }
}
