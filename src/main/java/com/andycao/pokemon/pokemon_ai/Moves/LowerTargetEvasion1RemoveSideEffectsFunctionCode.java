package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class LowerTargetEvasion1RemoveSideEffectsFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        moveTarget.updateEvasionStage(-1, user, false);

        BattleManager.getInstance().removeEntryHazards(user);
        BattleManager.getInstance().removeEntryHazards(moveTarget);
        BattleManager.getInstance().removeScreens(moveTarget);
    }
}
