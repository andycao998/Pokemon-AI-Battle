package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DoublePowerIfTargetStatusProblemFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!moveTarget.getStatus().isEmpty() && !moveTarget.getStatus().equals("Fainted")) {
            setPower(getPower() * 2);
            TurnEventMessageBuilder.getInstance().appendEvent("Hex doubled in power from " + moveTarget.getName() + "'s afflictions!");
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
