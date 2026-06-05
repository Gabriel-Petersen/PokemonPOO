package game.creature;

import game.creature.move.Move;
import game.creature.move.StatType;
import game.creature.move.status.StatusEffect;
import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private Integer currentAccuracy;
    private Integer id;
    private String nickname;
    private Integer currentHp;
    private Species species;
    private Stats currentStats;
    private Integer currentLevel;
    private Integer currentExperience;
    private final Move[] moves = new Move[4];
    private final List<StatusEffect> statusEffects = new ArrayList<>();



    public Boolean isAlive(){
        return currentHp>0;
    }

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
}