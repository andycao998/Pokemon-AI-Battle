package com.andycao.pokemon.pokemon_ai.Moves;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class PowerHigherWithUserHeavierThanTargetFunctionCode extends Move {
    private void calculatePower(double userWeight, double targetWeight) {
        BigDecimal userWeightDecimal = new BigDecimal(userWeight);
        BigDecimal targetWeightDecimal = new BigDecimal(targetWeight);

        double ratio = userWeightDecimal.divide(targetWeightDecimal, RoundingMode.HALF_UP).doubleValue();

        if (ratio >= 5.0) {
            setPower(120);
        }
        else if (ratio >= 4.0) {
            setPower(100);
        }
        else if (ratio >= 3.0) {
            setPower(80);
        }
        else if (ratio >= 2.0) {
            setPower(60);
        }
        else {
            setPower(40);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        calculatePower(user.getWeight(), moveTarget.getWeight());

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
