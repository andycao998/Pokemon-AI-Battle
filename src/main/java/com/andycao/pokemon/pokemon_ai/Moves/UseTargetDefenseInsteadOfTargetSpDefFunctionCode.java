package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UseTargetDefenseInsteadOfTargetSpDefFunctionCode extends Move {
    private int calculateTargetDefense(Pokemon moveTarget) {
        int unmodifiedDefense = moveTarget.getUnmodifiedDefense();
        int defenseStage = moveTarget.getDefenseStage();

        double modifier = 1.0;
        if (defenseStage < 0) {
            modifier = (double) 2 / (2 + Math.abs(defenseStage));
        }
        else {
            modifier = (double) (2 + Math.abs(defenseStage)) / 2;
        }

        return (int) Math.floor(unmodifiedDefense * modifier);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int targetSpDefense = moveTarget.getCurrentSpDefense();

        int effectiveDefense = calculateTargetDefense(moveTarget);

        moveTarget.setCurrentSpDefense(effectiveDefense);
        BattleManager.getInstance().dealDamage(moveTarget, this);
        moveTarget.setCurrentSpDefense(targetSpDefense);
    }
}
