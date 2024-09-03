package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class PowerHigherWithUserHPFunctionCode extends Move {
    private void calculatePower(Pokemon user) {
        int power = (int) Math.floor((double) (150 * user.getCurrentHp()) / user.getMaxHp());
        setPower(power);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        calculatePower(user);

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
