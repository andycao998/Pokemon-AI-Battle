package com.andycao.pokemon.pokemon_ai.Moves;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UseRandomUserMoveIfAsleepFunctionCode extends Move {
    private Move getRandomMove(Pokemon user) throws InvalidIdentifierException {
        String[] moves = user.getMoves();

        Move move1 = MoveFactory.generateMove(moves[0]);
        Move move2 = MoveFactory.generateMove(moves[1]);
        Move move3 = MoveFactory.generateMove(moves[2]);
        Move move4 = MoveFactory.generateMove(moves[3]);

        List<Move> moveList = new ArrayList<Move>();
        moveList.add(move1);
        moveList.add(move2);
        moveList.add(move3);
        moveList.add(move4);

        Collections.shuffle(moveList);

        for (Move move : moveList) {
            if (!move.containsFlag("CannotSleepTalk") && !move.getName().equals("Sleep Talk")) {
                return move;
            }
        }

        return new NullMove();
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!moveTarget.getStatus().equals("Sleep")) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is not asleep!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        Move randomMove = getRandomMove(moveTarget);

        if (randomMove.getName().isEmpty()) {
            TurnEventMessageBuilder.getInstance().appendEvent("Sleep Talk couldn't find a move!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }
        
        BattleManager.getInstance().useMove(moveTarget, randomMove.getId());
    }
}
