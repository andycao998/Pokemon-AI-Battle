package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ProtectUserFromDamagingMovesKingsShieldFunctionCode extends Move {
    public void lowerAttack1(Pokemon target, boolean activated) {
        Pokemon user = BattleManager.getInstance().getOpposing(target);

        if (!activated) {
            target.updateAttackStage(-1, user, false);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        ProtectUserFunctionCode protectUser = new ProtectUserFunctionCode();
        protectUser.protect(moveTarget, "King's Shield");
    }
}
