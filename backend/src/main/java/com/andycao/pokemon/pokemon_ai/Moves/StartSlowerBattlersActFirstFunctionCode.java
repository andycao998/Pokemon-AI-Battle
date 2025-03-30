package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StartSlowerBattlersActFirstFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getTrickRoomTurns() > 0) {
            BattleManager.getInstance().setTrickRoom(0);
            return;
        }

        BattleManager.getInstance().setTrickRoom(5);
    }
}
