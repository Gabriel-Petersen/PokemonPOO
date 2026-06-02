package game.items;

import java.awt.image.BufferedImage;

import game.battle.ActionResult;
import game.creature.Pokemon;

public class CaptureItem extends Item{
    private Double captureModifier;

    public CaptureItem(Double captureModifier, String name, String description, Boolean battleUsable, Integer basePrice, BufferedImage sprite) {
        super(name, description, battleUsable, basePrice, sprite);
        this.captureModifier = captureModifier;
    }

    public Double getCaptureModifier() {
        return captureModifier;
    }

    public void setCaptureModifier(Double captureModifier) {
        this.captureModifier = captureModifier;
    }

    
    public Double computeCatchChance(Pokemon target){
        return 1.0;//STUB
    }


    @Override
    public Boolean canUse(Pokemon target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ActionResult use(Pokemon target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    



}
