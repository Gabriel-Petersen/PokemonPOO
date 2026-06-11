package game.creature;

import game.creature.move.Move;
import game.creature.move.StatType;
import game.creature.move.status.StatusEffect;
import game.creature.move.status.VolatileStatusEffect;
import java.util.ArrayList;
import java.util.Arrays;
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
        statusEffects.removeIf(ef -> ef == null || ef instanceof VolatileStatusEffect || ef.isExpired());
    }

    public String getNickname() {
        return (nickname == null || nickname.isBlank() ? specie.getName() : nickname);
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
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentAccuracy == null) ? 0 : currentAccuracy.hashCode());
        result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
        result = prime * result + ((currentHp == null) ? 0 : currentHp.hashCode());
        result = prime * result + ((specie == null) ? 0 : specie.hashCode());
        result = prime * result + ((currentStats == null) ? 0 : currentStats.hashCode());
        result = prime * result + ((currentLevel == null) ? 0 : currentLevel.hashCode());
        result = prime * result + ((currentExperience == null) ? 0 : currentExperience.hashCode());
        result = prime * result + ((hasOwner == null) ? 0 : hasOwner.hashCode());
        result = prime * result + Arrays.hashCode(moves);
        result = prime * result + ((statusEffects == null) ? 0 : statusEffects.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pokemon other = (Pokemon) obj;
        if (currentAccuracy == null) {
            if (other.currentAccuracy != null)
                return false;
        } else if (!currentAccuracy.equals(other.currentAccuracy))
            return false;
        if (nickname == null) {
            if (other.nickname != null)
                return false;
        } else if (!nickname.equals(other.nickname))
            return false;
        if (currentHp == null) {
            if (other.currentHp != null)
                return false;
        } else if (!currentHp.equals(other.currentHp))
            return false;
        if (specie == null) {
            if (other.specie != null)
                return false;
        } else if (!specie.equals(other.specie))
            return false;
        if (currentStats == null) {
            if (other.currentStats != null)
                return false;
        } else if (!currentStats.equals(other.currentStats))
            return false;
        if (currentLevel == null) {
            if (other.currentLevel != null)
                return false;
        } else if (!currentLevel.equals(other.currentLevel))
            return false;
        if (currentExperience == null) {
            if (other.currentExperience != null)
                return false;
        } else if (!currentExperience.equals(other.currentExperience))
            return false;
        if (hasOwner == null) {
            if (other.hasOwner != null)
                return false;
        } else if (!hasOwner.equals(other.hasOwner))
            return false;
        if (!Arrays.equals(moves, other.moves))
            return false;
        if (statusEffects == null) {
            if (other.statusEffects != null)
                return false;
        } else if (!statusEffects.equals(other.statusEffects))
            return false;
        return true;
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }
}