package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class AddStickyWebToFoeSideFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getStickyWeb(moveTarget)) {
            TurnEventMessageBuilder.getInstance().appendEvent("Sticky Web is already set up!");
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(moveTarget), true);
            return;
        }

        BattleManager.getInstance().setStickyWeb(moveTarget);
        TurnEventMessageBuilder.getInstance().appendEvent("A sticky web surrounds the opposing team!");
    }
}
