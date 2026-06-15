package game.creature;

import java.awt.*;
import java.util.Set;

public enum ElementType {
    FIRE {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(WATER, ROCK);
        }
        @Override public Color getDisplayColor() { return Color.red; }
    },
    WATER {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(GRASS, ELECTRIC);
        }
        @Override public Color getDisplayColor() { return Color.blue; }
    },
    GRASS {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FIRE, POISON);
        }
        @Override public Color getDisplayColor() { return Color.green; }
    },
    ROCK {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FIGHTING, GRASS, WATER);
        }
        @Override public Color getDisplayColor() { return new Color(86, 33, 3); }
    },
    NORMAL {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FIGHTING);
        }
        @Override public Color getDisplayColor() { return null; }
    },
    ELECTRIC {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(ROCK, DRAGON);
        }
        @Override public Color getDisplayColor() { return Color.yellow; }
    },
    FIGHTING {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FAIRY, POISON);
        }
        @Override public Color getDisplayColor() { return new Color(255, 141, 24); }
    },
    POISON {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(ROCK, WATER, FAIRY);
        }
        @Override public Color getDisplayColor() { return new Color(160, 32, 240); }
    },
    DRAGON {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FAIRY, DRAGON);
        }
        @Override public Color getDisplayColor() { return Color.cyan; }
    },
    FAIRY {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(ROCK, POISON);
        }
        @Override public Color getDisplayColor() { return Color.pink; }
    },
    NONE {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of();
        }
        @Override public Color getDisplayColor() { return null; }
    };

    public abstract Set<ElementType> getWeaknesses();
    public abstract Color getDisplayColor();
}
