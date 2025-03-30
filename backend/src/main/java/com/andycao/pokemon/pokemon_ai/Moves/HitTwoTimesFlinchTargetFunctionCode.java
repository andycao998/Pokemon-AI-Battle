package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HitTwoTimesFlinchTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        HitTwoTimesFunctionCode hitTwiceFunction = new HitTwoTimesFunctionCode();
        int numHits = hitTwiceFunction.hitTwice(moveTarget, this);

        if (numHits == 2) {
            setEffectChance(51);
        }

        FlinchTargetFunctionCode flinchFunction = new FlinchTargetFunctionCode();
        flinchFunction.flinch(moveTarget, getEffectChance());
    }
}
