package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DoublePowerAfterFusionFlareFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        Move userLastMove = BattleManager.getInstance().getPokemonLastMove(user);
        Move targetLastMove = BattleManager.getInstance().getPokemonLastMove(moveTarget);

        if (userLastMove.getName().equals("Fusion Flare") || targetLastMove.getName().equals("Fusion Flare")) {
            setPower(getPower() * 2);
            TurnEventMessageBuilder.getInstance().appendEvent("Fusion Bolt doubled in power from a previous Fusion Flare!");
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
