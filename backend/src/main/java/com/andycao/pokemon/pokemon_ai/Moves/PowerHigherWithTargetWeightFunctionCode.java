package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class PowerHigherWithTargetWeightFunctionCode extends Move {
    private void calculatePower(Pokemon target) {
        if (target.getWeight() >= 200.0) {
            setPower(120);
        }
        else if (target.getWeight() >= 100.0) {
            setPower(100);
        }
        else if (target.getWeight() >= 50.0) {
            setPower(80);
        }
        else if (target.getWeight() >= 25.0) {
            setPower(60);
        }
        else if (target.getWeight() >= 10.0) {
            setPower(40);
        }
        else {
            setPower(20);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        calculatePower(moveTarget);

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
