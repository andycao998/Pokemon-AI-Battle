package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ConfuseTargetFunctionCode extends Move {
    public void confuse(Pokemon target, int effectChance, boolean status) {
        boolean confuse = false;

        if (target.getConfused() && status) {
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + "'s already confused!");
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(target), true);
            return;
        }

        if (target.canConfuse(BattleManager.getInstance().getOpposing(target))) {
            if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
                confuse = true;
            }
        }

        if (confuse) {
            target.setConfused(true);
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " is confused!");
        }
    }


    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (getCategory().equals("Status")) {
            confuse(moveTarget, getEffectChance(), true);
        }
        else {
            BattleManager.getInstance().dealDamage(moveTarget, this);
            confuse(moveTarget, getEffectChance(), false);
        }
    }
}
