package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UserTargetAverageHPFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        int averageHp = (int) Math.floor((double) (moveTarget.getCurrentHp() + user.getCurrentHp()) / 2);

        moveTarget.setCurrentHp(averageHp);
        user.setCurrentHp(averageHp);

        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " has " + user.getCurrentHp() + " HP and " + 
                                                          moveTarget.getName() + " has " + moveTarget.getCurrentAbility() + "HP!");
    }
}
