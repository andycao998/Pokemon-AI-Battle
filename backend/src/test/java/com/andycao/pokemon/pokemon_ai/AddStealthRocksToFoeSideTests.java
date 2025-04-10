package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddStealthRocksToFoeSideTests {
    @Test
    void addStealthRocksToPlayerSide() {
        String id = "Test1";
        String inputs = "BULKUP\nSTEALTHROCK\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {823, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 823 is Corviknight and 473 is Mamoswine)
                         473, 330, 484, 637, 591, 442}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", // Bulk Up in this case is a filler move that shouldn't affect Stealth Rock
                          "STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; // Only fill out moves necessary for testing

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Stealth Rock is used by bot's Pokemon, so bot side shouldn't have rocks
        boolean expectedStealthRocksOnBotSide = false;
        boolean actualStealthRocksOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getStealthRocks();
        boolean expectedStealthRocksOnPlayerSide = true;
        boolean actualStealthRocksOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getStealthRocks();

        assertEquals(expectedStealthRocksOnBotSide, actualStealthRocksOnBotSide);
        assertEquals(expectedStealthRocksOnPlayerSide, actualStealthRocksOnPlayerSide);
    }

    @Test
    void addStealthRocksToBotSide() {
        String id = "Test2";
        String inputs = "STEALTHROCK\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {473, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedStealthRocksOnBotSide = true;
        boolean actualStealthRocksOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getStealthRocks();
        boolean expectedStealthRocksOnPlayerSide = false;
        boolean actualStealthRocksOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getStealthRocks();

        assertEquals(expectedStealthRocksOnBotSide, actualStealthRocksOnBotSide);
        assertEquals(expectedStealthRocksOnPlayerSide, actualStealthRocksOnPlayerSide);
    }

    @Test
    void addMoreThanOneStealthRocksFails() {
        String id = "Test3";
        String inputs = "STEALTHROCK\nBULKUP\nSTEALTHROCK\nBULKUP\n";
        int numTurns = 3;

        int[] pokemon = {473, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedStealthRocksOnBotSide = true;
        boolean actualStealthRocksOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getStealthRocks();
        boolean expectedPlayerMoveFailed = true;
        boolean actualPlayerMoveFailed = BattleContextHolder.get().getActionHandler().getPokemonCurrentMoveFailed(BattleContextHolder.get().getPlayerActivePokemon());

        assertEquals(expectedStealthRocksOnBotSide, actualStealthRocksOnBotSide);
        assertEquals(expectedPlayerMoveFailed, actualPlayerMoveFailed);
    }

    // Stealth Rock should not damage the opposing Pokemon already on the field, only those that switch in
    @Test
    void stealthRocksDidNotDealDamageOnInitialUse() {
        String id = "Test4";
        String inputs = "STEALTHROCK\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {473, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp();
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp); // Check that the Stealth Rock user didn't take damage either
    }

    // Unlike Spikes, Toxic Spikes, or Sticky Web, Stealth Rock will still affect Flying-type Pokemon
    @Test
    void flyingTypeSwitchInShouldTakeDamageFromStealthRocks() {
        String id = "Test5";
        String inputs = "STEALTHROCK\nBULKUP\nSTEALTHROCK\nSWITCH Corviknight\n";
        int numTurns = 3;

        int[] pokemon = {473, 135, 468, 791, 230, 452,
                         330, 823, 484, 637, 591, 442};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Corviknight for this example is neutrally affected by stealth rock (Flying and Steel), taking 1/8 max HP damage
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp);
    }

    @Test
    void neutralToRockDamageFromStealthRocks() {
        String id = "Test6";
        String inputs = "STEALTHROCK\nBULKUP\nSTEALTHROCK\nSWITCH Spiritomb\n";
        int numTurns = 3;

        int[] pokemon = {473, 135, 591, 791, 230, 452,
                         330, 823, 484, 637, 468, 442};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Spiritomb in this example is a Ghost and Dark-type with no interactions with rock (1/8 max HP damage)
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp);
    }

    @Test
    void twoTimesWeakToRockDamageFromStealthRocks() {
        String id = "Test7";
        String inputs = "STEALTHROCK\nBULKUP\nSTEALTHROCK\nSWITCH Togekiss\n";
        int numTurns = 3;

        int[] pokemon = {473, 135, 591, 791, 230, 452,
                         330, 823, 484, 637, 468, 442};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Togekiss in this example is a Flying-type and weak to rock (1/4 max HP damage)
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 4);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp);
    }

    @Test
    void fourTimesWeakToRockDamageFromStealthRocks() {
        String id = "Test8";
        String inputs = "STEALTHROCK\nBULKUP\nSTEALTHROCK\nSWITCH Volcarona\n";
        int numTurns = 3;

        int[] pokemon = {473, 135, 468, 791, 230, 452,
                         330, 823, 484, 637, 591, 442};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Volcarona in this example is a Bug and Fire-type and very weak to rock (1/2 max HP damage)
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 2);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp);
    }

    @Test
    void twoTimesResistantToRockDamageFromStealthRocks() {
        String id = "Test9";
        String inputs = "STEALTHROCK\nBULKUP\nSTEALTHROCK\nSWITCH Flygon\n";
        int numTurns = 3;

        int[] pokemon = {473, 135, 591, 791, 230, 452,
                         823, 330, 484, 637, 468, 442};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Flygon in this example is a Ground-type and resistant to rock (1/16 max HP damage)
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp);
    }

    @Test
    void fourTimesResistantToRockDamageFromStealthRocks() {
        String id = "Test10";
        String inputs = "STEALTHROCK\nBULKUP\nSTEALTHROCK\nSWITCH Cobalion\n";
        int numTurns = 3;

        int[] pokemon = {473, 135, 591, 791, 230, 452,
                         823, 330, 484, 637, 468, 638};
        String[] moves = {"STEALTHROCK", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Cobalion in this example is a Steel and Fighting-type and very resistant to rock (1/32 max HP damage)
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 32);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp);
    }

    // Tests switching after a Pokemon faints from Stealth Rock: use a move that lowers HP to the range that it will faint if it switches in again
    @Test
    void faintingToStealthRocksAllowsSwitchIn() {
        String id = "Test11";
        String inputs = "STEALTHROCK\nBULKUP\nLAVAPLUME\nBULKUP\nSTEALTHROCK\nSWITCH Mamoswine\nSTEALTHROCK\nSWITCH Corviknight\nSWITCH Mamoswine\n"; // After fainting, prompt new Pokemon to send out
        int numTurns = 5;

        int[] pokemon = {637, 135, 591, 791, 230, 452,
                         823, 330, 484, 473, 468, 638};
        String[] moves = {"STEALTHROCK", "LAVAPLUME", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Corviknight in this case faints and the bot switches to Mamoswine... the available party remaining that you can switch to is now 4 Pokemon
        int expectedOpposingPokemonRemaining = 4;
        int actualOpposingPokemonRemaining = BattleContextHolder.get().getBotPartyHandler().getAvailableParty().length;
        String expectedOpposingActivePokemon = "Mamoswine";
        String actualOpposingActivePokemon = BattleContextHolder.get().getBotActivePokemon().getName();

        assertEquals(expectedOpposingPokemonRemaining, actualOpposingPokemonRemaining);
        assertEquals(expectedOpposingActivePokemon, actualOpposingActivePokemon);
    }
}
