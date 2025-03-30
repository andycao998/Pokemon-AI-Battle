package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HealUserByHalfOfDamageDoneFunctionCode extends Move {
    private void healFromDamage(Pokemon user, int damage) {
        // Big root
        user.receiveHealing((int) Math.floor((double) damage / 2));
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);
        healFromDamage(user, damage);
    }
}
