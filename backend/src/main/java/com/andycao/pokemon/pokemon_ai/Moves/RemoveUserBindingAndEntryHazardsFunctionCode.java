package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RemoveUserBindingAndEntryHazardsFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);

        if (damage == 0) {
            return;
        }

        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        user.updateSpeedStage(1, user, false);

        BattleManager.getInstance().removeEntryHazards(user);

        if (user.getLeeched()) {
            user.setLeeched(false);
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " is no longer leeched!");
        }

        if (user.getBoundTurns() > 0) {
            user.removeBind();
        }
    }
}
