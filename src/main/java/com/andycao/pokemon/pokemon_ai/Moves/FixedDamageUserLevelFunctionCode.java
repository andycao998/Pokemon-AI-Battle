package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class FixedDamageUserLevelFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDirectDamage(moveTarget, this, 50);
    }
}