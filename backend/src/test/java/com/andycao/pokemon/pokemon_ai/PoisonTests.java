package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PoisonTests {
    @Test
    void playerPokemonCanBePoisoned() {
        String id = "Test1";
        String inputs = "BULKUP\nPOISONGAS\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {484, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 484 is Palkia and 89 is Muk)
                         89, 330, 823, 637, 591, 442}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", // Bulk Up in this case is a filler move that shouldn't affect poisoning
                          "POISONGAS", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; // Only fill out moves necessary for testing

        EffectChanceRandomizer.setTestSeed(0); // Seeded so that Poison Gas never misses

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedPlayerPokemonStatus = "Poison";
        String actualPlayerPokemonStatus = BattleContextHolder.get().getPlayerActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedPlayerPokemonStatus, actualPlayerPokemonStatus);
    }

    @Test
    void botPokemonCanBePoisoned() {
        String id = "Test2";
        String inputs = "POISONGAS\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"POISONGAS", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedBotPokemonStatus = "Poison";
        String actualBotPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonStatus, actualBotPokemonStatus);
    }

    @Test
    void steelTypesCannotBePoisoned() {
        String id = "Test3";
        String inputs = "POISONGAS\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         823, 330, 823, 637, 591, 442}; // Corviknight is part Steel
        String[] moves = {"POISONGAS", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedBotPokemonStatus = ""; // No status effect
        String actualBotPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonStatus, actualBotPokemonStatus);
    }

    @Test
    void poisonTypesCannotBePoisoned() {
        String id = "Test4";
        String inputs = "POISONGAS\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         454, 330, 823, 637, 591, 442}; // Toxicroak is part Poison
        String[] moves = {"POISONGAS", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedBotPokemonStatus = "";
        String actualBotPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonStatus, actualBotPokemonStatus);
    }

    @Test
    void endOfTurnPoisonDamage() {
        String id = "Test5";
        String inputs = "POISONGAS\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"POISONGAS", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8);
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void endOfTurnPoisonDamageIsAlwaysConsistent() {
        String id = "Test5";
        String inputs = "POISONGAS\nBULKUP\nPOISONGAS\nBULKUP\n";
        int numTurns = 3;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"POISONGAS", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (2 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8));
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void pokemonCanBeBadlyPoisoned() {
        String id = "Test6";
        String inputs = "TOXIC\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXIC", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedBotPokemonStatus = "BadPoison";
        String actualBotPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonStatus, actualBotPokemonStatus);
    }

    @Test
    void endOfTurnOneToxicDamage() {
        String id = "Test7";
        String inputs = "TOXIC\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXIC", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16);
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    // Toxic damage increases by 1/16 max HP damage every turn of poison (1/16 + 2/16)
    @Test
    void endOfTurnTwoToxicDamage() {
        String id = "Test8";
        String inputs = "TOXIC\nBULKUP\nTOXIC\nBULKUP\n";
        int numTurns = 3;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXIC", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - ((int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16) + 
                                                                                                 (2 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16)));
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void endOfTurnThreeToxicDamage() {
        String id = "Test9";
        String inputs = "TOXIC\nBULKUP\nTOXIC\nBULKUP\nTOXIC\nBULKUP\n";
        int numTurns = 4;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXIC", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - ((int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16) + 
                                                                                                 (2 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16)) + 
                                                                                                 (3 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16)));
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void endOfTurnFourToxicDamage() {
        String id = "Test10";
        String inputs = "TOXIC\nBULKUP\nTOXIC\nBULKUP\nTOXIC\nBULKUP\nTOXIC\nBULKUP\n";
        int numTurns = 5;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXIC", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - ((int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16) + 
                                                                                                 (2 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16)) + 
                                                                                                 (3 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16)) +
                                                                                                 ((int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 4))); // 4/16 = 1/4
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void toxicDamageResetsOnSwitch() {
        String id = "Test11";
        String inputs = "TOXIC\nBULKUP\nTOXIC\nSWITCH Corviknight\nTOXIC\nSWITCH Palkia\n";
        int numTurns = 4;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXIC", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Two turns of poison damage each at 1/16 max HP because switching reset the damage
        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (2 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16));
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void poisonIsStillActiveAfterSwitching() {
        String id = "Test12";
        String inputs = "TOXIC\nBULKUP\nTOXIC\nSWITCH Corviknight\nSUNNYDAY\nSWITCH Palkia\n";
        int numTurns = 4;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXIC", "SUNNYDAY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Muk doesn't reapply Toxic, so Palkia must still be poisoned to take two ticks of poison damage
        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (2 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16));
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void pokemonBehindSubstituteCannotBePoisoned() {
        String id = "Test13";
        String inputs = "POISONGAS\nSUBSTITUTE\n";
        int numTurns = 2;

        int[] pokemon = {89, 135, 468, 791, 230, 452,
                         330, 454, 823, 637, 591, 442};
        String[] moves = {"POISONGAS", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "SUBSTITUTE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Flygon should not have a status (poison) because it went first and used Substitute
        String expectedBotPokemonStatus = "";
        String actualBotPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonStatus, actualBotPokemonStatus);
    }
}
