package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class OnSwitchOutAbilities {
    private static boolean success = false;

    public static boolean execute(Pokemon user, Move move) {
        String ability = user.getCurrentAbility();

        regenerator(ability, user);

        if (success) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + "'s " + ability + " activated!");
        }
        
        boolean flag = success;
        success = false;
        return flag;
    }

    private static void regenerator(String ability, Pokemon user) {
        if (ability.equals("Regenerator")) {
            user.receiveHealing((int) Math.floor((double) user.getMaxHp() / 4));
            success = true;
        }
    }
}
