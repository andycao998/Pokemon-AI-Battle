package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class TrapTargetInBattleFunctionCode extends Move {
    public void trapTarget(Pokemon moveTarget) {
        if (moveTarget.getBoundTurns() > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is already trapped!");
            return;
        }

        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if ((moveTarget.getType1().equals("Ghost") || moveTarget.getType2().equals("Ghost")) && getCategory().equals("Status")) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " cannot be trapped!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        moveTarget.setBound(user, 1000);
        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " can no longer escape from " + user.getName() + "!");
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!getCategory().equals("Status")) {
            BattleManager.getInstance().dealDamage(moveTarget, this);
        }

        trapTarget(moveTarget);
    }
}
