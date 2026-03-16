import engine.core.GamePanel;
import engine.input.Input;
import engine.primitives.Square;
import game.Monster;

import javax.swing.*;
import java.awt.*;

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

        Square quadrado = new Square(10, 10, 100, Color.red, 0);
        gamePanel.addElement(quadrado);

        Monster monster = new Monster();
        gamePanel.addElement(monster);

        new Thread(() -> {
            while(true)
            {
                gamePanel.updateAll();
                gamePanel.repaint();
                Input.endFrame();

                try { Thread.sleep(16); } catch (Exception ignored) {}
            }
        }).start();
    }
}


