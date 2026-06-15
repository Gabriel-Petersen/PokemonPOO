package execs;

import engine.assets.AssetManager;
import engine.core.GamePanel;
import engine.input.Input;
import game.battle.Team;
import game.capturing.ETRegistry;
import game.capturing.WildEncounterUpdater;
import game.creature.Pokemon;
import game.entities.Npc;
import game.entities.NpcSeller;
import game.entities.NpcTrainer;
import game.itemsystem.Inventory;
import game.loader.ItemRegistry;
import game.loader.SpecieRegister;
import game.player.Player;
import game.scenario.GameMap;
import game.ui.common.MainMenu;
import game.ui.common.CharacterCreationMenu; 
import game.ui.common.StarterSelectionMenu;
import java.awt.Color;
import java.util.Objects;
import javax.swing.JFrame;

public class Main
{
    public static void buildMap(String playerName, int starterId, String pokemonNickname)
    {
        GamePanel gamePanel = GamePanel.getInstance();

        GameMap tilemap = getTilemap(gamePanel);

        WildEncounterUpdater encounterUpdater = new WildEncounterUpdater();
        encounterUpdater.setGameMap(tilemap);
        gamePanel.addElement(encounterUpdater);
        encounterUpdater.registerEncounterArea(3098, 5049, 3149, 4860, ETRegistry.TABLE_0);

        Player player = getPlayer(tilemap, gamePanel, playerName, starterId, pokemonNickname);

        encounterUpdater.registerPlayer(player);

        Npc npc1 = new Npc("Certinho", "npcs/ingame/npc_ingame01.png");
        npc1.getTransform().setScale(3, 3);
        npc1.getTransform().setPosition(2856, 5522);
        String[] npc1Message = {"Olá certinho!", "Vamos abrir o Eclipse para fazer nosso programinha em Java?", "E o Grêmio?"};
        npc1.setMessage(npc1Message);
        gamePanel.addElement(npc1);

        NpcTrainer npc2 = new NpcTrainer(new Inventory(), new Team(), "sei la", "npcs/ingame/npc_ingame05.png", "npcs/battle/npc_battle01.png");
        npc2.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(1), 5));
        npc2.getCurrent().setOwner(true);
        npc2.getTransform().setScale(3, 3);
        npc2.getTransform().setPosition(3000, 5522);
        String[] npc2Message = {"Vamos lutar!"};
        npc2.setMessage(npc2Message);
        gamePanel.addElement(npc2);

        NpcTrainer npc3 = new NpcTrainer(new Inventory(), new Team(), "sei la", "npcs/ingame/npc_ingame05.png", "npcs/battle/npc_battle01.png");
        npc3.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie("DEBUG"), 10));
        npc3.getCurrent().setOwner(true);
        npc3.getTransform().setScale(3, 3);
        npc3.getTransform().setPosition(3300, 5522);
        npc3.setMessage(null);
        gamePanel.addElement(npc3);

        NpcSeller shopkeeper = new NpcSeller("Vendedor", "npcs/ingame/npc_ingame03.png");
        shopkeeper.getTransform().setScale(3, 3);
        shopkeeper.getTransform().setPosition(3100, 5522);
        shopkeeper.getInventory().add(ItemRegistry.smallPotion, 5);
        shopkeeper.getInventory().add(ItemRegistry.mediumPotion, 2);
        gamePanel.addElement(shopkeeper);
    }

    private static GameMap getTilemap(GamePanel gamePanel)
    {
        GameMap tilemap = new GameMap(
                Objects.requireNonNull(AssetManager.getSprite("scenario/mapa_limited.png")),
                AssetManager.getSprite("scenario/collision_matrix_map.png"),
                AssetManager.getSprite("scenario/grass_matrix_map.png"),
                16
        );
        tilemap.setLayer(-1);
        tilemap.getTransform().setScale(3, 3);
        tilemap.getTransform().setPosition(-4 * tilemap.getSizeX(), -4 * tilemap.getSizeY());
        gamePanel.addElement(tilemap);
        return tilemap;
    }

    private static Player getPlayer(GameMap tilemap, GamePanel gamePanel, String playerName, int starterId, String pokemonNickname) {
        Player player = new Player();
        player.getTransform().setScale(1.8, 1.8);
        player.setCurrentMap(tilemap);
        player.getTransform().setPosition(2806, 5522);
        
        player.getMetadata().setName(playerName);
        
        var starterSpecie = SpecieRegister.getSpecie(starterId);
        player.getTeam().addMember(new Pokemon(pokemonNickname, starterSpecie, 5));
        player.getCurrent().setOwner(true);
        
        gamePanel.addElement(player);
        
        var inv = player.getInventory();
        inv.add(ItemRegistry.smallPotion, 1);
        inv.add(ItemRegistry.mediumPotion, 10);
        inv.add(ItemRegistry.pokeBall, 10);
        return player;
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Minha janela");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = GamePanel.getInstance();
        gamePanel.setBackground(Color.DARK_GRAY);
        frame.setContentPane(gamePanel);
        frame.pack();
        frame.setVisible(true);

        MainMenu menu = MainMenu.getInstance();
        
        menu.setOnStart(() -> {
            menu.hide();

            CharacterCreationMenu nameMenu = new CharacterCreationMenu(chosenName -> {
                StarterSelectionMenu starterMenu = new StarterSelectionMenu((chosenStarterId, chosenNickname) -> buildMap(chosenName, chosenStarterId, chosenNickname));
                starterMenu.show();
            });
            nameMenu.show();
        });
        
        menu.show();

        new Thread(() -> {
            long lastTime = System.nanoTime();
            do
            {
                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
                lastTime = currentTime;

                gamePanel.updateAll(deltaTime);
                gamePanel.repaint();
                Input.endFrame();

                try { Thread.sleep(16); } catch (InterruptedException ignored) {}
            } while (true);
        }).start();
    }
}