package game.creature;

@FunctionalInterface
public interface PokemonClickAction {
    void execute(Pokemon pokemon);
}
