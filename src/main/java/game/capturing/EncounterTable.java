package game.capturing;

import game.creature.Pokemon;
import game.creature.Specie;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class EncounterTable
{
    private record ProbabilityEntry (
        double probability,
        int minLevel,
        int maxLevel
    ) { }

    private static class EncounterTableBuilder {
        private record Entry (Specie pokemon, int minLevel, int maxLevel) {}
        private final Map<Integer, Entry> map = new TreeMap<>();
        private int raritySum = 0;
        public EncounterTableBuilder addSpecie(
                Specie pokemon, int rarity, int minLevel, int maxLevel
        )
        {
            if (rarity < 0) throw new IllegalArgumentException("Rarity must be greater than 0");
            if (minLevel > maxLevel) {
                int aux = minLevel;
                minLevel = maxLevel;
                maxLevel = aux;
            }
            map.put(-rarity, new Entry(pokemon, minLevel, maxLevel));
            raritySum += rarity;
            return this;
        }

        public EncounterTable build()
        {
            var ans = new HashMap<Specie, ProbabilityEntry>();
            double ant = 0;
            for (var item : map.entrySet())
            {
                double prob = ant + (double)item.getKey()/raritySum;
                ans.put(
                        item.getValue().pokemon(),
                        new ProbabilityEntry(
                                prob,
                                item.getValue().minLevel(),
                                item.getValue().maxLevel()
                        )
                );
                ant = prob;
            }
            return new EncounterTable(ans);
        }
    }

    private final Map<Specie, ProbabilityEntry> table;
    private EncounterTable(Map<Specie, ProbabilityEntry> map) { table = map; }

    public Pokemon sortSpecie()
    {
        double prob = Math.random();
        for (var item : table.entrySet())
        {
            if (item.getValue().probability() >= prob)
            {
                int deltaLevel = item.getValue().maxLevel() - item.getValue().minLevel();
                var r = new Random();
                int level = item.getValue().minLevel() + r.nextInt(deltaLevel);
                return new Pokemon(null, item.getKey(), level);
            }
        }
        throw new IllegalStateException("Probabilities do not sum 1");
    }
}
