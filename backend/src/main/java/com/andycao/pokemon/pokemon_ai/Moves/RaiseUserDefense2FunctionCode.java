package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserDefense2FunctionCode extends Move {
    private void raiseDefense2(Pokemon target, int effectChance, boolean status) {
        if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
            target.updateDefenseStage(2, target, false);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!getCategory().equals("Status")) {
            Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
            raiseDefense2(user, getEffectChance(), false);
        }
        else {
            raiseDefense2(moveTarget, getEffectChance(), true);
        }
    }
}
