package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HealUserFullyAndFallAsleepFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (moveTarget.getStatus().equals("Sleep")) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is already asleep!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        if (moveTarget.getCurrentHp() == moveTarget.getMaxHp()) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is at full HP!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        if (!moveTarget.canSleep(moveTarget)) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " cannot fall asleep!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }
        
        moveTarget.setAsleep(3);
        moveTarget.receiveHealing(moveTarget.getMaxHp());

        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " fell asleep and healed!");
    }
}
