package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ParalyzeFlinchTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        ParalyzeTargetFunctionCode paralyzeTarget = new ParalyzeTargetFunctionCode();
        paralyzeTarget.paralyze(moveTarget, getEffectChance(), false, getType());

        FlinchTargetFunctionCode flinchTarget = new FlinchTargetFunctionCode();
        flinchTarget.flinch(moveTarget, getEffectChance());
    }
}
