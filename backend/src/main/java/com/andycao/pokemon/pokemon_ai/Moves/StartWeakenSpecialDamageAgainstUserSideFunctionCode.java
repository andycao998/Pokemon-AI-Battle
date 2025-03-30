package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StartWeakenSpecialDamageAgainstUserSideFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getLightScreen(moveTarget)) {
            TurnEventMessageBuilder.getInstance().appendEvent("Light Screen is already up!");
            return;
        }

        BattleManager.getInstance().setLightScreen(moveTarget);
        TurnEventMessageBuilder.getInstance().appendEvent("Light Screen made " + moveTarget.getName() + "'s side resistant to special moves!");
    }
}
