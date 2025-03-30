package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DisableTargetSoundMovesFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        if (moveTarget.getSoundBlockedTurns() > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " already had its throat chopped!");
        }
        else {
            moveTarget.setSoundBlocked(2);
        }
    }
}
