package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class LowerTargetDefense1PowersUpInGravityFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        LowerTargetDefense1FunctionCode lowerTargetDefense = new LowerTargetDefense1FunctionCode();
        lowerTargetDefense.lowerDefense1(moveTarget, getEffectChance(), false);
    }
}
