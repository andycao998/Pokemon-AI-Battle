package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class PoisonTargetFunctionCode extends Move {
    public void poison(Pokemon target, int effectChance, boolean status) {
        boolean poison = false;

        if (!target.getStatus().isEmpty() && status) {
            if (target.getStatus().equals("Poison") || target.getStatus().equals("BadPoison")) {
                TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + "'s already poisoned!");
            }
            else {
                TurnEventMessageBuilder.getInstance().appendEvent("But it failed!");
            }
            
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(target), true);
            return;
        }

        if (target.canPoison(BattleManager.getInstance().getOpposing(target))) {
            if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
                poison = true;
            }
        }

        if (poison) {
            target.setPoisoned(false);
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " was poisoned!");
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (getCategory().equals("Status")) {
            poison(moveTarget, getEffectChance(), true);
        }
        else {
            BattleManager.getInstance().dealDamage(moveTarget, this);
            poison(moveTarget, getEffectChance(), false);
        }
    }
}
