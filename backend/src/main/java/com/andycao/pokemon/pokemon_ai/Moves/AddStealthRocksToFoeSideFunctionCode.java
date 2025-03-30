package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class AddStealthRocksToFoeSideFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getStealthRocks(moveTarget)) {
            TurnEventMessageBuilder.getInstance().appendEvent("Stealth Rocks are already set up!");
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(moveTarget), true);
            return;
        }

        BattleManager.getInstance().setStealthRocks(moveTarget);
        TurnEventMessageBuilder.getInstance().appendEvent("Pointed stones float around the opposing team!");
    }
}
