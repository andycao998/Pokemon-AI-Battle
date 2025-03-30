package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StartWeakenPhysicalDamageAgainstUserSideFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getReflect(moveTarget)) {
            TurnEventMessageBuilder.getInstance().appendEvent("Reflect is already up!");
            return;
        }

        BattleManager.getInstance().setReflect(moveTarget);
        TurnEventMessageBuilder.getInstance().appendEvent("Reflect made " + moveTarget.getName() + "'s side resistant to physical moves!");
    }
}
