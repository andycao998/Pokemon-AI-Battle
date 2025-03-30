package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class AttackerFaintsIfUserFaintsFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (moveTarget.getDestinyBond()) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " already used Destiny Bond last turn!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        moveTarget.setDestinyBond(true);

        Pokemon opposing = BattleManager.getInstance().getOpposing(moveTarget);
        
        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " hopes to take " + opposing.getName() + " with it!");
    }
}
