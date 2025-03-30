package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UserFaintsExplosiveFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        user.setCurrentHp(0);
        user.setFainted();

        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " exploded!");
    }
}
