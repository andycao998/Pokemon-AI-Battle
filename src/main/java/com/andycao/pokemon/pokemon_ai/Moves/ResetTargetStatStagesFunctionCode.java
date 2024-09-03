package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ResetTargetStatStagesFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);

        if (damage > 0) {
            moveTarget.resetStatChanges();
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + "'s stat changes were reset by clear smog!");
        }
    }
}
