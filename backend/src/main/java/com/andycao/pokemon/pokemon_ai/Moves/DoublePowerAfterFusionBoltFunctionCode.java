package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DoublePowerAfterFusionBoltFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        Move userLastMove = BattleManager.getInstance().getPokemonLastMove(user);
        Move targetLastMove = BattleManager.getInstance().getPokemonLastMove(moveTarget);

        if (userLastMove.getName().equals("Fusion Bolt") || targetLastMove.getName().equals("Fusion Bolt")) {
            setPower(getPower() * 2);
            TurnEventMessageBuilder.getInstance().appendEvent("Fusion Flare doubled in power from a previous Fusion Bolt!");
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
