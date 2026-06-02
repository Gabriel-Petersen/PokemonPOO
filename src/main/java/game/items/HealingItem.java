package game.items;

import java.awt.image.BufferedImage;

public abstract class HealingItem extends Item 
{
    public HealingItem(String name, String description, Integer basePrice, BufferedImage sprite) {
        super(name, description, true, basePrice, sprite);
    }
}
