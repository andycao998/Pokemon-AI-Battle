package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class IgnoreTargetAbilityFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        String targetAbility = moveTarget.getCurrentAbility();

        moveTarget.setCurrentAbility("None");
        BattleManager.getInstance().dealDamage(moveTarget, this);
        moveTarget.setCurrentAbility(targetAbility);
    }
}
