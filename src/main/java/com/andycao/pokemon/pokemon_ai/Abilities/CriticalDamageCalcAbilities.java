package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class CriticalDamageCalcAbilities {
    public static double execute(Pokemon target, Move move) {
        String ability = target.getCurrentAbility();

        double multiplier = 1.5;
        multiplier = sniper(ability);
        
        return multiplier;
    }

    private static double sniper(String ability) {
        if (ability.equals("Sniper")) {
            return 2.25;
        }

        return 1.5;
    }
}
