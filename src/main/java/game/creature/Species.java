package game.creature;

import game.creature.move.ElementType;
import game.creature.move.Move;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Species {
    private final Integer dexNumber;
    private final String name;
    private final Stats baseStats;
    private final ElementType primaryType;
    private final ElementType secondaryType;
    private Species evolution;
    private final Map<Integer,Move> movePool = new HashMap<>();
    private final BufferedImage sprite;

    public Species(Stats baseStats, Integer dexNumber, String name, ElementType primaryType, ElementType secondaryType) {
        this.baseStats = baseStats;
        this.dexNumber = dexNumber;
        this.name = name;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.sprite = null;
    }
    
    public Integer getDexNumber() {
        return dexNumber;
    }

    public String getName() {
        return name;
    }

    public Stats getBaseStats() {
        return baseStats;
    }

    public ElementType getPrimaryType() {
        return primaryType;
    }

    public ElementType getSecondaryType() {
        return secondaryType;
    }

    public Map<Integer, Move> getMovePool() {
        return movePool;
    }

    public Boolean hasType(ElementType type){
        return primaryType == type || secondaryType == type;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public Species getEvolution() {
        return evolution;
    }

    public void setEvolution(Species evolution) {
        this.evolution = evolution;
    }

    public List<Move> resolveMovessForLevel(Integer level){
        List<Move> moves = new ArrayList<>();
        for(int i=0;i<=level;i++){
            Move newMove = movePool.getOrDefault(i, null);
            if(newMove!=null){
                moves.add(newMove);
            }
        }
        return moves;
            
    }

    

    
}
