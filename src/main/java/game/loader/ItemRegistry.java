package game.loader;

import engine.assets.AssetManager;
import game.itemsystem.Item;
import game.itemsystem.items.HpHealItem;

public class ItemRegistry {
    public static final Item smallPotion;
    public static final Item mediumPotion;

    static {
        smallPotion = new HpHealItem(
            "Small Potion", 
            "A potion that heals 10HP", 
            "Healed up to 10 HP points!!", 
            30,
            AssetManager.getSprite("player_sheet/run_up/running_04.png"), 
            10
        );

        mediumPotion = new HpHealItem(
            "Medium Potion", 
            "A potion that heals 30HP", 
            "Healed up to 30 HP points!!", 
            75,
            AssetManager.getSprite("player_sheet/run_down/running_00.png"), 
            30
        );
    }
}
