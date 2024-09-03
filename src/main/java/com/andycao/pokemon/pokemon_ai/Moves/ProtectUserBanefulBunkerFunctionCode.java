package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ProtectUserBanefulBunkerFunctionCode extends Move {
    public void poisonTarget(Pokemon target, boolean activated) {
        if (!activated) {
            PoisonTargetFunctionCode poisonFunction = new PoisonTargetFunctionCode();
            poisonFunction.poison(target, getEffectChance(), true);
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " was poisoned from the spiny bunker!");
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        ProtectUserFunctionCode protectUser = new ProtectUserFunctionCode();
        protectUser.protect(moveTarget, "Baneful Bunker");
    }
}
