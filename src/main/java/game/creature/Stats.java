package game.creature;

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

    public Stats scaleForLevel(int level)
    {
        Stats scaled = new Stats(this);

        scaled.hp = ((this.hp * 2 * level) / 80) + level + 10;
        scaled.attack = ((this.attack * 2 * level) / 80) + 5;
        scaled.defense = ((this.defense * 2 * level) / 80) + 5;
        scaled.specialAttack = ((this.specialAttack * 2 * level) / 80) + 5;
        scaled.specialDefense = ((this.specialDefense * 2 * level) / 80) + 5;
        scaled.speed = ((this.speed * 2 * level) / 80) + 5;

        return scaled;
    }
    
    public Integer getValue(StatType statType){
        return switch (statType) {
            case HP -> hp;
            case ATTACK -> attack;
            case DEFENSE -> defense;
            case SPECIAL_ATTACK -> specialAttack;
            case SPECIAL_DEFENSE -> specialDefense;
            case SPEED -> speed;
            case ACCURACY -> 100;
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
            default -> throw new IllegalArgumentException("Unexpected value: " + statType);
        }
        return copy;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("Stats {")
            .append("\n\tHP: ").append(hp)
            .append("\n\tATK: ").append(attack)
            .append("\n\tDEF: ").append(defense)
            .append("\n\tSP_ATK: ").append(specialAttack)
            .append("\n\tSP_DEF: ").append(specialDefense)
            .append("\n\tSPEED: ").append(speed)
            .append("\n}")
            .toString();
    }
}