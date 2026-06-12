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
    private final UiImage detailsContainer;
    private final UiButton[] memberButtons = new UiButton[6];

    // Detalhes do Pokémon
    private final UiText nameTxt;
    private final UiText specieTypeTxt;
    private final UiText levelTxt;
    private final UiText hpTxt;
    private final UiText[] statLines = new UiText[5];
    private final UiImage pokemonSpriteView;

    // Grid de Moves e Painel de Detalhes do Move selecionado
    private final UiButton[] moveButtons = new UiButton[4];
    private final MoveDetailsTooltip moveDetailsTooltip;

    public TeamUiPanel(int sizeX, int sizeY, Color backgroundColor, Team team)
    {
        super(sizeX, sizeY, backgroundColor);
        this.team = team;

        getUiTransform().setAnchor(Anchor.CENTER);
        getUiTransform().setPosition(0, 0);

        // CONTAINER ESQUERDO: Lista de Membros
        int listW = (int) (sizeX * 0.4);
        listContainer = new UiImage(listW, sizeY - 20, new Color(32, 36, 45));
        listContainer.getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        listContainer.getUiTransform().setPosition(10, 0);
        addChild(listContainer);

        // CONTAINER DIREITO: Detalhes gerais do monstro
        int detailsW = (int) (sizeX * 0.56);
        detailsContainer = new UiImage(detailsW, sizeY - 20, new Color(48, 54, 66));
        detailsContainer.getUiTransform().setAnchor(Anchor.CENTER_RIGHT);
        detailsContainer.getUiTransform().setPosition(-10, 0);
        detailsContainer.setVisible(false); // Nasce oculto para evitar textos órfãos de "STATS" e "MOVESET"
        addChild(detailsContainer);

        // Viewport do Sprite
        pokemonSpriteView = new UiImage(100, 100, new Color(0, 0, 0, 0));
        pokemonSpriteView.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        pokemonSpriteView.getUiTransform().setPosition(15, 15);
        detailsContainer.addChild(pokemonSpriteView);

        // Linha 1: Apenas Nickname
        nameTxt = new UiText("");
        nameTxt.setFont("Arial", Font.BOLD, 18);
        nameTxt.setColor(Color.WHITE);
        nameTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        nameTxt.getUiTransform().setPosition(130, 20);
        detailsContainer.addChild(nameTxt);

        // Linha 2: Espécie + Elementos (Quebra de Linha Solicitada)
        specieTypeTxt = new UiText("");
        specieTypeTxt.setFont("Arial", Font.ITALIC, 13);
        specieTypeTxt.setColor(new Color(180, 190, 210));
        specieTypeTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        specieTypeTxt.getUiTransform().setPosition(130, 43);
        detailsContainer.addChild(specieTypeTxt);

        levelTxt = new UiText("");
        levelTxt.setFont("Arial", Font.PLAIN, 13);
        levelTxt.setColor(new Color(200, 200, 200));
        levelTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        levelTxt.getUiTransform().setPosition(130, 64);
        detailsContainer.addChild(levelTxt);

        hpTxt = new UiText("");
        hpTxt.setFont("Arial", Font.BOLD, 13);
        hpTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        hpTxt.getUiTransform().setPosition(130, 83);
        detailsContainer.addChild(hpTxt);

        // Bloco de Atributos (Stats)
        UiText statsTitleTxt = new UiText("STATS");
        statsTitleTxt.setFont("Arial", Font.BOLD, 13);
        statsTitleTxt.setColor(new Color(241, 196, 15));
        statsTitleTxt.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        statsTitleTxt.getUiTransform().setPosition(20, 130);
        detailsContainer.addChild(statsTitleTxt);

        for (int i = 0; i < statLines.length; i++) {
            statLines[i] = new UiText("");
            statLines[i].setFont("Consolas", Font.PLAIN, 12);
            statLines[i].setColor(Color.WHITE);
            statLines[i].getUiTransform().setAnchor(Anchor.TOP_LEFT);
            statLines[i].getUiTransform().setPosition(20, 150 + (i * 18));
            detailsContainer.addChild(statLines[i]);
        }

        // Bloco do Moveset
        UiText movesTitle = new UiText("MOVESET");
        movesTitle.setFont("Arial", Font.BOLD, 13);
        movesTitle.setColor(new Color(52, 152, 219));
        movesTitle.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        movesTitle.getUiTransform().setPosition(-130, 130);
        detailsContainer.addChild(movesTitle);

        // Inicialização dos botões do Moveset

        moveDetailsTooltip = new MoveDetailsTooltip(detailsW - 30, 75);

        int moveBtnW = 125;
        int moveBtnH = 26;
        for (int i = 0; i < 4; i++) {
            final int index = i;
            moveButtons[i] = new UiButton("-", () -> {
                if (selectedPokemon != null && index < selectedPokemon.getMoves().length) {
                    Move move = selectedPokemon.getMoves()[index];
                    if (move != null) moveDetailsTooltip.displayMove(move);
                }
            });
            moveButtons[i].getTransform().setScale(moveBtnW, moveBtnH);
            moveButtons[i].getUiTransform().setAnchor(Anchor.TOP_RIGHT);
            moveButtons[i].getUiTransform().setPosition(-15, 150 + (i * 30));
            moveButtons[i].setBackgroundColor(new Color(40, 45, 55));
            moveButtons[i].setForegroundColor(Color.WHITE);
            for (var text : moveButtons[i].getAllChildrenFromType(UiText.class)) {
                text.setFont("Arial", Font.PLAIN, 12);
            }
            detailsContainer.addChild(moveButtons[i]);
        }

        moveDetailsTooltip.getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
        moveDetailsTooltip.getUiTransform().setPosition(0, -15);
        detailsContainer.addChild(moveDetailsTooltip);

        setupMembersList();
        if (team.hasAvailableMember()) {
            updateSelection(team.getActiveMember());
        }
    }

    private void setupMembersList()
    {
        listContainer.removeAllChildren();

        int containerH = (int) listContainer.getTransform().getScale().y();
        int btnH = (containerH - 30) / 6;
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
                    memberButtons[i].setBackgroundColor(new Color(53, 62, 78));
                    memberButtons[i].setForegroundColor(Color.WHITE);
                }

                // Acoplamento da Barra de Vida no botão de seleção do Monstro
                int maxHp = p.getCurrentStats().getValue(StatType.HP);
                UiHpBar hpBar = new UiHpBar(listContainer.getTransform().getScale().x() - 40, 6, p.getCurrentHp(), maxHp);
                hpBar.getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
                hpBar.getUiTransform().setPosition(0, -4);
                memberButtons[i].addChild(hpBar);

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
        
        // Torna visível o bloco agora que os dados existem de fato
        detailsContainer.setVisible(true);

        String specieName = pokemon.getSpecie().getName();
        String typesStr = pokemon.getSpecie().getTypes();

        // Alterado para aplicar a quebra de linha visual solicitada
        nameTxt.setText(pokemon.getNickname());
        specieTypeTxt.setText("[" + specieName + "]  •  (" + typesStr + ")");

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

        // Popula o Grid de Botões de Movimento
        Move[] moves = pokemon.getMoves();
        for (int i = 0; i < 4; i++) {
            if (i < moves.length && moves[i] != null) {
                moveButtons[i].setVisible(true);
                for (var text : moveButtons[i].getAllChildrenFromType(UiText.class)) {
                    text.setText(moves[i].getName());
                }
            } else {
                moveButtons[i].setVisible(true);
                for (var text : moveButtons[i].getAllChildrenFromType(UiText.class)) {
                    text.setText("-");
                }
            }
        }
        
        moveDetailsTooltip.clear();
    }

    public void refresh() {
        setupMembersList();
        if (selectedPokemon != null) {
            updateSelection(selectedPokemon);
        }
    }

    private static class UiHpBar extends UiImage 
    {
        public UiHpBar(double width, int height, int currentHp, int maxHp) 
        {
            super((int) width, height, new Color(35, 40, 50));
            
            double ratio = Math.max(0.0, Math.min(1.0, (double) currentHp / maxHp));
            int fillWidth = (int) (width * ratio);
            
            if (fillWidth > 0) {
                Color hpColor = new Color(105, 230, 105); // Verde
                if (ratio <= 0.2) hpColor = Color.RED;
                else if (ratio <= 0.5) hpColor = Color.YELLOW;

                UiImage fill = new UiImage(fillWidth, height, hpColor);
                fill.getUiTransform().setAnchor(Anchor.CENTER_LEFT);
                fill.getUiTransform().setPosition(0, 0);
                addChild(fill);
            }
        }
    }
}