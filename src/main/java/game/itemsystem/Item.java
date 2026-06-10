package game.itemsystem;

import game.battle.ActionResult;
import game.creature.Pokemon;
import java.awt.image.BufferedImage;

public abstract class Item {
 private String name;
 private String description;
 private String inGameMessage;
 private Boolean battleUsable;
 private Integer basePrice;
 private BufferedImage sprite;
 
 
 public Item(
   String name, String description, String inGameMessage, Boolean battleUsable, Integer basePrice, BufferedImage sprite
 ) {
    this.name = name;
    this.description = description;
    this.inGameMessage = inGameMessage;
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
 public String getInGameMessage() {
   return inGameMessage;
 }
 public void setInGameMessage(String message) {
   inGameMessage = message;
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

 @Override public boolean equals(Object other) {
    if (other instanceof Item it) return it.name.equals(name);
    return false;
 }

 @Override public int hashCode() { return name.hashCode(); }

 public abstract Boolean canUse(Pokemon target);

 public abstract ActionResult use(Pokemon target);

}
