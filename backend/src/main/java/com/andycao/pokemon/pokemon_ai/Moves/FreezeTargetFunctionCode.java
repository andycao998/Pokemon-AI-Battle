package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class FreezeTargetFunctionCode extends Move {
    public void freeze(Pokemon target, int effectChance, boolean status) {
        boolean freeze = false;

        if (target.canFreeze(BattleManager.getInstance().getOpposing(target))) {
            if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
                freeze = true;
            }
        }

        if (freeze) {
            target.setFrozen();
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " was frozen!");
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);
        freeze(moveTarget, getEffectChance(), false);
    }
}
