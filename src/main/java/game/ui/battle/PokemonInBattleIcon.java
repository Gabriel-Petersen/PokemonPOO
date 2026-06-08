package game.ui.battle;

import engine.ui.elements.UiImage;
import engine.ui.elements.UiProgressBar;
import engine.ui.elements.UiText;
import engine.ui.elements.UiProgressBar.Direction;
import game.creature.Pokemon;

public class PokemonInBattleIcon extends UiImage {
    private UiProgressBar hpBar;
    private UiText pokemonName;
    private Pokemon source;

    private PokemonInBattleIcon(Pokemon source, boolean isBackSprite) 
    {
        super(isBackSprite ? source.getSpecie().getBackSprite() : source.getSpecie().getfrontSprite());
        this.source = source;
        hpBar = new UiProgressBar(Direction.LEFT2RIGHT);
        hpBar.setProgress(source.getCurrentHp());
        if (source.getNickname() == null || source.getNickname().isBlank()) 
            pokemonName = new UiText(source.getSpecie().getName());
        else
            pokemonName = new UiText(source.getNickname());

        addChild(hpBar);
        addChild(pokemonName);
    }
    public static PokemonInBattleIcon buildPlayerIcon(Pokemon source) { return new PokemonInBattleIcon(source, true); }
    public static PokemonInBattleIcon buildNpcIcon(Pokemon source) { return new PokemonInBattleIcon(source, false); }

    public Pokemon getSource() { return source; }
    public UiProgressBar getHpBar() { return hpBar; }
    public UiText getPokemonName() { return pokemonName; }
}
