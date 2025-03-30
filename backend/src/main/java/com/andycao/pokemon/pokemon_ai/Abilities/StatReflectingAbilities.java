package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StatReflectingAbilities {
    private static boolean success = false;

    public static boolean execute(Pokemon user, Move move, Pokemon inflicter, String stat, int statStage) {
        String ability = user.getCurrentAbility();

        mirrorArmor(ability, user, inflicter, stat, statStage);
        
        boolean flag = success;
        success = false;
        return flag;
    }

    private static void mirrorArmor(String ability, Pokemon user, Pokemon inflicter, String stat, int statStage) {
        if (!ability.equals("Mirror Armor")) {
            return;
        }

        if (user.equals(inflicter)) {
            return;
        }

        if (statStage >= 0) {
            return;
        }

        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + "'s " + ability + " activated!");

        if (stat.equals("Attack")) {
            inflicter.updateAttackStage(statStage, inflicter, false);
        }
        else if (stat.equals("Defense")) {
            inflicter.updateDefenseStage(statStage, inflicter, false);
        }
        else if (stat.equals("SpAttack")) {
            inflicter.updateSpAttackStage(statStage, inflicter, false);
        }
        else if (stat.equals("SpDefense")) {
            inflicter.updateSpDefenseStage(statStage, inflicter, false);
        }
        else if (stat.equals("Speed")) {
            inflicter.updateSpeedStage(statStage, inflicter, false);
        }
        else if (stat.equals("Accuracy")) {
            inflicter.updateAccuracyStage(statStage, inflicter, false);
        }
        else if (stat.equals("Evasion")) {
            inflicter.updateEvasionStage(statStage, inflicter, false);
        }

        success = true;
    }
}
