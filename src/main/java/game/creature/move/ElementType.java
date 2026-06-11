package game.creature.move;

import java.util.Set;

public enum ElementType {
    FIRE {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(WATER, ROCK);
        }
    },
    WATER {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(GRASS, ELECTRIC);
        }
    },
    GRASS {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FIRE, DRAGON);
        }
    },
    ROCK {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FIGHTING, GRASS, WATER);
        }
    },
    NORMAL {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FIGHTING);
        }
    },
    ELECTRIC {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(ROCK, DRAGON);
        }
    },
    FIGHTING {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FAIRY, POISON);
        }
    },
    POISON {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(ROCK, WATER, GRASS);
        }
    },
    DRAGON {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(FAIRY);
        }
    },
    FAIRY {
        @Override
        public Set<ElementType> getWeaknesses() {
            return Set.of(NORMAL, POISON);
        }
    },
    NONE {
        @Override public Set<ElementType> getWeaknesses() { return Set.of(); }
    };

    public abstract Set<ElementType> getWeaknesses();
}
