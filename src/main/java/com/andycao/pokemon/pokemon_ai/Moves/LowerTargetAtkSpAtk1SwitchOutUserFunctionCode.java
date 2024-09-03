package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class LowerTargetAtkSpAtk1SwitchOutUserFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        SwitchOutUserStatusMoveFunctionCode switchCheckFunction = new SwitchOutUserStatusMoveFunctionCode();

        if (!switchCheckFunction.canSwitch(user)) {
            return;
        }

        if (moveTarget.getAttackStage() == -6 && moveTarget.getSpAttackStage() == -6) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " was unable to switch out as its threat failed!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        if (!BattleManager.getInstance().endBattle()) {
            // Bypass trapping effects
            BattleManager.getInstance().switchPokemon(user, getName());
        }
    }
}
