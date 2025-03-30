package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;

public class DoublePowerIfTargetUndergroundFunctionCode extends Move {
    private void dealUndergroundDamage(Pokemon moveTarget, boolean underground) {
        if (underground) {
            setPower(getPower() * 2);
            BattleManager.getInstance().dealDamage(moveTarget, this);
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget + " was underground!");
            setPower(getPower() / 2);
        }
        else {
            BattleManager.getInstance().dealDamage(moveTarget, this);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        dealUndergroundDamage(moveTarget, false);
    }
    
}
