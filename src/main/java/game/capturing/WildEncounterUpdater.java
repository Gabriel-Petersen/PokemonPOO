package game.capturing;

import engine.core.GamePanel;
import engine.lifecycle.Updatable;
import game.battle.BattleSession;
import game.creature.Pokemon;
import game.player.Player;
import game.scenario.GameMap;

import java.util.HashMap;
import java.util.Map;

public class WildEncounterUpdater implements Updatable
{
    private record TileCoordinate(int tileX, int tileY) { }

    private static final int FRAMES_TO_ROLL = 70; // Com approx. 60FPS, temos 1 segundo e meio
    private static final double ENCOUNTER_CHANCE = 0.225;
    private final Map<TileCoordinate, EncounterTable> tableMap = new HashMap<>();

    private Player player;
    private GameMap gameMap;
    private int currFrameCount = 0;

    public void registerEncounterArea(
            double startWorldX, double startWorldY, double endWorldX, double endWorldY, EncounterTable table
    )
    {
        if (gameMap == null)
            throw new IllegalStateException("Não é possível registrar áreas de encontro antes de definir o GameMap!");

        int[] startTiles = gameMap.worldToTileCords(startWorldX, startWorldY);
        int[] endTiles = gameMap.worldToTileCords(endWorldX, endWorldY);

        int startTileX = startTiles[0];
        int startTileY = startTiles[1];
        int endTileX = endTiles[0];
        int endTileY = endTiles[1];

        if (startTileX > endTileX) { int aux = startTileX; startTileX = endTileX; endTileX = aux; }
        if (startTileY > endTileY) { int aux = startTileY; startTileY = endTileY; endTileY = aux; }

        for (int x = startTileX; x <= endTileX; x++)
            for (int y = startTileY; y <= endTileY; y++)
                tableMap.put(new TileCoordinate(x, y), table);
    }

    @Override public void setup() { }
    public void registerPlayer(Player player) { this.player = player; }
    public void setGameMap(GameMap gameMap) { this.gameMap = gameMap; }

    @Override
    public void update()
    {
        if (player == null || gameMap == null) return;

        if (player.isTalking() || player.isBattling() || player.isUiOpen()) { return; }

        var footPos = player.getFootPosition();

        if (gameMap.isTallGrassAt(footPos[0], footPos[1]))
        {
            if (currFrameCount >= FRAMES_TO_ROLL)
            {
                if (!player.getTeam().hasAvailableMember()) {
                    currFrameCount = 0;
                    return;
                }

                int[] tileCords = gameMap.worldToTileCords(footPos[0], footPos[1]);

                EncounterTable currentTable = tableMap.get(new TileCoordinate(tileCords[0], tileCords[1]));

                if (currentTable != null && Math.random() <= ENCOUNTER_CHANCE)
                {
                    Pokemon wildPokemon = currentTable.sortSpecie();
                    startWildEncounter(wildPokemon);
                }
                currFrameCount = 0;
            }
            else currFrameCount++;
        }
        else currFrameCount = 0;

    }

    private void startWildEncounter(Pokemon wildPokemon)
    {
        System.out.println("Batalha iniciada contra: " + wildPokemon.getNickname() + " Nível " + wildPokemon.getCurrentLevel());

        player.setBattling(true);
        var nature = new WildTrainer(wildPokemon);
        var session = new BattleSession(player, nature);
        GamePanel.getInstance().addElement(session);
    }
}
