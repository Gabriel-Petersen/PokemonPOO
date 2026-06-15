package game.entities;

import engine.events.LambdaEvent;
import game.player.Player;
import game.ui.common.DialogueBox;

public class NpcHealer extends Npc 
{
    public NpcHealer(String name, String spritePath) 
    { 
        super(name, spritePath); 
        message = new String[] {
            "Seja bem vindo! Vou curar seus amigos para você!!",
            "Só um instante..."
        };
    }

    @Override public final void setMessage(String[] message) { System.err.println("Immutable message of NpcHealer"); }

    @Override
    public void onInteract(Player player) {
        super.onInteract(player);
        var db = DialogueBox.getInstance();

        db.getEventQueue().enqueue(new LambdaEvent(() -> player.getTeam().healAll()));
        db.getEventQueue().setOnEndResolving(() -> {
            player.setTalking(false);
            db.setVisible(false);
        });
    }
}
