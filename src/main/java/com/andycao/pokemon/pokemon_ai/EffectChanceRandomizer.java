package com.andycao.pokemon.pokemon_ai;

import java.util.Random;

public class EffectChanceRandomizer {
    // Evaluate random chance scenarios (Ex: 30% chance to Burn target)
    public static boolean evaluate(int chance, int range) {
        Random rand = new Random();
        int randomNumber = rand.nextInt(range) + 1;

        if (randomNumber <= chance) {
            return true;
        }

        return false;
    }
}
