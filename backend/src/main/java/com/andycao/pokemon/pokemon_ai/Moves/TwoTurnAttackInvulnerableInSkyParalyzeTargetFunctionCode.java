package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class TwoTurnAttackInvulnerableInSkyParalyzeTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        TwoTurnAttackInvulnerableInSkyFunctionCode twoTurnSkyFunction = new TwoTurnAttackInvulnerableInSkyFunctionCode();

        if (!user.lockedIntoMove()) {
            twoTurnSkyFunction.firstTurnSky(moveTarget, user, getId());
        }
        else if (user.getLockedMove().getLeft().equals(getId())) {
            twoTurnSkyFunction.secondTurnSky(moveTarget, user, this);

            ParalyzeTargetFunctionCode paralyzeFunction = new ParalyzeTargetFunctionCode();
            paralyzeFunction.paralyze(moveTarget, getEffectChance(), false, getType());
        }
    }
}
