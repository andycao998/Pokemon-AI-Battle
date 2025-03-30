package com.andycao.pokemon.pokemon_ai.Moves;

import java.util.Random;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class MultiTurnAttackConfuseUserAtEndFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);

        int turnCounter = user.getLockedMove().getRight();
        if (damage > 0 && turnCounter == 0) {
            Random random = new Random();
            int maxTurns = random.nextInt(2) + 2;
            user.setLockedMove(getId(), maxTurns, maxTurns, maxTurns);
        }
        else if (damage == 0) {
            user.setLockedMove("", user.getLockedMove().getMiddle(), 0, user.getLockedMove().getRight() - 1);
        }
    }
}
