package game.itemsystem.items;

import game.itemsystem.Item;
import java.awt.image.BufferedImage;

public abstract class HealingItem extends Item 
{
    public HealingItem(String name, String description, String inGameMessage, Integer basePrice, BufferedImage sprite) {
        super(name, description, inGameMessage, true, basePrice, sprite);
    }
}
