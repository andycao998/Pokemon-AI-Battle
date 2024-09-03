package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StatLossImmunityAbilities {
    private static boolean success = false;

    public static boolean execute(Pokemon user, Move move, Pokemon inflicter) {
        String ability = user.getCurrentAbility();

        preventStatDrop(ability, user, inflicter);
        
        boolean flag = success;
        success = false;
        return flag;
    }

    private static void preventStatDrop(String ability, Pokemon user, Pokemon inflicter) {
        if (user.equals(inflicter)) {
            return;
        }

        if (ability.equals("Clear Body") || ability.equals("Full Metal Body") || ability.equals("White Smoke")) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + "'s stats cannot be lowered!");
            success = true;
        }
    }
}
