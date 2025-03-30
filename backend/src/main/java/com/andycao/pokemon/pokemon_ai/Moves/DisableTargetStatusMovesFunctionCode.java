package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DisableTargetStatusMovesFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (moveTarget.getTauntedTurns() > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is already taunted!");
        }
        else {
            moveTarget.setTaunted(3);
        }
    }
}
