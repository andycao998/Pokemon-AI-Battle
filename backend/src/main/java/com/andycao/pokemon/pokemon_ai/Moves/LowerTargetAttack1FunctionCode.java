package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class LowerTargetAttack1FunctionCode extends Move {
    public void lowerAttack1(Pokemon target, int effectChance, boolean status) {
        Pokemon user = BattleManager.getInstance().getOpposing(target);

        if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
            target.updateAttackStage(-1, user, false);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!getCategory().equals("Status")) {
            BattleManager.getInstance().dealDamage(moveTarget, this);
            lowerAttack1(moveTarget, getEffectChance(), false);
        }
        else {
            lowerAttack1(moveTarget, getEffectChance(), true);
        }
    }
}
