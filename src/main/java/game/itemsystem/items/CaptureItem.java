package game.itemsystem.items;

import game.battle.ActionResult;
import game.creature.Pokemon;
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

    
    public Double computeCatchChance(Pokemon target){
        return 1.0 * captureModifier;
    }


    @Override
    public Boolean canUse(Pokemon target) {
        return !target.hasOwner();
    }

    @Override
    public ActionResult use(Pokemon target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    



}
