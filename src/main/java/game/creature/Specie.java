package game.creature;

import game.creature.move.ElementType;
import game.creature.move.Move;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Specie {
    private final Integer dexNumber;
    private final String name;
    private final Stats baseStats;
    private final ElementType primaryType;
    private final ElementType secondaryType;
    private final Specie evolution;
    private final Map<Integer,Move> movePool = new HashMap<>();
    private final BufferedImage frontSprite;
    private final BufferedImage backSprite;

    public int weaknessTo(ElementType element){
        if (primaryType.getWeaknesses().contains(element)) {
            if (secondaryType != null && secondaryType.getWeaknesses().contains(element)) {
                return 4;
            }
            return 2;
        }
        if (secondaryType != null && secondaryType.getWeaknesses().contains(element)) {
            return 2;
        }
        return 1;
    }

    public Specie (
        Integer dexNumber, String name, Stats baseStats, ElementType primaryType, ElementType secondaryType, BufferedImage frontSprite, BufferedImage backSprite
    ) 
    {
        this.dexNumber = dexNumber;
        this.name = name;
        this.baseStats = baseStats;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.frontSprite = frontSprite;
        this.backSprite = backSprite;
        this.evolution = null;
    }

    public Specie (
        Integer dexNumber, String name, Stats baseStats, ElementType primaryType, ElementType secondaryType, Specie evolution, BufferedImage frontSprite, BufferedImage backSprite
    ) 
    {
        this.dexNumber = dexNumber;
        this.name = name;
        this.baseStats = baseStats;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.evolution = evolution;
        this.frontSprite = frontSprite;
        this.backSprite = backSprite;
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

    public BufferedImage getfrontSprite() {
        return frontSprite;
    }

    public BufferedImage getBackSprite() {
        return backSprite;
    }

    public Specie getEvolution() {
        return evolution;
    }

    public void loadMovepool(Map<Integer, Move> pool) {
        movePool.clear();
        if (pool != null) movePool.putAll(pool);
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

    @Override
    public int hashCode() { return dexNumber.hashCode(); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Specie other = (Specie) obj;
        if (dexNumber == null) {
            if (other.dexNumber != null)
                return false;
        } else if (!dexNumber.equals(other.dexNumber))
            return false;
        return true;
    }
}
