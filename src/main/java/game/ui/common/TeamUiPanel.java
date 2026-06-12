package game.ui.common;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.battle.Team;
import game.creature.Pokemon;
import game.creature.move.Move;
import game.creature.move.StatType;

import java.awt.Color;
import java.awt.Font;

public class TeamUiPanel extends UiImage
{
    private final Team team;
    private Pokemon selectedPokemon;

    private final UiImage listContainer;
    private final UiButton[] memberButtons = new UiButton[6];

    private final UiText nameTxt;
    private final UiText levelTxt;
    private final UiText hpTxt;
    private final UiText[] statLines = new UiText[5];
    private final UiText[] moveLines = new UiText[4];
    private final UiImage pokemonSpriteView;

    public TeamUiPanel(int sizeX, int sizeY, Color backgroundColor, Team team)
    {
        super(sizeX, sizeY, backgroundColor);
        this.team = team;

        getUiTransform().setAnchor(Anchor.CENTER);
        getUiTransform().setPosition(0, 0);

        int listW = (int) (sizeX * 0.4);
        listContainer = new UiImage(listW, sizeY - 20, new Color(32, 36, 45));
        listContainer.getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        listContainer.getUiTransform().setPosition(10, 0);
        addChild(listContainer);

        int detailsW = (int) (sizeX * 0.56);
        UiImage detailsContainer = new UiImage(detailsW, sizeY - 20, new Color(48, 54, 66));
        detailsContainer.getUiTransform().setAnchor(Anchor.CENTER_RIGHT);
        detailsContainer.getUiTransform().setPosition(-10, 0);
        addChild(detailsContainer);

        pokemonSpriteView = new UiImage(120, 120, new Color(0, 0, 0, 0));
        pokemonSpriteView.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        pokemonSpriteView.getUiTransform().setPosition(20, 20);
        detailsContainer.addChild(pokemonSpriteView);

        nameTxt = new UiText("");
        nameTxt.setFont("Arial", Font.BOLD, 18);
        nameTxt.setColor(Color.WHITE);
        nameTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        nameTxt.getUiTransform().setPosition(160, 25);
        detailsContainer.addChild(nameTxt);

        levelTxt = new UiText("");
        levelTxt.setFont("Arial", Font.PLAIN, 14);
        levelTxt.setColor(new Color(200, 200, 200));
        levelTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        levelTxt.getUiTransform().setPosition(160, 50);
        detailsContainer.addChild(levelTxt);

        hpTxt = new UiText("");
        hpTxt.setFont("Arial", Font.BOLD, 14);
        hpTxt.setColor(new Color(105, 230, 105));
        hpTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        hpTxt.getUiTransform().setPosition(160, 75);
        detailsContainer.addChild(hpTxt);

        UiText statsTitleTxt = new UiText("STATS");
        statsTitleTxt.setFont("Arial", Font.BOLD, 14);
        statsTitleTxt.setColor(new Color(241, 196, 15));
        statsTitleTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        statsTitleTxt.getUiTransform().setPosition(20, 155);
        detailsContainer.addChild(statsTitleTxt);

        for (int i = 0; i < statLines.length; i++) {
            statLines[i] = new UiText("");
            statLines[i].setFont("Consolas", Font.PLAIN, 13);
            statLines[i].setColor(Color.WHITE);
            statLines[i].getUiTransform().setAnchor(Anchor.TOP_LEFT);
            statLines[i].getUiTransform().setPosition(20, 180 + (i * 20));
            detailsContainer.addChild(statLines[i]);
        }

        UiText movesTitle = new UiText("MOVESET");
        movesTitle.setFont("Arial", Font.BOLD, 14);
        movesTitle.setColor(new Color(52, 152, 219));
        movesTitle.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        movesTitle.getUiTransform().setPosition(-120, 155);
        detailsContainer.addChild(movesTitle);

        for (int i = 0; i < moveLines.length; i++) {
            moveLines[i] = new UiText("");
            moveLines[i].setFont("Arial", Font.PLAIN, 13);
            moveLines[i].setColor(Color.WHITE);
            moveLines[i].getUiTransform().setAnchor(Anchor.TOP_RIGHT);
            moveLines[i].getUiTransform().setPosition(-120, 180 + (i * 22));
            detailsContainer.addChild(moveLines[i]);
        }

        setupMembersList();
        updateSelection(team.getActiveMember());
    }

