package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class PowerHigherWithTargetFasterThanUserFunctionCode extends Move {
    private void calculatePower(int userSpeed, int targetSpeed) {
        if (userSpeed == 0) {
            setPower(1);
            return;
        }

        double speedCalc = ((25 * targetSpeed) / userSpeed) + 1;
        int power = Math.min(150, (int) speedCalc);

        setPower(power);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        calculatePower(user.getCurrentSpeed(), moveTarget.getCurrentSpeed());

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
