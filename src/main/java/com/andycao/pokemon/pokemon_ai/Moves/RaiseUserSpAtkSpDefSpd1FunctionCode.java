package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserSpAtkSpDefSpd1FunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        moveTarget.updateSpAttackStage(1, moveTarget, false);
        moveTarget.updateSpDefenseStage(1, moveTarget, false);
        moveTarget.updateSpeedStage(1, moveTarget, false);
    }
}
