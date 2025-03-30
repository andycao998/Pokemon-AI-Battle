package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class SleepTargetNextTurnFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (!moveTarget.getStatus().isEmpty()) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " cannot become drowsy!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        if (moveTarget.getDrowsyTurns() > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is already drowsy!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        moveTarget.setDrowsy(2, false);
        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " became drowsy!");
    }
}
