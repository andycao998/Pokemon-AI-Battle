package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;

public class LowerTargetSpDef1FunctionCode extends Move {
    public void lowerSpDef1(Pokemon target, int effectChance) {
        Pokemon user = BattleManager.getInstance().getOpposing(target);

        if (EffectChanceRandomizer.evaluate(effectChance, 100)) {
            target.updateSpDefenseStage(-1, user, false);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);
        lowerSpDef1(moveTarget, getEffectChance());
    }
}
