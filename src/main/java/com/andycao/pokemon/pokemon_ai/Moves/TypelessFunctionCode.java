package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class TypelessFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealTypelessDamage(moveTarget, this);

        if (getName().equals("Struggle")) {
            Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
            int recoil = (int) Math.floor(((double) user.getMaxHp() / 4));
            user.receiveDamage(recoil, user);
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " is hit with recoil!");
        }
    }
}