    private void setupMembersList()
    {
        listContainer.removeAllChildren();

        int btnH = (int) ((listContainer.getTransform().getScale().y() - 30) / 6);
        var members = team.getMembers();

        for (int i = 0; i < 6; i++)
        {
            if (i < members.size())
            {
                Pokemon p = members.get(i);
                String label = p.getNickname() + "  Lvl " + p.getCurrentLevel();

                memberButtons[i] = new UiButton(label, () -> updateSelection(p));

                if (!p.isAlive()) {
                    memberButtons[i].setBackgroundColor(new Color(70, 40, 40));
                    memberButtons[i].setForegroundColor(Color.GRAY);
                } else if (i == team.getActiveIndex()) {
                    memberButtons[i].setBackgroundColor(new Color(43, 81, 120));
                    memberButtons[i].setForegroundColor(Color.WHITE);
                } else {
                    memberButtons[i].setBackgroundColor(new Color(45, 52, 66));
                    memberButtons[i].setForegroundColor(Color.WHITE);
                }
            } else {
                memberButtons[i] = new UiButton("[ VAZIO ]", () -> {});
                memberButtons[i].setBackgroundColor(new Color(24, 26, 32));
                memberButtons[i].setForegroundColor(new Color(80, 80, 80));
            }

            memberButtons[i].getTransform().setScale(listContainer.getTransform().getScale().x() - 20, btnH - 4);
            memberButtons[i].getUiTransform().setAnchor(Anchor.CENTER_TOP);
            memberButtons[i].getUiTransform().setPosition(0, 10 + (i * btnH));

            listContainer.addChild(memberButtons[i]);
        }
    }

    public void updateSelection(Pokemon pokemon)
    {
        if (pokemon == null) return;
        this.selectedPokemon = pokemon;

        String specieName = pokemon.getSpecie().getName();
        String typesStr = pokemon.getSpecie().getTypes();

        if (pokemon.getNickname().equals(specieName)) {
            nameTxt.setText(pokemon.getNickname() + " (" + typesStr + ")");
        } else {
            nameTxt.setText(pokemon.getNickname() + " [" + specieName + "] (" + typesStr + ")");
        }

        levelTxt.setText("Level: " + pokemon.getCurrentLevel() + "  |  Exp: " + (pokemon.getCurrentExperience() != null ? pokemon.getCurrentExperience() : 0));

        int maxHp = pokemon.getCurrentStats().getValue(StatType.HP);
        hpTxt.setText("HP: " + pokemon.getCurrentHp() + " / " + maxHp);

        double hpRatio = (double) pokemon.getCurrentHp() / maxHp;
        if (hpRatio <= 0.2) hpTxt.setColor(Color.RED);
        else if (hpRatio <= 0.5) hpTxt.setColor(Color.YELLOW);
        else hpTxt.setColor(new Color(105, 230, 105));

        if (pokemon.getSpecie().getFrontSprite() != null) {
            pokemonSpriteView.setImage(pokemon.getSpecie().getFrontSprite());
        }

        var stats = pokemon.getCurrentStats();
        String[] prefix = {"ATK: ", "DEF: ", "SP. ATK: ", "SP. DEF: ", "SPEED: "};
        for (var stat : StatType.values()) {
            if (stat == StatType.HP) continue;
            statLines[stat.ordinal() - 1].setText(prefix[stat.ordinal() - 1] + stats.getValue(stat));
        }

        Move[] moves = pokemon.getMoves();
        for (int i = 0; i < 4; i++)
        {
            if (i < moves.length && moves[i] != null) {
                moveLines[i].setText("» " + moves[i].getName() + " [" + moves[i].getPp() + " PP]");
            } else {
                moveLines[i].setText("» -");
            }
        }
    }

    public void refresh() {
        setupMembersList();
        if (selectedPokemon != null) {
            updateSelection(selectedPokemon);
        }
    }
}