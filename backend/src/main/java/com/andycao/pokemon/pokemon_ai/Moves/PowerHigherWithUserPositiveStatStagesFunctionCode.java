package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class PowerHigherWithUserPositiveStatStagesFunctionCode extends Move {
    private int getNumPositiveStages(Pokemon user) {
        int num = 0;

        if (user.getAttackStage() > 0) {
            num += user.getAttackStage() * 20;
        }
        if (user.getDefenseStage() > 0) {
            num += user.getDefenseStage() * 20;
        }
        if (user.getSpAttackStage() > 0) {
            num += user.getSpAttackStage() * 20;
        }
        if (user.getSpDefenseStage() > 0) {
            num += user.getSpDefenseStage() * 20;
        }
        if (user.getSpeedStage() > 0) {
            num += user.getSpeedStage() * 20;
        }
        if (user.getAccuracyStage() > 0) {
            num += user.getAccuracyStage() * 20;
        }
        if (user.getEvasionStage() > 0) {
            num += user.getEvasionStage() * 20;
        }

        return num;
    } 

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        setPower(getNumPositiveStages(user));

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
