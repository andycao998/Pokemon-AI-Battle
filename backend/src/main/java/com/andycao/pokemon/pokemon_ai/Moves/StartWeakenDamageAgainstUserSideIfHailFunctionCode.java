package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StartWeakenDamageAgainstUserSideIfHailFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!BattleManager.getInstance().getWeather().equals("Hail")) {
            TurnEventMessageBuilder.getInstance().appendEvent("Aurora Veil cannot manifest without hail!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        if (BattleManager.getInstance().getAuroraVeil(moveTarget)) {
            TurnEventMessageBuilder.getInstance().appendEvent("Aurora Veil is already up!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        BattleManager.getInstance().setAuroraVeil(moveTarget);
        TurnEventMessageBuilder.getInstance().appendEvent("Aurora Veil made " + moveTarget.getName() + "'s side resistant to damaging moves!");
    }
}