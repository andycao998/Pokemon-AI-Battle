package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserMainStats1FunctionCode extends Move {
    public void raiseMainStats1(Pokemon target, int effectChance, boolean status) {
        if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
            target.updateAttackStage(1, target, false);
            target.updateDefenseStage(1, target, false);
            target.updateSpAttackStage(1, target, false);
            target.updateSpDefenseStage(1, target, false);
            target.updateSpeedStage(1, target, false);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!getCategory().equals("Status")) {
            BattleManager.getInstance().dealDamage(moveTarget, this);
            raiseMainStats1(moveTarget, getEffectChance(), false);
        }
        else {
            raiseMainStats1(moveTarget, getEffectChance(), true);
        }
    }
}
