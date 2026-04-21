package execs;

import engine.core.GamePanel;
import engine.input.Input;
import examples.TilemapExample;

import java.awt.Color;
import javax.swing.JFrame;

public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Minha janela");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = GamePanel.getInstance();
        gamePanel.setBackground(Color.DARK_GRAY);

        frame.setContentPane(gamePanel);
        frame.pack();
        frame.setVisible(true);

        TilemapExample.runExample();

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


