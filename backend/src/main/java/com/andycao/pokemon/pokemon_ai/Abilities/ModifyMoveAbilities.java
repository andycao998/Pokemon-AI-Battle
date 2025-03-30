package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ModifyMoveAbilities {
    private static boolean success = false;

    public static boolean execute(Pokemon user, Move move) {
        String ability = user.getCurrentAbility();

        sereneGrace(ability, move);
        
        boolean flag = success;
        success = false;
        return flag;
    }

    private static void sereneGrace(String ability, Move move) {
        if (ability.equals("Serene Grace")) {
            move.setEffectChance(move.getEffectChance() * 2);
        }
    }
}
