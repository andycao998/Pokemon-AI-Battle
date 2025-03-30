package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserSpeed2FunctionCode extends Move {
    public void raiseSpeed2(Pokemon moveTarget) {
        moveTarget.updateSpeedStage(2, moveTarget, false);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        raiseSpeed2(moveTarget);
    }
}
