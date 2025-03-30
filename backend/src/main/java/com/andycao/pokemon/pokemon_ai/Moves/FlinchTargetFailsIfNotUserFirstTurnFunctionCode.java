package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class FlinchTargetFailsIfNotUserFirstTurnFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (user.getTurnsOut() == 1) {
            BattleManager.getInstance().dealDamage(moveTarget, this);

            FlinchTargetFunctionCode flinchFunction = new FlinchTargetFunctionCode();
            flinchFunction.flinch(moveTarget, getEffectChance());
        }
        else {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + "'s move failed because it isn't their first turn out!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
        }
    }
}
