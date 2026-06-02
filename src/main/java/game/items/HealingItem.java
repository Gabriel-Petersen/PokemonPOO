package game.items;

import java.awt.image.BufferedImage;

public abstract class HealingItem extends Item {
     private Integer healAmount;

    public HealingItem(String name, String description, Boolean battleUsable, Integer basePrice, BufferedImage sprite, Integer healAmount) {
        super(name, description, battleUsable, basePrice, sprite);
        this.healAmount = healAmount;
    }
  

    public Integer getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(Integer healAmount) {
        this.healAmount = healAmount;
    }

    
}
