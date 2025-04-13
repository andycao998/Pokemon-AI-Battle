package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BurnTests {
    @Test
    void playerPokemonCanBeBurned() {
        String id = "Test1";
        String inputs = "BULKUP\nWILLOWISP\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {484, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 484 is Palkia and 442 is Spiritomb)
                         442, 330, 823, 637, 591, 89}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", // Bulk Up in this case is a filler move that shouldn't affect poisoning
                          "WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; // Only fill out moves necessary for testing

        EffectChanceRandomizer.setTestSeed(0); // Seeded so that Will-O-Wisp never misses

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedPlayerPokemonStatus = "Burn";
        String actualPlayerPokemonStatus = BattleContextHolder.get().getPlayerActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedPlayerPokemonStatus, actualPlayerPokemonStatus);
    }

    @Test
    void botPokemonCanBeBurned() {
        String id = "Test2";
        String inputs = "WILLOWISP\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {442, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedBotPokemonStatus = "Burn";
        String actualBotPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonStatus, actualBotPokemonStatus);
    }

    @Test
    void fireTypesCannotBeBurned() {
        String id = "Test3";
        String inputs = "WILLOWISP\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {442, 135, 468, 791, 230, 452,
                         257, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
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
    void endOfTurnBurnDamage() {
        String id = "Test4";
        String inputs = "WILLOWISP\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {442, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
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

    @Test
    void endOfTurnBurnDamageIsAlwaysConsistent() {
        String id = "Test5";
        String inputs = "WILLOWISP\nBULKUP\nWILLOWISP\nBULKUP\n";
        int numTurns = 3;

        int[] pokemon = {442, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (2 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16));
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void burnIsStillActiveAfterSwitching() {
        String id = "Test6";
        String inputs = "WILLOWISP\nBULKUP\nCALMMIND\nSWITCH Flygon\nCALMMIND\nSWITCH Palkia\n";
        int numTurns = 4;

        int[] pokemon = {442, 135, 468, 791, 230, 452,
                         484, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "CALMMIND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedBotPokemonStatus = "Burn";
        String actualBotPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonStatus, actualBotPokemonStatus);
    }

    @Test
    void burnHalvesPhysicalDamage() {
        String id = "Test7";
        String inputs = "WILLOWISP\nFIREPUNCH\n";
        int numTurns = 2;

        int[] pokemon = {478, 135, 468, 791, 230, 452,
                         68, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "FIREPUNCH", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Froslass goes first and burns Machamp before it can attack
        int expectedPlayerPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp() - 57; // Damage calculation after burn
        int actualPlayerPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedPlayerPokemonHp, actualPlayerPokemonHp);
    }

    @Test
    void burnDoesNotAffectAttackStat() {
        String id = "Test8";
        String inputs = "WILLOWISP\nFIREPUNCH\n";
        int numTurns = 2;

        int[] pokemon = {478, 135, 468, 791, 230, 452,
                         68, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "FIREPUNCH", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonAttack = BattleContextHolder.get().getBotActivePokemon().getUnmodifiedAttack();
        int actualBotPokemonAttack = BattleContextHolder.get().getBotActivePokemon().getCurrentAttack();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonAttack, actualBotPokemonAttack);
    }

    @Test
    void burnDoesNotAffectSpecialDamage() {
        String id = "Test9";
        String inputs = "WILLOWISP\nFLAMETHROWER\n";
        int numTurns = 2;

        int[] pokemon = {478, 135, 468, 791, 230, 452,
                         68, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "FLAMETHROWER", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedPlayerPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp() - 78; // Same damage with and without burn
        int actualPlayerPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedPlayerPokemonHp, actualPlayerPokemonHp);
    }

    @Test
    void burnDoesNotAffectSetDamage() {
        String id = "Test10";
        String inputs = "WILLOWISP\nSEISMICTOSS\n";
        int numTurns = 2;

        int[] pokemon = {655, 135, 468, 791, 230, 452,
                         68, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "SEISMICTOSS", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedPlayerPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp() - 50; // Same damage with and without burn
        int actualPlayerPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedPlayerPokemonHp, actualPlayerPokemonHp);
    }

    @Test
    void pokemonBehindSubstituteCannotBeBurned() {
        String id = "Test9";
        String inputs = "WILLOWISP\nSUBSTITUTE\n";
        int numTurns = 2;

        int[] pokemon = {655, 135, 468, 791, 230, 452,
                         478, 330, 823, 637, 591, 89};
        String[] moves = {"WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "SUBSTITUTE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Froslass should not have a status (poison) because it went first and used Substitute
        String expectedBotPokemonStatus = "";
        String actualBotPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonStatus, actualBotPokemonStatus);
    }
}

// pokemon behind substitute cannot be burned