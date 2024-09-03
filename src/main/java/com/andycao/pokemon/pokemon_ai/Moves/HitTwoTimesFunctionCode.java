package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HitTwoTimesFunctionCode extends Move {
    public int hitTwice(Pokemon target, Move move) {
        HitTwoToFiveTimesFunctionCode hitTimes = new HitTwoToFiveTimesFunctionCode();
        Pokemon user = BattleManager.getInstance().getOpposing(target);

        int numHits = 0;
        for (int i = 1; i <= 2; i++) {
            BattleManager.getInstance().dealDamage(target, move);

            if (hitTimes.checkFaintedOrInterrupted(user, target)) {
                numHits = i;
                break;
            }
        }

        TurnEventMessageBuilder.getInstance().appendEvent(getName() + " hit " + numHits + " times!");
        return numHits;
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        hitTwice(moveTarget, this);
    }
}
