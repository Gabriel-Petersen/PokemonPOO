package game.ui;

import engine.core.GamePanel;
import engine.events.EventScheduler;
import engine.events.TypewriterEvent;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import java.awt.Color;

public class DialogueBox extends UiImage 
{
    private static final DialogueBox INSTANCE = new DialogueBox();
    
    private final UiText txt = new UiText();
    private final EventScheduler eventQueue = new EventScheduler();
    private boolean isAdded = false;

    private DialogueBox()
    {
        super(
            700,
            40,
            Color.black    
        );
        getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
        getUiTransform().setPosition(getUiTransform().getPosition().add(0, -20));
        setVisible(true);
        txt.setColor(Color.white);
        txt.getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        var txtTr = txt.getUiTransform();
        txtTr.setPosition(txtTr.getPosition().add(10, 0));
        addChild(txt);
    }

    public static DialogueBox getInstance() { 
        if (!INSTANCE.isAdded) {
            GamePanel.getInstance().addElement(INSTANCE);
            INSTANCE.isAdded = true;
        }
        return INSTANCE; 
    }

    public void showText(String text)
    {
        if (text == null || text.isEmpty()) return;
        txt.setText(text);
        eventQueue.enqueue(new TypewriterEvent(txt, text, 0.05, 0.5));
    }

    public EventScheduler getEventQueue() {
        return eventQueue;
    }
}
