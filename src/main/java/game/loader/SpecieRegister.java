package game.loader;

import java.util.HashMap;
import java.util.Map;

import game.creature.Specie;

public class SpecieRegister {
    private static final Map<Integer, Specie> specieIdMap = new HashMap<>();
    private static final Map<String, Specie> specieNameMap = new HashMap<>();
    
    public static Specie getSpecie(String name) { return specieNameMap.getOrDefault(name, null); }
    public static Specie getSpecie(Integer dexId) { return specieIdMap.getOrDefault(dexId, null); }
}
