package game.creature;

import game.creature.move.Move;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Specie {
    private final Integer dexNumber;
    private final String name;
    private final Stats baseStats;
    private final ElementType primaryType;
    private final ElementType secondaryType;
    private final Specie evolution;
    private final TreeMap<Integer,Move> movePool = new TreeMap<>();
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

    public BufferedImage getFrontSprite() {
        return frontSprite;
    }

    public BufferedImage getBackSprite() {
        return backSprite;
    }

    public Specie getEvolution() {
        return evolution;
    }

    public void loadMovePool(Map<Integer, Move> pool) {
        movePool.clear();
        if (pool != null) movePool.putAll(pool);
    }

    public Collection<Move> resolveMovesForLevel(Integer level){
        return movePool.headMap(level + 1).values();
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
            return other.dexNumber == null;
        } else return dexNumber.equals(other.dexNumber);
    }

    public String getTypes() {
        return primaryType + " / " + secondaryType;
    }
}
