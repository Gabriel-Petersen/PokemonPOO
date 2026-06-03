package game.creature;

import game.creature.move.Move;
import game.creature.move.StatType;
import game.creature.move.status.StatusEffect;
import game.creature.move.status.VolatileStatusEffect;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private Integer currentAccuracy = 100;
    private String nickname;
    private Integer currentHp;
    private Specie specie;
    private Stats currentStats;
    private Integer currentLevel;
    private Integer currentExperience;
    private Boolean hasOwner = false;
    private final Move[] moves;
    private final List<StatusEffect> statusEffects = new ArrayList<>();
    
    public Pokemon(String nickname, Specie Specie, Integer currentLevel) { this(nickname, Specie, currentLevel, new Move[4]); }

    public Pokemon(String nickname, Specie Specie, Integer currentLevel, Move[] moves) {
        this.nickname = nickname;
        this.specie = Specie;
        this.currentLevel = currentLevel;
        this.moves = moves;
        currentStats = Specie.getBaseStats().scaleForLevel(currentLevel);
        currentHp = currentStats.getValue(StatType.HP);
    }

    public void setupForBattle() {
        currentAccuracy = 100;
        statusEffects.removeIf(ef -> ef instanceof VolatileStatusEffect || ef.isExpired());
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getCurrentHp() {
        return currentHp;
    }

    public Specie getSpecie() {
        return specie;
    }

    public Stats getCurrentStats() {
        return currentStats;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public Integer getCurrentExperience() {
        return currentExperience;
    }

    public Move[] getMoves() {
        return moves;
    }

    public Boolean isAlive(){
        return currentHp>0;
    }

    public Boolean hasOwner() { return hasOwner; }
    public void setOwner(Boolean hasOwner) { this.hasOwner = hasOwner; }

    public Integer receiveDamage(Integer damage){
        currentHp-=damage;
        return currentHp;
    }
    public Integer heal(Integer heal){
        if(currentHp+heal<=currentStats.getValue(StatType.HP)){
            currentHp+=heal;
        }else{
            currentHp=currentStats.getValue(StatType.HP);
        }
        return currentHp;
    }

    /*public Boolean applyStatus(StatusEffect statusEffect, BattleContext context){
        
    }*/

    /*public Stats getEffectiveStats(BattleContext context){
    
    }*/

    /*public Integer getEffectiveStat(StatType statType, BattleContext context){
    
    }*/

    /*public void resolveStatusAtTurnStart(BattleContext context){
    
    }*/

    /*public void resolveStatusAtTurnEnd(BattleContext context){
    
    }*/

    

    public Boolean replaceMove(Integer slot, Move currentMove){
        Move oldMove = moves[slot];
        moves[slot]=currentMove;
        return oldMove==null;
    }

    public Integer getCurrentAccuracy() {
        return currentAccuracy;
    }

    public void setCurrentAccuracy(Integer currentAccuracy) {
        this.currentAccuracy = currentAccuracy;
    }
    
    public void levelUp(){
        currentLevel++;
        currentStats = currentStats.scaleForLevel(currentLevel);
        currentHp = currentStats.getValue(StatType.HP);
        for(var mv:moves){
            if(mv!=null){
                mv.restorePP();
            }
        }
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }
}