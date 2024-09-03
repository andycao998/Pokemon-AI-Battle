package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class OnHitTargetAbilities {
    private static boolean success = false;

    public static boolean execute(Pokemon target, Move move) {
        String ability = target.getCurrentAbility();

        flameBody(ability, target, move);
        
        boolean flag = success;
        success = false;
        return flag;
    }

    private static void flameBody(String ability, Pokemon target, Move move) {
        if (!ability.equals("Flame Body")) {
            return;
        }

        if (!move.containsFlag("Contact")) {
            return;
        }

        Pokemon user = BattleManager.getInstance().getOpposing(target);
        if (user.canBurn(target) && EffectChanceRandomizer.evaluate(3, 10)) {
            user.setBurned();
            success = true;

            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " was burned by " + target.getName() + "'s " + ability + "!");
        }
    }
}
