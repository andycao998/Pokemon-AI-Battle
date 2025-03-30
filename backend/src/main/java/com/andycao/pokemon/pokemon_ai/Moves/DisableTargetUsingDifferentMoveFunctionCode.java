package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DisableTargetUsingDifferentMoveFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        Move lastTargetMove = BattleManager.getInstance().getPokemonLastMove(moveTarget);

        if (moveTarget.getEncoredTurns() > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is already encored!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        if (lastTargetMove.getName().isEmpty()) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " hasn't made a move!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        if (moveTarget.getMovePPs(lastTargetMove.getId()) == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(lastTargetMove.getName() + " has no PP remaining!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        if (lastTargetMove.containsFlag("CannotSleepTalk") || lastTargetMove.equals(this) || lastTargetMove.getName().equals("Transform")) {
            TurnEventMessageBuilder.getInstance().appendEvent(lastTargetMove.getName() + " cannot be encored!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        moveTarget.setEncored(lastTargetMove.getId(), 3);

        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " was encored into using " + lastTargetMove.getName() + "!");
    }
}
