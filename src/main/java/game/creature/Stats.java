package game.creature;

public class Stats {
    
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;

    public Stats(int hp, int attack, int defense, int specialAttack, int specialDefense, int speed) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
    }
    public void scaleForLevel(int level) {
        this.hp = (int) (this.hp * (1 + level * 0.05));
        this.attack = (int) (this.attack * (1 + level * 0.1));
        this.defense = (int) (this.defense * (1 + level * 0.1));
        this.specialAttack = (int) (this.specialAttack * (1 + level * 0.1));
        this.specialDefense = (int) (this.specialDefense * (1 + level * 0.1));
        this.speed = (int) (this.speed * (1 + level * 0.1));
    }

    
}