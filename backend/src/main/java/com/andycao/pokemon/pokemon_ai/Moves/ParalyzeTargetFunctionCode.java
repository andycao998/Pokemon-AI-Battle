package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;

public class ParalyzeTargetFunctionCode extends Move {
    public void paralyze(Pokemon target, int effectChance, boolean status, String moveType) {
        boolean paralyze = false;

        if (!target.getStatus().isEmpty() && status) {
            if (target.getStatus().equals("Paralysis")) {
                TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + "'s already paralyzed!");
            }
            else {
                TurnEventMessageBuilder.getInstance().appendEvent("But it failed!");
            }
            
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(target), true);
            return;
        }

        if (target.canParalyze(moveType, BattleManager.getInstance().getOpposing(target))) {
            if (status || EffectChanceRandomizer.evaluate(effectChance, 100)) {
                paralyze = true;
            }
        }

        if (paralyze) {
            target.setParalyzed();
            // System.out.println(target.getName() + " was paralyzed! It may be unable to move!");
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " is paralyzed! It may be unable to move!");
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (getCategory().equals("Status")) {
            paralyze(moveTarget, getEffectChance(), true, getType());
        }
        else {
            BattleManager.getInstance().dealDamage(moveTarget, this);
            paralyze(moveTarget, getEffectChance(), false, getType());
        }
    }
}
