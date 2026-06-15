package game.creature;

import game.battle.BattleContext;
import game.creature.move.Move;
import game.creature.move.status.StatusEffect;
import game.creature.move.status.VolatileStatusEffect;

import java.util.*;

public class Pokemon {
    private Integer currentAccuracy = 100;
    private String nickname;
    private Integer currentHp;
    private Specie specie;
    private Stats currentStats;
    private Integer currentLevel;
    private int currentExperience;
    private Boolean hasOwner = false;
    private final Move[] moves;
    private final List<StatusEffect> statusEffects = new ArrayList<>();

    private static Move[] getLast4Moves(Collection<Move> moves) 
    {
        Move[] mvs = new Move[4];
        int count = 0;

        for (Move move : moves) 
        {
            if (move == null) continue;
            mvs[count % 4] = move;
            count++;
        }
        return mvs;
    }
    
    public Pokemon(String nickname, Specie specie, Integer currentLevel) { 
        this(nickname, specie, currentLevel, getLast4Moves(specie.resolveMovesForLevel(currentLevel)));
    }

    public Pokemon(String nickname, Specie specie, Integer currentLevel, Move[] moves) {
        this.nickname = nickname;
        this.specie = specie;
        this.currentLevel = currentLevel;
        this.moves = moves;
        currentExperience = 0;
        currentStats = specie.getBaseStats().scaleForLevel(currentLevel);
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
    public int getCurrentExperience() {
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
    public void receiveDamage(Integer damage){ currentHp = Integer.max(0, currentHp - damage); }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public void heal(Integer heal){
        if(currentHp+heal<=currentStats.getValue(StatType.HP)){
            currentHp+=heal;
        }else{
            currentHp=currentStats.getValue(StatType.HP);
        }
    }

    public boolean gainExperience(int amount)
    {
        this.currentExperience += amount;
        int expNeeded = getRequiredExperienceForNextLevel();
        boolean leveledUp = false;

        while (this.currentExperience >= expNeeded)
        {
            this.currentExperience -= expNeeded;
            levelUp();
            leveledUp = true;
            expNeeded = getRequiredExperienceForNextLevel();
        }

        return leveledUp;
    }

    public int getRequiredExperienceForNextLevel() { return this.currentLevel * 30; }

    private void checkEvolution()
    {
        if (specie.getEvolution() != null)
            if (currentLevel >= 20 || (currentLevel >= 10 && specie.getEvolution().getEvolution() != null))
                specie = specie.getEvolution();
    }

    public Boolean applyStatus(StatusEffect statusEffect, BattleContext context){
        statusEffects.add(statusEffect);
        statusEffect.onApply(this, context);
        return true;
    }

    public Stats getEffectiveStats(BattleContext context){
        Stats effectiveStats = new Stats(currentStats);
        for(var ef:statusEffects){
            if(ef!=null){
                var statModifierRules = ef.getStatModifierRules();
                if(statModifierRules!=null){
                    for(var rule:statModifierRules){
                        effectiveStats = rule.applyOn(effectiveStats, context);
                    }
                }
            }
        }
        return effectiveStats;
    }

    public Integer getEffectiveStat(StatType statType, BattleContext context){
        return getEffectiveStats(context).getValue(statType);
    }

    public void resolveStatusAtTurnEnd(BattleContext context){
        for(var ef:statusEffects){
            if(ef!=null){
                ef.onTurnEnd(this, context);
            }
        }
        statusEffects.removeIf(ef -> ef == null || ef.isExpired());
    }

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
        checkEvolution();
        currentStats = specie.getBaseStats().scaleForLevel(currentLevel);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pokemon pokemon)) return false;
        return Objects.equals(nickname, pokemon.nickname) &&
                Objects.equals(specie, pokemon.specie) &&
                Objects.equals(currentStats, pokemon.currentStats) &&
                Objects.equals(currentLevel, pokemon.currentLevel) &&
                Objects.equals(hasOwner, pokemon.hasOwner) &&
                Objects.deepEquals(moves, pokemon.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, specie, currentStats, currentLevel, hasOwner, Arrays.hashCode(moves));
    }
}