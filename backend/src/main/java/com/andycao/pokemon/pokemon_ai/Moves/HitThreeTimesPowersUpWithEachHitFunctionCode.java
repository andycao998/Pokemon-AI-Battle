package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HitThreeTimesPowersUpWithEachHitFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        HitTwoToFiveTimesFunctionCode hitTimes = new HitTwoToFiveTimesFunctionCode();

        for (int i = 1; i <= 3; i++) {
            BattleManager.getInstance().dealDamage(moveTarget, this);

            if (!BattleManager.getInstance().passAccuracyCheck(hitTimes, user, moveTarget) || hitTimes.checkFaintedOrInterrupted(user, moveTarget)) {
                TurnEventMessageBuilder.getInstance().appendEvent(getName() + " hit " + i + " times!");
                return;
            }

            setPower(getPower() + 20);
        }
    }
}
