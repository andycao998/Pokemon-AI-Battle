package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserAtkDefAcc1FunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        moveTarget.updateAttackStage(1, moveTarget, false);
        moveTarget.updateDefenseStage(1, moveTarget, false);
        moveTarget.updateAccuracyStage(1, moveTarget, false);
    }
}
