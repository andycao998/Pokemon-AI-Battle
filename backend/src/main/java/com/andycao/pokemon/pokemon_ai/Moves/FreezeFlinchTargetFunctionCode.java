package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class FreezeFlinchTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        FreezeTargetFunctionCode freezeTarget = new FreezeTargetFunctionCode();
        freezeTarget.freeze(moveTarget, getEffectChance(), false);

        FlinchTargetFunctionCode flinchTarget = new FlinchTargetFunctionCode();
        flinchTarget.flinch(moveTarget, getEffectChance());
    }
}
