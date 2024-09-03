package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class OnSwitchInAbilities {
    private static boolean success = false;

    public static boolean execute(Pokemon user, Move move) {
        String ability = user.getCurrentAbility();

        pressure(ability, user);

        if (success) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + "'s " + ability + " activated!");
        }
        
        boolean flag = success;
        success = false;
        return flag;
    }

    private static void pressure(String ability, Pokemon user) {
        if (ability.equals("Pressure")) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " is exerting its pressure!");
        }
    }
}
