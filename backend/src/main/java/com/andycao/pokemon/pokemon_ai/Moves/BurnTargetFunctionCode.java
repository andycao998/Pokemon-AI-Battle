package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;

public class BurnTargetFunctionCode extends Move {
    public void burn(Pokemon target, int effectChance, boolean status) {
        boolean burn = false;

        if (!target.getStatus().isEmpty() && status) {
            if (target.getStatus().equals("Burn")) {
                TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + "'s already burned!");
            }
            else {
                TurnEventMessageBuilder.getInstance().appendEvent("But it failed!");
            }
            
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(target), true);
            return;
        }

        if (target.canBurn(BattleManager.getInstance().getOpposing(target))) {
            if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
                burn = true;
            }
        }
        else {
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " cannot be burned!");
        }

        if (burn) {
            target.setBurned();
            // System.out.println(target.getName() + " was burned!");
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " is burned!");
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (getCategory().equals("Status")) {
            burn(moveTarget, getEffectChance(), true);
        }
        else {
            BattleManager.getInstance().dealDamage(moveTarget, this);
            burn(moveTarget, getEffectChance(), false);
        }
    }
}
