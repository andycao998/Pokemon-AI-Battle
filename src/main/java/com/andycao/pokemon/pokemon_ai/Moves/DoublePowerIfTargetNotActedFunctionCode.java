package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import org.apache.commons.lang3.tuple.Pair;

public class DoublePowerIfTargetNotActedFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pair<Move, Integer> targetMoveChoice = BattleManager.getInstance().getPokemonMoveChoice(moveTarget);
        int order = targetMoveChoice.getValue();

        if (order == 1) {
            setPower(getPower() * 2);
            TurnEventMessageBuilder.getInstance().appendEvent(getName() + "'s power doubled from acting first!");
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
