package com.andycao.pokemon.pokemon_ai.Moves;

import java.util.Random;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ParalyzeBurnOrFreezeTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);

        if (damage == 0) {
            return;
        }

        String[] statuses = {"Paralysis", "Burn", "Freeze"};
        Random random = new Random();
        String chosenStatus = statuses[random.nextInt(3)];

        if (chosenStatus.equals("Paralysis")) {
            ParalyzeTargetFunctionCode paralyzeFunction = new ParalyzeTargetFunctionCode();
            paralyzeFunction.paralyze(moveTarget, getEffectChance(), false, getType());
        }
        else if (chosenStatus.equals("Burn")) {
            BurnTargetFunctionCode burnFunction = new BurnTargetFunctionCode();
            burnFunction.burn(moveTarget, getEffectChance(), false);
        }
        else {
            FreezeTargetFunctionCode freezeFunction = new FreezeTargetFunctionCode();
            freezeFunction.freeze(moveTarget, getEffectChance(), false);
        }
    }
}
