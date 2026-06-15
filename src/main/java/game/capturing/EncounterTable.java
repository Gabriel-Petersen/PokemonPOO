package game.capturing;

import game.creature.Pokemon;
import game.creature.Specie;

import java.util.*;

public class EncounterTable
{
    private record ProbabilityEntry (
            Specie specie,
            double minProb,
            double maxProb,
            int minLevel,
            int maxLevel
    ) { }

    public static class Builder
    {
        private record RawEntry (Specie pokemon, int rarity, int minLevel, int maxLevel) {}
        private final List<RawEntry> entries = new ArrayList<>();
        private int raritySum = 0;

        public static Builder create() { return new Builder(); }

        public Builder addSpecie(Specie pokemon, int rarity, int minLevel, int maxLevel)
        {
            if (rarity <= 0) throw new IllegalArgumentException("Rarity must be greater than 0");
            if (minLevel > maxLevel) {
                int aux = minLevel;
                minLevel = maxLevel;
                maxLevel = aux;
            }

            entries.add(new RawEntry(pokemon, rarity, minLevel, maxLevel));
            raritySum += rarity;
            return this;
        }

        public EncounterTable build()
        {
            if (entries.isEmpty()) throw new IllegalStateException("Cannot build an empty encounter table");

            List<ProbabilityEntry> probabilityTable = new ArrayList<>();
            double currentOffset = 0.0;

            for (var entry : entries)
            {
                double step = (double) entry.rarity() / raritySum;
                double nextOffset = currentOffset + step;

                probabilityTable.add(new ProbabilityEntry(
                        entry.pokemon(),
                        currentOffset,
                        nextOffset,
                        entry.minLevel(),
                        entry.maxLevel()
                ));

                currentOffset = nextOffset;
            }

            return new EncounterTable(probabilityTable);
        }
    }

    private final List<ProbabilityEntry> table;
    private final Random random = new Random();

    private EncounterTable(List<ProbabilityEntry> table) {
        this.table = table;
    }

    public Pokemon sortSpecie()
    {
        double roll = random.nextDouble();

        for (var entry : table)
        {
            if (roll >= entry.minProb() && roll < entry.maxProb())
            {
                int range = (entry.maxLevel() - entry.minLevel()) + 1;
                int selectedLevel = entry.minLevel() + random.nextInt(range);

                return new Pokemon(null, entry.specie(), selectedLevel);
            }
        }

        var lastEntry = table.getLast();
        int range = (lastEntry.maxLevel() - lastEntry.minLevel()) + 1;
        return new Pokemon(
                null, lastEntry.specie(), lastEntry.minLevel() + random.nextInt(range)
        );
    }
}
