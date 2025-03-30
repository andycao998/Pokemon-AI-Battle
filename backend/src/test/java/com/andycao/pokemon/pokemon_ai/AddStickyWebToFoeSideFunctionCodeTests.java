package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddStickyWebToFoeSideFunctionCodeTests {
    @Test
    void addStickyWebToPlayerSide() {
        String id = "Test1";
        String inputs = "BULKUP\nSTICKYWEB\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {823, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 823 is Corviknight and 738 is Vikavolt)
                         738, 330, 473, 637, 591, 442}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", // Bulk Up in this case is a filler move that shouldn't affect Sticky Web
                          "STICKYWEB", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; // Only fill out moves necessary for testing

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Sticky Web is used by bot's Pokemon, so bot side shouldn't have webs
        boolean expectedStickyWebOnBotSide = false;
        boolean actualStickyWebOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getStickyWeb();
        boolean expectedStickyWebOnPlayerSide = true;
        boolean actualStickyWebOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getStickyWeb();

        assertEquals(expectedStickyWebOnBotSide, actualStickyWebOnBotSide);
        assertEquals(expectedStickyWebOnPlayerSide, actualStickyWebOnPlayerSide);
    }

    @Test
    void addStickyWebToBotSide() {
        String id = "Test2";
        String inputs = "STICKYWEB\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {738, 473, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"STICKYWEB", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedStickyWebOnBotSide = true;
        boolean actualStickyWebOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getStickyWeb();
        boolean expectedStickyWebOnPlayerSide = false;
        boolean actualStickyWebOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getStickyWeb();

        assertEquals(expectedStickyWebOnBotSide, actualStickyWebOnBotSide);
        assertEquals(expectedStickyWebOnPlayerSide, actualStickyWebOnPlayerSide);
    }

    @Test
    void addMoreThanOneStickyWebFails() {
        String id = "Test3";
        String inputs = "STICKYWEB\nBULKUP\nSTICKYWEB\nBULKUP\n";
        int numTurns = 3;

        int[] pokemon = {738, 473, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"STICKYWEB", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedStickyWebOnBotSide = true;
        boolean actualStickyWebOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getStickyWeb();
        boolean expectedPlayerMoveFailed = true;
        boolean actualPlayerMoveFailed = BattleContextHolder.get().getActionHandler().getPokemonCurrentMoveFailed(BattleContextHolder.get().getPlayerActivePokemon());

        assertEquals(expectedStickyWebOnBotSide, actualStickyWebOnBotSide);
        assertEquals(expectedPlayerMoveFailed, actualPlayerMoveFailed);
    }

    // Sticky Web should not slow the opposing Pokemon already on the field, only those that switch in
    @Test
    void stickyWebsDidNotSlowOnInitialUse() {
        String id = "Test4";
        String inputs = "STICKYWEB\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {738, 473, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"STICKYWEB", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedOpposingPokemonSpeedStage = 0;
        int actualOpposingPokemonSpeedStage = BattleContextHolder.get().getBotActivePokemon().getSpeedStage();
        int expectedUserPokemonSpeedStage = 0;
        int actualUserPokemonHpSpeedStage = BattleContextHolder.get().getPlayerActivePokemon().getSpeedStage();

        assertEquals(expectedOpposingPokemonSpeedStage, actualOpposingPokemonSpeedStage);
        assertEquals(expectedUserPokemonSpeedStage, actualUserPokemonHpSpeedStage); // Check that the Sticky Web user didn't get slowed either
    }

    // Sticky Web will not affect Flying-type Pokemon
    @Test
    void flyingTypeSwitchInShouldNotBeAffectedFromStickyWeb() {
        String id = "Test5";
        String inputs = "STICKYWEB\nBULKUP\nSTICKYWEB\nSWITCH Corviknight\n";
        int numTurns = 3;

        int[] pokemon = {738, 473, 468, 791, 230, 452,
                         330, 823, 484, 637, 591, 442};
        String[] moves = {"STICKYWEB", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Corviknight for this example is a Flying-type and immune to Sticky Web slow
        int expectedOpposingPokemonSpeedStage = 0;
        int actualOpposingPokemonSpeedStage = BattleContextHolder.get().getBotActivePokemon().getSpeedStage();
        int expectedUserPokemonSpeedStage = 0;
        int actualUserPokemonHpSpeedStage = BattleContextHolder.get().getPlayerActivePokemon().getSpeedStage();

        assertEquals(expectedOpposingPokemonSpeedStage, actualOpposingPokemonSpeedStage);
        assertEquals(expectedUserPokemonSpeedStage, actualUserPokemonHpSpeedStage);
    }

    @Test
    void nonFlyingTypeSwitchInShouldBeAffectedFromStickyWeb() {
        String id = "Test5";
        String inputs = "STICKYWEB\nBULKUP\nSTICKYWEB\nSWITCH Palkia\n";
        int numTurns = 3;

        int[] pokemon = {738, 473, 468, 791, 230, 452,
                         330, 823, 484, 637, 591, 442};
        String[] moves = {"STICKYWEB", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Palkia for this example is not a Flying-type and is slowed by Sticky Web
        int expectedOpposingPokemonSpeedStage = -1;
        int actualOpposingPokemonSpeedStage = BattleContextHolder.get().getBotActivePokemon().getSpeedStage();
        int expectedUserPokemonSpeedStage = 0;
        int actualUserPokemonHpSpeedStage = BattleContextHolder.get().getPlayerActivePokemon().getSpeedStage();

        assertEquals(expectedOpposingPokemonSpeedStage, actualOpposingPokemonSpeedStage);
        assertEquals(expectedUserPokemonSpeedStage, actualUserPokemonHpSpeedStage);
    }
}
