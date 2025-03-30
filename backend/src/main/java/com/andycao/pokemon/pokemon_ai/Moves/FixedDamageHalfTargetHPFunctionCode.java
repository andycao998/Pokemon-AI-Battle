package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class FixedDamageHalfTargetHPFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int damage = (int) Math.floor((double) moveTarget.getMaxHp() / 2);
        damage = Math.max(damage, 1);

        BattleManager.getInstance().dealDirectDamage(moveTarget, this, damage);
    }
}
