package examples;

import engine.core.GamePanel;
import engine.events.EventScheduler;
import engine.events.GameEvent;
import engine.events.LambdaEvent;
import engine.events.ProgressBarChangeEvent;
import engine.events.TypewriterEvent;
import engine.events.WaitEvent;
import engine.ui.core.UiTransform;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiProgressBar;
import engine.ui.elements.UiText;
import java.awt.Color;

public final class EventQueueExample
{
    private EventQueueExample() { }

    public static void runExample()
    {
        GamePanel gamePanel = GamePanel.getInstance();
        EventScheduler battleScheduler = new EventScheduler();

        UiProgressBar healthBar = new UiProgressBar(UiProgressBar.Direction.LEFT2RIGHT);
        healthBar.getTransform().setPosition(25, 60);
        healthBar.getTransform().setScale(300, 20);
        healthBar.setBackgroundColor(Color.BLACK);
        healthBar.setFillColor(Color.GREEN);
        healthBar.setProgress(1.0);
        gamePanel.addElement(healthBar);

        UiText console = new UiText("Aguardando ação...");
        console.setColor(Color.WHITE);
        console.getTransform().setPosition(50, 100);
        gamePanel.addElement(console);

        UiButton btnAtaque = new UiButton("Executar Ataque", () -> {
            if (battleScheduler.isResolving()) return;

            battleScheduler.enqueue(new TypewriterEvent(
                    console, "Pikachu usou Choque do Trovão!", 0.05, 0.5)
            );

            battleScheduler.enqueue(new WaitEvent(0.3));
            battleScheduler.enqueue(new LambdaEvent(() -> healthBar.setFillColor(Color.RED)));
            battleScheduler.enqueue(new ProgressBarChangeEvent(healthBar, 0.4, 1.5));

            // Evento customizado para mostrar init/update/isFinished em ação.
            battleScheduler.enqueue(new PulseTextEvent(
                    console,
                    "Eletricidade estática carregando",
                    1.2,
                    0.2
            ));

            battleScheduler.enqueue(new TypewriterEvent(console, "É super efetivo!", 0.05, 1.0));

            battleScheduler.enqueue(new LambdaEvent(() -> {
                console.setText("O que Pikachu fará?");
                healthBar.setFillColor(Color.YELLOW);
            }));

            battleScheduler.enqueue(new WaitEvent(1.2));
            battleScheduler.enqueue(new LambdaEvent(() -> {
                console.setText("Escolha a Ação");
                System.out.println("Fila de eventos finalizada!");
            }));

            battleScheduler.resolve();
        });

        btnAtaque.getTransform().setPosition(50, 300);
        btnAtaque.getChild(0).getUiTransform().setAnchor(UiTransform.Anchor.CENTER_LEFT);
        btnAtaque.getTransform().setScale(150, 50);
        gamePanel.addElement(btnAtaque);

        UiButton btnReset = new UiButton("Reset", () -> {
            healthBar.setProgress(1.0);
            healthBar.setFillColor(Color.GREEN);
            console.setText("Laboratório Restaurado.");
        });
        btnReset.getChild(0).getUiTransform().setAnchor(UiTransform.Anchor.CENTER_LEFT);
        btnReset.getTransform().setPosition(210, 300);
        btnReset.getTransform().setScale(150, 50);
        gamePanel.addElement(btnReset);
    }

    private static final class PulseTextEvent extends GameEvent
    {
        private final UiText text;
        private final String baseMessage;
        private final double duration;
        private final double pulseInterval;

        private double elapsed;
        private double pulseElapsed;
        private boolean pulseOn;

        private PulseTextEvent(
                UiText text,
                String baseMessage,
                double duration,
                double pulseInterval
        )
        {
            super(null);
            this.text = text;
            this.baseMessage = baseMessage;
            this.duration = duration;
            this.pulseInterval = pulseInterval;
        }

        @Override
        public void init()
        {
            elapsed = 0;
            pulseElapsed = 0;
            pulseOn = false;
            text.setText(baseMessage);
        }

        @Override
        public void update(double dt)
        {
            elapsed += dt;
            pulseElapsed += dt;

            if (pulseElapsed >= pulseInterval)
            {
                pulseElapsed -= pulseInterval;
                pulseOn = !pulseOn;
                text.setText(pulseOn ? baseMessage + "..." : baseMessage);
            }
        }

        @Override
        public boolean isFinished()
        {
            return elapsed >= duration;
        }
    }
}
