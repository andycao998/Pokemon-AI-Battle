package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DoublePowerIfUserLastMoveFailedFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        if (BattleManager.getInstance().getPokemonLastMoveFailed(user)) {
            setPower(getPower() * 2);
            TurnEventMessageBuilder.getInstance().appendEvent(getName() + "'s power doubled because " + user.getName() + "'s last move failed!");
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
