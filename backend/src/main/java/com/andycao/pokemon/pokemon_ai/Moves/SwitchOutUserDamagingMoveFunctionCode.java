package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class SwitchOutUserDamagingMoveFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);
        // Wimp Out and Emergency Exit

        SwitchOutUserStatusMoveFunctionCode switchCheckFunction = new SwitchOutUserStatusMoveFunctionCode();

        if (!switchCheckFunction.canSwitch(user)) {
            return;
        }

        if (damage > 0 && !BattleManager.getInstance().endBattle()) {
            // Bypass trapping effects
            BattleManager.getInstance().switchPokemon(user, getName());
        }
    }
}
