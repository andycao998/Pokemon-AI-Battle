package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class SleepTargetFunctionCode extends Move {
    public void sleep(Pokemon target, int effectChance, boolean status) {
        boolean sleep = false;

        if (!target.getStatus().isEmpty() && status) {
            if (target.getStatus().equals("Sleep")) {
                TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + "'s already asleep!");
            }
            else {
                TurnEventMessageBuilder.getInstance().appendEvent("But it failed!");
            }
            
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(target), true);
            return;
        }

        if (target.canSleep(BattleManager.getInstance().getOpposing(target))) {
            if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
                sleep = true;
            }
        }

        if (sleep) {
            target.setAsleep();
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " fell asleep!");
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (getCategory().equals("Status")) {
            sleep(moveTarget, getEffectChance(), true);
        }
        else {
            BattleManager.getInstance().dealDamage(moveTarget, this);
            sleep(moveTarget, getEffectChance(), false);
        }
    }
}
