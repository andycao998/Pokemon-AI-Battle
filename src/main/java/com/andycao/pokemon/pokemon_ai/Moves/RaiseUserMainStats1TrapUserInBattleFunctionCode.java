package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserMainStats1TrapUserInBattleFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (moveTarget.getBoundTurns() > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is already trapped!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        RaiseUserMainStats1FunctionCode raiseMainStats = new RaiseUserMainStats1FunctionCode();
        raiseMainStats.raiseMainStats1(moveTarget, getEffectChance(), true);

        TrapTargetInBattleFunctionCode trapTarget = new TrapTargetInBattleFunctionCode();
        trapTarget.trapTarget(moveTarget);
    }
}
