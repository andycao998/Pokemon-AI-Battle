package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;

public class FlinchTargetFunctionCode extends Move {
    public void flinch(Pokemon moveTarget, int effectChance) {
        if (moveTarget.canFlinch()) {
            if (EffectChanceRandomizer.evaluate(effectChance, 100)) {
                moveTarget.setFlinched(true);
            }
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        flinch(moveTarget, getEffectChance());
        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
    
}
