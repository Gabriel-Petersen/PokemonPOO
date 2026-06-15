package game.ui.battle;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiProgressBar;
import engine.ui.elements.UiProgressBar.Direction;
import engine.ui.elements.UiText;
import game.creature.Pokemon;
import game.creature.StatType;
import java.awt.Color;
import java.awt.Font;

public class PokemonInBattleIcon extends UiImage 
{
    private UiProgressBar hpBar;
    private UiText pokemonName;
    private Pokemon source;
    private UiImage statusPanel;
    private final boolean isBackSprite;

    private PokemonInBattleIcon(Pokemon source, boolean isBackSprite) 
    {
        super(isBackSprite ? source.getSpecie().getBackSprite() : source.getSpecie().getFrontSprite());
        this.source = source;
        this.isBackSprite = isBackSprite;
        
        getTransform().setScale(160, 160);

        setupStatusPanel(isBackSprite);
    }

    private void setupStatusPanel(boolean isBackSprite) 
    {
        statusPanel = new UiImage(180, 65, new Color(30, 30, 30, 180));
        
        statusPanel.getUiTransform().setAnchor(isBackSprite ? Anchor.CENTER_RIGHT : Anchor.CENTER_LEFT);
        statusPanel.getUiTransform().setPosition(isBackSprite ? 140 : -140, -20);
        addChild(statusPanel);

        String nameStr = (source.getNickname() == null || source.getNickname().isBlank()) 
                ? source.getSpecie().getName() : source.getNickname();
        
        pokemonName = new UiText(nameStr + "  Lv." + source.getCurrentLevel());
        pokemonName.setFont("Arial", Font.BOLD, 14);
        pokemonName.setColor(Color.WHITE);
        pokemonName.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        pokemonName.getUiTransform().setPosition(10, 8);
        statusPanel.addChild(pokemonName);

        hpBar = new UiProgressBar(Direction.LEFT2RIGHT);
        hpBar.getTransform().setScale(160, 12);
        hpBar.getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
        hpBar.getUiTransform().setPosition(0, -10);
        hpBar.setFillColor(Color.green);
        
        double hpPercent = (double) source.getCurrentHp() / source.getCurrentStats().getValue(StatType.HP);
        hpBar.setProgress(hpPercent);
        
        statusPanel.addChild(hpBar);
    }

    public static PokemonInBattleIcon buildPlayerIcon(Pokemon source) { return new PokemonInBattleIcon(source, true); }
    public static PokemonInBattleIcon buildNpcIcon(Pokemon source) { return new PokemonInBattleIcon(source, false); }

    public Pokemon getSource() { return source; }
    public UiProgressBar getHpBar() { return hpBar; }
    public UiText getPokemonName() { return pokemonName; }

    public void setSource(Pokemon source)
    {
        this.source = source;
        setImage(isBackSprite ? source.getSpecie().getBackSprite() : source.getSpecie().getFrontSprite());
    }
}