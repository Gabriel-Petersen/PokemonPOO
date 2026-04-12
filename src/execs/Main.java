package execs;

import java.awt.Color;

import javax.swing.JFrame;

import engine.core.GamePanel;
import engine.events.*;
import engine.input.Input;
import engine.primitives.Square;
import engine.ui.core.UiTransform;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiProgressBar;
import engine.ui.elements.UiText;
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

        setupLaboratorio();

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

                try { Thread.sleep(16); } catch (Exception ignored) {}
            } while (true);
        }).start();
    }

    public static void setupLaboratorio() 
    {
        System.out.println("Iniciando laboratorio");
        GamePanel gamePanel = GamePanel.getInstance();
        EventScheduler battleScheduler = new EventScheduler();

        // 1. Criar uma Barra de HP para testar o ProgressBarChangeEvent
        UiProgressBar healthBar = new UiProgressBar(UiProgressBar.Direction.LEFT2RIGHT);
        healthBar.getTransform().setPosition(25, 60);
        healthBar.getTransform().setScale(300, 20);
        healthBar.setBackgroundColor(Color.BLACK);
        healthBar.setFillColor(Color.GREEN);
        healthBar.setProgress(1.0); // Começa cheia
        gamePanel.addElement(healthBar);

        // 2. Criar um elemento de texto para o Typewriter
        UiText console = new UiText("Aguardando ação...");
        console.setColor(Color.white);
        console.getTransform().setPosition(50, 100);
        gamePanel.addElement(console);

        // 3. Botão para disparar a "Sequence de Ataque"
        UiButton btnAtaque = new UiButton("Executar Ataque", () -> {
            // Se já estiver resolvendo, ignore o clique (ou deixe enfileirar, você decide)
            if (battleScheduler.isResolving()) return;

            System.out.println("Enfileirando sequência de eventos...");

            // Evento 1: Texto de ataque
            battleScheduler.enqueue(new TypewriterEvent(
                    console, "Pikachu usou Choque do Trovão!", 0.05, 0.5)
            );

            // Evento 2: Pequena pausa dramática
            battleScheduler.enqueue(new WaitEvent(0.3));

            // Evento 3: Mudar a cor da barra para amarelo no meio do caminho (via Lambda)
            battleScheduler.enqueue(new LambdaEvent(() -> healthBar.setFillColor(Color.red)));

            // Evento 4: A barra de vida desce suavemente
            // Se a vida atual é 1.0, vamos descer para 0.4 em 1.5 segundos
            battleScheduler.enqueue(new ProgressBarChangeEvent(healthBar, 0.4, 1.5));

            // Evento 5: Texto de dano
            battleScheduler.enqueue(new TypewriterEvent(console, "É super efetivo!", 0.05, 1.0));

            // Evento 6: Voltar para o estado original
            battleScheduler.enqueue(new LambdaEvent(() -> {
                console.setText("O que Pikachu fará?");
                healthBar.setFillColor(Color.yellow);
            }));

            battleScheduler.enqueue(new WaitEvent(1.2));
            battleScheduler.enqueue(new LambdaEvent(() -> {
                console.setText("Escolha a Ação");
                System.out.println("Fila de eventos finalizada!");
            }));

            // DAR O PLAY
            battleScheduler.resolve();
        });

        btnAtaque.getTransform().setPosition(50, 300);
        btnAtaque.getChild(0).getUiTransform().setAnchor(UiTransform.Anchor.CENTER_LEFT);
        btnAtaque.getTransform().setScale(150, 50);
        gamePanel.addElement(btnAtaque);

        // 4. Botão de Reset (para testar a barra subindo)
        UiButton btnReset = new UiButton("Reset Lab", () -> {
            healthBar.setProgress(1.0);
            healthBar.setFillColor(Color.GREEN);
            console.setText("Laboratório Restaurado.");
        });
        btnReset.getChild(0).getUiTransform().setAnchor(UiTransform.Anchor.CENTER_LEFT);
        btnReset.getTransform().setPosition(210, 300);
        btnReset.getTransform().setScale(150, 50);
        gamePanel.addElement(btnReset);
    }
}


