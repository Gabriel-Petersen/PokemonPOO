package game.creature.move;

import game.battle.BattleContext;
import game.creature.Pokemon;

public abstract class Move {
    private static final int MAX_PP=20;
    private String name;
    private Integer power;
    private Double accuracy;
    private Integer priority;
    protected ElementType elementType;
    private Integer pp;
    private MoveCategory category;    

    public Move(String name, Integer power, Double accuracy, Integer priority, ElementType elementType,
            MoveCategory category) {
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        this.priority = priority;
        this.elementType = elementType;
        this.category = category;
        restorePP();
    }

    public Boolean canUse(BattleContext context){
        return pp>0;
    }

    public abstract MoveResult execute(Pokemon attacker,Pokemon target,BattleContext context);
    public void restorePP(){
        pp=MAX_PP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp;
    }

    public MoveCategory getCategory() {
        return category;
    }

    public void setCategory(MoveCategory category) {
        this.category = category;
    }

    

}