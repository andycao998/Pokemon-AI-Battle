package com.andycao.pokemon.pokemon_ai;

import java.util.Random;

public class EffectChanceRandomizer {
    private static int testSeed = -1;

    public static void setTestSeed(int seed) {
        testSeed = seed;
    }

    // Evaluate random chance scenarios (Ex: 30% chance to Burn target)
    public static boolean evaluate(int chance, int range) {
        if (BattleContextHolder.get().isTestBattle() && testSeed != -1) {
            return evaluateSeed(chance, range);
        }

        Random rand = new Random();
        int randomNumber = rand.nextInt(range) + 1;

        if (randomNumber <= chance) {
            return true;
        }

        return false;
    }

    private static boolean evaluateSeed(int chance, int range) {
        Random rand = new Random(testSeed);
        int randomNumber = rand.nextInt(range) + 1;

        if (randomNumber <= chance) {
            return true;
        }

        return false;
    }
}
