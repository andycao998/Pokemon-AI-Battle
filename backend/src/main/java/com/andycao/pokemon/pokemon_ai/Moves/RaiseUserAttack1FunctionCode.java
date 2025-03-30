package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserAttack1FunctionCode extends Move {
    public void raiseUserAttack1(Pokemon user, int effectChance, boolean status) {
        if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
            user.updateAttackStage(1, user, false);
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (!getCategory().equals("Status")) {
            BattleManager.getInstance().dealDamage(moveTarget, this);
            raiseUserAttack1(user, getEffectChance(), false);
        }
        else {
            raiseUserAttack1(user, getEffectChance(), true);
        }
    }
}
