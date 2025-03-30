package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DoublePowerIfUserPoisonedBurnedParalyzedFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (user.getStatus().equals("Burn") || user.getStatus().equals("Paralysis") || user.getStatus().equals("Poison")) {
            setPower(getPower() * 2);
            TurnEventMessageBuilder.getInstance().appendEvent("Under a status ailment, Facade's power is doubled!");
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }   
}
