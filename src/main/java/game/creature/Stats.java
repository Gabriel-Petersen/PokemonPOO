package game.creature;

import game.creature.move.StatType;

public class Stats {
    
    private Integer hp;
    private Integer attack;
    private Integer defense;
    private Integer specialAttack;
    private Integer specialDefense;
    private Integer speed;

    public Stats(Stats other) {
        this.hp = other.hp;
        this.attack = other.attack;
        this.defense = other.defense;
        this.specialAttack = other.specialAttack;
        this.specialDefense = other.specialDefense;
        this.speed = other.speed;
    }

    public Stats(Integer hp, Integer attack, Integer defense, Integer specialAttack, Integer specialDefense, Integer speed) {
        this.attack = attack;
        this.defense = defense;
        this.hp = hp;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
    }

    public Stats scaleForLevel(Integer level) {
        Stats scaledStats = new Stats(this);
        scaledStats.hp = (int) (this.hp * (1 + level * 0.05));
        scaledStats.attack = (int) (this.attack * (1 + level * 0.1));
        scaledStats.defense = (int) (this.defense * (1 + level * 0.1));
        scaledStats.specialAttack = (int) (this.specialAttack * (1 + level * 0.1));
        scaledStats.specialDefense = (int) (this.specialDefense * (1 + level * 0.1));
        scaledStats.speed = (int) (this.speed * (1 + level * 0.1));
        return scaledStats;
    }
    
    public Integer getValue(StatType statType){
        return switch (statType) {
            case HP -> hp;
            case ATTACK -> attack;
            case DEFENSE -> defense;
            case SPECIAL_ATTACK -> specialAttack;
            case SPECIAL_DEFENSE -> specialDefense;
            case SPEED -> speed;
        };
    }

    public Stats withValue(StatType statType, Integer value){
        Stats copy = new Stats(this);
        switch (statType) {
            case HP -> copy.hp = value;
            case ATTACK -> copy.attack = value;
            case DEFENSE -> copy.defense = value;
            case SPECIAL_ATTACK -> copy.specialAttack = value;
            case SPECIAL_DEFENSE -> copy.specialDefense = value;
            case SPEED -> copy.speed = value;
        }
        return copy;
    }    
}