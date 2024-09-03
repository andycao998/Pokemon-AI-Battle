package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ProtectUserFromTargetingMovesSpikyShieldFunctionCode extends Move {
    public void damageAttacker(Pokemon target, boolean activated) {
        Pokemon user = BattleManager.getInstance().getOpposing(target);

        if (!activated) {
            int damage = (int) Math.floor((double) target.getMaxHp() / 8);
            target.receiveDamage(damage, user);
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " was hurt by the spiky shield!");
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        ProtectUserFunctionCode protectUser = new ProtectUserFunctionCode();
        protectUser.protect(moveTarget, "Spiky Shield");
    }
}
