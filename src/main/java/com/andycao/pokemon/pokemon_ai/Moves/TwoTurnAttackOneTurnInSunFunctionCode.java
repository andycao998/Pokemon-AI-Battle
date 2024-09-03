package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class TwoTurnAttackOneTurnInSunFunctionCode extends Move {
    public void firstTurnCharge(Pokemon target, Pokemon user, String moveId) {
        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " is charging a move using the sun!");

        user.setLockedMove(moveId, 1, 100, -1);
    }

    public void secondTurnUnleash(Pokemon target, Pokemon user, Move move) {
        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " unleashed the solar energy!");

        int damage = BattleManager.getInstance().dealDamage(target, move);

        if (damage == 0) {
            user.interruptMultiTurnMove();
        }

        user.setLockedMove("", user.getLockedMove().getMiddle(), 0, -1);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        String activeWeather = BattleManager.getInstance().getWeather();
        if (activeWeather.equals("Harsh Sunlight") || activeWeather.equals("Extremely Harsh Sunlight")) {
            TurnEventMessageBuilder.getInstance().appendEvent("The harsh sun instantly charged the attack!");
            secondTurnUnleash(moveTarget, user, this);
            return;
        }

        if (!user.lockedIntoMove()) {
            firstTurnCharge(moveTarget, user, getId());
        }
        else if (user.getLockedMove().getLeft().equals(getId())) {
            secondTurnUnleash(moveTarget, user, this);
        }
    }
}
