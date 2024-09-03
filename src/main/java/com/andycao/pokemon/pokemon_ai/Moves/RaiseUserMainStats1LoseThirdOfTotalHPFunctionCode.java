package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserMainStats1LoseThirdOfTotalHPFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int damage = (int) Math.floor((double) moveTarget.getMaxHp() / 3);

        if (damage >= moveTarget.getCurrentHp()) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " doesn't have enough HP!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        moveTarget.receiveDamage(damage, moveTarget);

        RaiseUserMainStats1FunctionCode raiseMainStats = new RaiseUserMainStats1FunctionCode();
        raiseMainStats.raiseMainStats1(moveTarget, getEffectChance(), true);

        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " cut down some of its HP to raise its stats!");
    }
}
