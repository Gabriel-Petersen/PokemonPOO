package game.battle.ai;

import game.battle.BattleContext;
import game.battle.Trainer;
import game.battle.actions.CombatAction;
import game.battle.actions.MoveAction;
import game.creature.Pokemon;
import game.creature.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartDamageAI implements BattleAI
{
    private final Random random = new Random();

    @Override
    public CombatAction selectAction(BattleContext context, Trainer npc)
    {
        Pokemon user = npc.getCurrent();
        Pokemon target = context.getPlayer().getCurrent();

        Move[] moves = user.getMoves();
        List<Move> usableMoves = new ArrayList<>();

        for (Move m : moves) {
            if (m != null && m.getPp() > 0 && m.canUse(context)) {
                usableMoves.add(m);
            }
        }

        if (usableMoves.isEmpty()) {
            return new MoveAction(moves[0], target, user, npc);
        }

        Move bestMove = null;
        for (Move move : usableMoves) {
            if (bestMove == null || move.getPower() >= bestMove.getPower())
                bestMove = move;
        }

        return new MoveAction(bestMove, target, user, npc);
    }
}