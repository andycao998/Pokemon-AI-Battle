package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DoublePowerIfTargetLostHPThisTurnFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (moveTarget.getLostHpThisTurn() > 0) {
            setPower(getPower() * 2);
            TurnEventMessageBuilder.getInstance().appendEvent(getName() + "'s power doubled because " + moveTarget.getName() + " lost health this turn!");
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
