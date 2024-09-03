package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;

public class RecoilThirdOfDamageDealtFunctionCode extends Move {
    private int calculateRecoil(int damage, int divisor) {
        int recoil = (int) Math.floor(((double) 1 / divisor) * damage);

        if (recoil < 1) {
            recoil = 1;
        }

        return recoil;
    }

    public void recoil(Pokemon moveTarget, Move move, int divisor) {
        int damage = BattleManager.getInstance().dealDamage(moveTarget, move);
        int recoil = calculateRecoil(damage, divisor);

        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (!user.getStatus().equals("Fainted")) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " was damaged by the recoil!");
            user.receiveDamage(recoil, user);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        recoil(moveTarget, this, 3);
    }
}
