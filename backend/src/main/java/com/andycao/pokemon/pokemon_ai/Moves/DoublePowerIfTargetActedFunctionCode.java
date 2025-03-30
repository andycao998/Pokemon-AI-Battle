package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import org.apache.commons.lang3.tuple.Pair;

public class DoublePowerIfTargetActedFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pair<Move, Integer> targetMoveChoice = BattleManager.getInstance().getPokemonMoveChoice(moveTarget);
        Move targetMove = targetMoveChoice.getKey();
        int order = targetMoveChoice.getValue();

        if (order == 0 && !targetMove.getName().isEmpty()) {
            setPower(getPower() * 2);
            TurnEventMessageBuilder.getInstance().appendEvent(getName() + "'s power doubled from acting second!");
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
