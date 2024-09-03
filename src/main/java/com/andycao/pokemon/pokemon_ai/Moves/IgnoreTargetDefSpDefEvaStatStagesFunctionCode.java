package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class IgnoreTargetDefSpDefEvaStatStagesFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int targetDefenseStages = moveTarget.getDefenseStage();
        int targetSpDefenseStages = moveTarget.getSpDefenseStage();
        int targetEvasionStages = moveTarget.getEvasionStage();

        moveTarget.updateDefenseStage(targetDefenseStages * -1, moveTarget, true);
        moveTarget.updateSpDefenseStage(targetSpDefenseStages * -1, moveTarget, true);
        moveTarget.updateEvasionStage(targetEvasionStages * -1, moveTarget, true);

        BattleManager.getInstance().dealDamage(moveTarget, this);

        moveTarget.updateDefenseStage(targetDefenseStages, moveTarget, true);
        moveTarget.updateSpDefenseStage(targetSpDefenseStages, moveTarget, true);
        moveTarget.updateEvasionStage(targetEvasionStages, moveTarget, true);
    }
}
