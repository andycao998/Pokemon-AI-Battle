package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class SwitchOutTargetStatusMoveFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        SwitchOutUserStatusMoveFunctionCode switchCheckFunction = new SwitchOutUserStatusMoveFunctionCode();

        if (!switchCheckFunction.canSwitch(moveTarget)) {
            return;
        }

        if (!BattleManager.getInstance().endBattle()) {
            // Bypass trapping effects
            BattleManager.getInstance().switchPokemon(moveTarget, getName());
        }
    }
}
