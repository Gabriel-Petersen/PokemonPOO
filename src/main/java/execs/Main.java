package execs;

import engine.assets.AssetManager;
import engine.core.GamePanel;
import engine.input.Input;
import game.battle.Team;
import game.capturing.ETRegistry;
import game.capturing.WildEncounterUpdater;
import game.creature.Pokemon;
import game.entities.Npc;
import game.entities.NpcHealer;
import game.entities.NpcSeller;
import game.entities.NpcTrainer;
import game.itemsystem.Inventory;
import game.loader.ItemRegistry;
import game.loader.SpecieRegister;
import game.player.Player;
import game.scenario.GameMap;
import game.ui.common.CharacterCreationMenu;
import game.ui.common.MainMenu;
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
        encounterUpdater.registerEncounterArea(3223, 4868, 3547, 4693, ETRegistry.TABLE_0);
        encounterUpdater.registerEncounterArea(2593, 4882, 3018, 4692, ETRegistry.TABLE_0);
        encounterUpdater.registerEncounterArea(3078, 4534, 3351, 4309, ETRegistry.TABLE_1);
        encounterUpdater.registerEncounterArea(3264, 4005, 3547, 3780, ETRegistry.TABLE_1);
        encounterUpdater.registerEncounterArea(2977, 3669, 3551, 3440, ETRegistry.TABLE_1);
        encounterUpdater.registerEncounterArea(2790, 305, 3114, 80, ETRegistry.TABLE_2);
        encounterUpdater.registerEncounterArea(1249, 2370, 1530, 2147, ETRegistry.TABLE_3);
        encounterUpdater.registerEncounterArea(337, 2369, 661, 2149, ETRegistry.TABLE_3);
        


        Player player = getPlayer(tilemap, gamePanel, playerName, starterId, pokemonNickname);

        encounterUpdater.registerPlayer(player);

        Npc npc1 = new Npc("Certinho", "npcs/ingame/npc_ingame07.png");
        npc1.getTransform().setScale(3, 3);
        npc1.getTransform().setPosition(2803, 5459);
        String[] npc1Message = {"Olá certinho!", "Vamos abrir o Eclipse para fazer nosso programinha em Java?", "E o Grêmio?"};
        npc1.setMessage(npc1Message);
        gamePanel.addElement(npc1);

        Npc npc2 = new Npc("Soussa", "npcs/ingame/npc_ingame02.png");
        npc2.getTransform().setScale(3, 3);
        npc2.getTransform().setPosition(3143, 2149);
        String[] npc2Message = {"Olá!", "Deixa eu te contar um segredo...", "Ponteiro de ponteiro de ponteiro de ponteiro de ponteiro de ponteiro de ponteiro de ponteiro"};
        npc2.setMessage(npc2Message);
        gamePanel.addElement(npc2);

        Npc npc3 = new Npc("Tuta", "npcs/ingame/npc_ingame08.png");
        npc3.getTransform().setScale(3, 3);
        npc3.getTransform().setPosition(2759, 438);
        String[] npc3Message = {"Olá!", "Em algum lugar nessa área tem uns pokémon legais!"};
        npc3.setMessage(npc3Message);
        gamePanel.addElement(npc3);

        NpcHealer npcHealer1 = new NpcHealer("Enfermeira", "npcs/ingame/npc_ingame04.png");
        npcHealer1.getTransform().setScale(3, 3);
        npcHealer1.getTransform().setPosition(3200, 5172);
        gamePanel.addElement(npcHealer1);

        NpcHealer npcHealer2 = new NpcHealer("Enfermeira", "npcs/ingame/npc_ingame04.png");
        npcHealer2.getTransform().setScale(3, 3);
        npcHealer2.getTransform().setPosition(3192, 2533);
        gamePanel.addElement(npcHealer2);

        NpcSeller shopkeeper1 = new NpcSeller("Vendedor", "npcs/ingame/npc_ingame07.png");
        shopkeeper1.getTransform().setScale(3, 3);
        shopkeeper1.getTransform().setPosition(3235, 5459);
        shopkeeper1.getInventory().add(ItemRegistry.smallPotion, 5);
        shopkeeper1.getInventory().add(ItemRegistry.mediumPotion, 2);
        gamePanel.addElement(shopkeeper1);

        NpcSeller shopkeeper2 = new NpcSeller("Vendedor", "npcs/ingame/npc_ingame07.png");
        shopkeeper2.getTransform().setScale(3, 3);
        shopkeeper2.getTransform().setPosition(3672, 2196);
        shopkeeper2.getInventory().add(ItemRegistry.smallPotion, 5);
        shopkeeper2.getInventory().add(ItemRegistry.mediumPotion, 2);
        shopkeeper2.getInventory().add(ItemRegistry.pokeBall, 2);
        gamePanel.addElement(shopkeeper2);

        NpcTrainer npctrainer1 = new NpcTrainer(new Inventory(), new Team(), "Luiz Gustavo", "npcs/ingame/npc_ingame09.png", "npcs/battle/npc_battle09.png");
        npctrainer1.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(25), 8));
        for (var poke : npctrainer1.getTeam().getMembers())
            poke.setOwner(true);
        npctrainer1.getTransform().setScale(3, 3);
        npctrainer1.getTransform().setPosition(2928, 4452);
        String[] npctrainer1Message = {"Vamos lutar!"};
        npctrainer1.setMessage(npctrainer1Message);
        gamePanel.addElement(npctrainer1);

        NpcTrainer npctrainer2 = new NpcTrainer(new Inventory(), new Team(), "Mateus", "npcs/ingame/npc_ingame09.png", "npcs/battle/npc_battle24.png");
        npctrainer2.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(74), 9));
        npctrainer2.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(66), 10));
        for (var poke : npctrainer2.getTeam().getMembers())
            poke.setOwner(true);
        npctrainer2.getTransform().setScale(3, 3);
        npctrainer2.getTransform().setPosition(3117, 3971);
        npctrainer2.setMessage(null);
        gamePanel.addElement(npctrainer2);

        NpcTrainer npctrainer3 = new NpcTrainer(new Inventory(), new Team(), "Pedro Luz", "npcs/ingame/npc_ingame08.png", "npcs/battle/npc_battle40.png");
        npctrainer3.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(20), 11));
        npctrainer3.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(35), 13));
        for (var poke : npctrainer3.getTeam().getMembers())
            poke.setOwner(true);
        npctrainer3.getTransform().setScale(3, 3);
        npctrainer3.getTransform().setPosition(3211, 3253);
        npctrainer3.setMessage(null);
        gamePanel.addElement(npctrainer3);

        NpcTrainer gymleader = new NpcTrainer(new Inventory(), new Team(), "Burimel", "npcs/ingame/npc_ingame08.png", "npcs/battle/npc_battle06.png");
        gymleader.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(20), 13));
        gymleader.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(75), 14));
        gymleader.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(5), 16));
        for (var poke : gymleader.getTeam().getMembers())
            poke.setOwner(true);
        gymleader.getTransform().setScale(3, 3);
        gymleader.getTransform().setPosition(3667, 1764);
        gymleader.setMessage(null);
        gamePanel.addElement(gymleader);

        NpcTrainer npctrainer4 = new NpcTrainer(new Inventory(), new Team(), "Carioca", "npcs/ingame/npc_ingame01.png", "npcs/battle/npc_battle10.png");
        npctrainer4.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(67), 19));
        npctrainer4.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(26), 20));
        npctrainer4.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(8), 22));
        for (var poke : npctrainer4.getTeam().getMembers())
            poke.setOwner(true);
        npctrainer4.getTransform().setScale(3, 3);
        npctrainer4.getTransform().setPosition(923, 1922);
        npctrainer4.setMessage(null);
        gamePanel.addElement(npctrainer4);

        NpcTrainer champion = new NpcTrainer(new Inventory(), new Team(), "Gabriel", "npcs/ingame/npc_ingame01.png", "npcs/battle/npc_battle25.png");
        champion.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(76), 23));
        champion.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(26), 24));
        champion.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(36), 25));
        champion.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(6), 26));
        for (var poke : champion.getTeam().getMembers())
            poke.setOwner(true);
        champion.getTransform().setScale(3, 3);
        champion.getTransform().setPosition(21, 2053);
        champion.setMessage(null);
        gamePanel.addElement(champion);
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