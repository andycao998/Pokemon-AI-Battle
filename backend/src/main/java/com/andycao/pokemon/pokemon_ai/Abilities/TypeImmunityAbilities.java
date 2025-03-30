package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public abstract class TypeImmunityAbilities {
    private static boolean success = false;

    public static boolean execute(Pokemon user, Move move) {
        String ability = user.getCurrentAbility();

        voltAbsorb(ability, user, move);
        levitate(ability, user, move);

        if (success) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + "'s " + ability + " negated the " + move.getType() + "-Type move!");
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(user), true);
        }
        
        boolean flag = success;
        success = false;
        return flag;
    }

    private static void voltAbsorb(String ability, Pokemon user, Move move) {
        if (!move.getType().equals("ELECTRIC")) {
            return;
        }

        if (ability.equals("Volt Absorb")) {
            user.receiveHealing((int) Math.floor((double) user.getMaxHp() / 4));
            success = true;
        }
    }

    private static void levitate(String ability, Pokemon user, Move move) {
        if (!move.getType().equals("GROUND") || move.getCategory().equals("Status")) {
            return;
        }

        if (ability.equals("Levitate")) {
            success = true;
        }
    }
}