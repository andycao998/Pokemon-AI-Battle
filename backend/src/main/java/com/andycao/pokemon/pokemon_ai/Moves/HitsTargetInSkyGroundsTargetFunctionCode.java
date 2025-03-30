package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HitsTargetInSkyGroundsTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        moveTarget.setGrounded(true);

        if (moveTarget.getInvulnerable().equals("Fly")) {
            moveTarget.setInvulnerable("");
            moveTarget.setLockedMove("", moveTarget.getLockedMove().getMiddle(), 0, -1);

            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " was knocked out of the air!");
        }

        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " was grounded!");
    }
}
