package game.itemsystem.items;

import game.battle.ActionResult;
import game.creature.Pokemon;
import game.creature.StatType;
import game.itemsystem.Item;
import java.awt.image.BufferedImage;

public class CaptureItem extends Item{
    private Double captureModifier;

    public CaptureItem(
        Double captureModifier, String name, String description, String inGameMessage, Boolean battleUsable, Integer basePrice, BufferedImage sprite
    ) {
        super(name, description, inGameMessage, battleUsable, basePrice, sprite);
        this.captureModifier = captureModifier;
    }

    public Double getCaptureModifier() {
        return captureModifier;
    }
    public void setCaptureModifier(Double captureModifier) {
        this.captureModifier = captureModifier;
    }
    public Double computeCatchChance(Pokemon target)
    {
        int maxHp = target.getCurrentStats().getValue(StatType.HP);
        int currentHp = target.getCurrentHp();
        double hpFactor = 1.0 - ((double) currentHp / maxHp);
        double baseChance = Math.max(0.05, hpFactor);
        return baseChance * captureModifier;
    }

    @Override public Boolean canUse(Pokemon target) {
        return target != null && !target.hasOwner() && target.isAlive();
    }

    @Override
    public ActionResult use(Pokemon target)
    {
        if (Math.random() <= computeCatchChance(target))
        {
            target.setOwner(true);
            return ActionResult.CAPTURED;
        }

        return ActionResult.MISSED;
    }
}
