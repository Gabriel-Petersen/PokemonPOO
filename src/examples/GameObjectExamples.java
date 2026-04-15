package examples;

import engine.core.GameObject;
import engine.core.GamePanel;
import engine.primitives.Square;
import engine.rendering.Renderer;
import engine.rendering.ShapeFactory;
import game.Player;
import java.awt.Color;

public final class GameObjectExamples
{
    public static void runExample()
    {
        GamePanel gamePanel = GamePanel.getInstance();

        // Usar a classe Square existente
        Square redSquare = new Square(10, 10, 100, Color.red, 0);
        Square blueSquare = new Square(0, 0, 10, Color.blue, -1);

        // Adicionar elementos ao painel
        gamePanel.addElement(redSquare);
        gamePanel.addElement(blueSquare);

        // Criar um círculo como exemplo adicional
        ExampleCircle circle = new ExampleCircle(50, 50, 25, Color.yellow);
        gamePanel.addElement(circle);

        // Usar o Player canônico do game
        Player player = new Player();
        gamePanel.addElement(player);

        // Interações: demonstrar mudanças de propriedades dos objetos
        redSquare.getTransform().setPosition(50, 100);
        redSquare.getTransform().setScale(1.5, 1.5);

        blueSquare.getTransform().setPosition(200, 150);
        blueSquare.getTransform().setScale(2, 2);

        circle.getTransform().setPosition(350, 100);
        circle.getTransform().setScale(1.2, 1.2);

        player.getTransform().setPosition(300, 200);
        player.getTransform().setScale(2, 2);
    }

    /**
     * Exemplo de GameObject customizado usando formas geométricas.
     * Demonstra como criar um círculo usando ShapeFactory.
     */
    private static class ExampleCircle extends GameObject
    {
        private final int radius;
        private final Color color;

        private ExampleCircle(int x, int y, int radius, Color color)
        {
            getTransform().setPosition(x, y);
            this.radius = radius;
            this.color = color;
        }

        @Override
        protected Renderer createSwingRenderer()
        {
            return ShapeFactory.createCircle(radius, color);
        }

        @Override
        public void setup()
        {
            renderer = createSwingRenderer();
        }

        @Override
        public void update()
        {
            // Rotação suave do círculo para demonstrar transformação
            getTransform().rotation += 1.0;
        }
    }
}
