package com.andycao.pokemon.pokemon_ai.Moves;

import java.util.Random;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HitTwoToFiveTimesFunctionCode extends Move {
    public int getNumHits() {
        Random random = new Random();
        int num = random.nextInt(100) + 1;

        if (num <= 35) {
            return 2;
        }
        else if (num <= 70) {
            return 3;
        }
        else if (num <= 85) {
            return 4;
        }
        else {
            return 5;
        }
    }

    public boolean checkFaintedOrInterrupted(Pokemon user, Pokemon target) {
        String userStatus = user.getStatus();
        String targetStatus = target.getStatus();

        if (userStatus.equals("Sleep") || userStatus.equals("Fainted") || targetStatus.equals("Fainted")) {
            return true;
        }

        return false;
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int numHits = getNumHits();
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        for (int i = 1; i <= numHits; i++) {
            BattleManager.getInstance().dealDamage(moveTarget, this);

            if (checkFaintedOrInterrupted(user, moveTarget)) {
                numHits = i;
                break;
            }
        }

        TurnEventMessageBuilder.getInstance().appendEvent(getName() + " hit " + numHits + " times!");
    }
}
