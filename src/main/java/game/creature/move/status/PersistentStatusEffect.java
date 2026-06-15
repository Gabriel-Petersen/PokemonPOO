package game.creature.move.status;

import java.util.ArrayList;
import java.util.List;

import game.battle.BattleContext;
import game.creature.Pokemon;

public class PersistentStatusEffect implements StatusEffect {

    private final String name;
    protected final List<StatModifierRule> modifiers;

    public PersistentStatusEffect(String name, List<StatModifierRule> modifiers) {
        this.name = name;
        this.modifiers = modifiers != null ? modifiers : new ArrayList<>();
    }

    @Override public Boolean isExpired() { return false; }
    @Override  public List<StatModifierRule> getStatModifierRules() { return modifiers; }
    public String getName() { return name; }

    @Override public void onApply(Pokemon target, BattleContext context) { }
    @Override public void onTurnStart(Pokemon target, BattleContext context) { }
    @Override public void onTurnEnd(Pokemon target, BattleContext context) { }
}
