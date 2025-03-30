package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RecoilThirdOfDamageDealtBurnTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        RecoilThirdOfDamageDealtFunctionCode recoilFunction = new RecoilThirdOfDamageDealtFunctionCode();
        recoilFunction.recoil(moveTarget, this, 3);

        BurnTargetFunctionCode burnFunction = new BurnTargetFunctionCode();
        burnFunction.burn(moveTarget, getEffectChance(), false);
    }
}
