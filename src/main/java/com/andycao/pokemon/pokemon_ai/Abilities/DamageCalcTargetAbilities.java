package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class DamageCalcTargetAbilities {
    private static boolean success = false;

    public static boolean execute(Pokemon target, Move move) {
        String ability = target.getCurrentAbility();

        thickFat(ability, target, move);
        
        boolean flag = success;
        success = false;
        return flag;
    }

    private static void thickFat(String ability, Pokemon target, Move move) {
        String type = move.getType();
        if (!type.equals("FIRE") && !type.equals("ICE")) {
            return;
        }

        if (ability.equals("Thick Fat")) {
            Pokemon user = BattleManager.getInstance().getOpposing(target);
            user.applyTempStatChange("Attack", 0.5);
            user.applyTempStatChange("SpAttack", 0.5);

            success = true;
            TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + "'s Thick Fat halved the damage!");
        }
    }
}
