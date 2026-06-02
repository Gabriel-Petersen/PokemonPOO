package game.items;

import game.battle.ActionResult;
import game.creature.Pokemon;
import java.awt.image.BufferedImage;

public abstract class Item {
 private String name;
 private String description;
 private Boolean battleUsable;
 private Integer basePrice;
 private BufferedImage sprite;
 
 
 public Item(String name, String description, Boolean battleUsable, Integer basePrice, BufferedImage sprite) {
    this.name = name;
    this.description = description;
    this.battleUsable = battleUsable;
    this.basePrice = basePrice;
    this.sprite = sprite;
}


 public String getName() {
    return name;
 }
 public void setName(String name) {
    this.name = name;
 }
 public String getDescription() {
    return description;
 }
 public void setDescription(String description) {
    this.description = description;
 }
    public Boolean isBattleUsable() {
    return battleUsable;
 }
 public void setBattleUsable(Boolean battleUsable) {
    this.battleUsable = battleUsable;
 }
 public Integer getBasePrice() {
    return basePrice;
 }
 public void setBasePrice(Integer basePrice) {
    this.basePrice = basePrice;
 }
 public BufferedImage getSprite() {
    return sprite;
 }
 public void setSprite(BufferedImage sprite) {
    this.sprite = sprite;
 }



 public abstract Boolean canUse(Pokemon target);

 public abstract ActionResult use(Pokemon target);

}
