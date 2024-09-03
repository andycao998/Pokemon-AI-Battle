package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class LowerUserDefSpDef1RaiseUserAtkSpAtkSpd2FunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        moveTarget.updateDefenseStage(-1, moveTarget, false);
        moveTarget.updateSpDefenseStage(-1, moveTarget, false);
        moveTarget.updateAttackStage(2, moveTarget, false);
        moveTarget.updateSpAttackStage(2, moveTarget, false);
        moveTarget.updateSpeedStage(2, moveTarget, false);
    }
}