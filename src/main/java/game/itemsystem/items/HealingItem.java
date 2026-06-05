package game.itemsystem.items;

import java.awt.image.BufferedImage;

import game.itemsystem.Item;

public abstract class HealingItem extends Item 
{
    public HealingItem(String name, String description, Integer basePrice, BufferedImage sprite) {
        super(name, description, true, basePrice, sprite);
    }
}
