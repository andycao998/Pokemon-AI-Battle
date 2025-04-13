package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UserMakeSubstituteFunctionCode extends Move {
    public void makeSub(Pokemon moveTarget) {
        int subHp = (int) Math.floor((double) moveTarget.getMaxHp() / 4);
        if (moveTarget.getCurrentHp() <= subHp) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " could not make a substitute!");
            return;
        }

        if (BattleManager.getInstance().getPokemonSubstitute(moveTarget) > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " already has a substitute!");
            return;
        }

        if (moveTarget.getBoundTurns() > 0) {
            moveTarget.removeBind();
        }

        moveTarget.receiveDamage(subHp, moveTarget);
        BattleManager.getInstance().setPokemonSubstitute(moveTarget, true, subHp);
        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " created a substitute using its HP!");
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        makeSub(moveTarget);
    }
}
