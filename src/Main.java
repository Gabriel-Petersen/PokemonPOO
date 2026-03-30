import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;

import engine.core.GamePanel;
import engine.input.Input;
import engine.primitives.Square;
import engine.ui.elements.UiButton;
import game.Player;

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

        gamePanel.addElement(new Square(0, 0, 10, Color.blue, -1));

        Player player = new Player();
        gamePanel.addElement(player);

        var rd = new Random();
        UiButton botao = new UiButton("Meu Botão", () ->
                quadrado.getTransform().setPosition(100 * rd.nextDouble() - 50, 100 * rd.nextDouble() - 50)
                // randomiza a position do quadrado
        );
        botao.getTransform().setScale(100, 100);
        gamePanel.addElement(botao);

        new Thread(() -> {
            do
            {
                gamePanel.updateAll();
                gamePanel.repaint();
                Input.endFrame();

                try { Thread.sleep(16); } catch (Exception ignored) {}
            } while (true);
        }).start();
    }
}


