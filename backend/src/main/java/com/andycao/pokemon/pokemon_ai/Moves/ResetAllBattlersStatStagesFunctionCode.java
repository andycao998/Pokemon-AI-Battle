package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ResetAllBattlersStatStagesFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().getPlayerPokemon().resetStatChanges();
        BattleManager.getInstance().getBotPokemon().resetStatChanges();

        TurnEventMessageBuilder.getInstance().appendEvent("Haze reset every battler's stat changes!");
    }
}
