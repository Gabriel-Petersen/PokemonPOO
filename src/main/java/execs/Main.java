package execs;

import engine.assets.AssetManager;
import engine.core.GamePanel;
import engine.input.Input;
import engine.tilemap.ImageTilemap;
import engine.tilemap.Tilemap;
import game.battle.Team;
import game.creature.Pokemon;
import game.entities.Npc;
import game.entities.NpcTrainer;
import game.itemsystem.Inventory;
import game.loader.SpecieRegister;
import game.player.Player;
import java.awt.Color;
import java.util.Objects;
import javax.swing.JFrame;

public class Main
{
    private static void buildMap()
    {
        GamePanel gamePanel = GamePanel.getInstance();

        Tilemap tilemap = new ImageTilemap(
                Objects.requireNonNull(AssetManager.getSprite("scenario/mapa_limited.png")),
                AssetManager.getSprite("scenario/collision_matrix_map.png"),
                16
        );

        tilemap.setLayer(-1);
        tilemap.getTransform().setScale(3, 3);
        tilemap.getTransform().setPosition(
                -4 * tilemap.getSizeX(),
                -4 * tilemap.getSizeY()
        );

        gamePanel.addElement(tilemap);

        Player player = new Player();
        player.getTransform().setScale(1.8, 1.8);
        player.setCurrentMap(tilemap);
        player.getTransform().setPosition(2806, 5522);
        player.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(1), 2));
        gamePanel.addElement(player);

        Npc npc1 = new Npc("Certinho", "npcs/ingame/npc_ingame01.png");
        npc1.getTransform().setScale(3, 3);
        npc1.getTransform().setPosition(2856, 5522);
        String[] npc1Message = {
            "Olá certinho!",
            "Vamos abrir o Eclipse para fazer nosso programinha em Java?",
            "E o Grêmio?"
        };
        npc1.setMessage(npc1Message);
        gamePanel.addElement(npc1);

        NpcTrainer npc2 = new NpcTrainer(
            new Inventory(), new Team(), "sei la", "npcs/ingame/npc_ingame05.png", "npcs/ingame/npc_ingame05.png"
        );
        npc2.getTeam().addMember(new Pokemon(null, SpecieRegister.getSpecie(1), 1));
        npc2.getTransform().setScale(3, 3);
        npc2.getTransform().setPosition(3000, 5522);
        String[] npc2Message = {
            "Vamos lutar!"
        };
        npc2.setMessage(npc2Message);
        gamePanel.addElement(npc2);
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

        buildMap();

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


