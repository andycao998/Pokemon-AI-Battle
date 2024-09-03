package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StartLeechSeedTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!moveTarget.canLeech()) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " cannot be leeched!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        moveTarget.setLeeched(true);

        TurnEventMessageBuilder.getInstance().appendEvent("Leeching seeds were applied to " + moveTarget.getName() + "!");
    }
}
