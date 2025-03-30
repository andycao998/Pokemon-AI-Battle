package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddSpikesToFoeSideFunctionCodeTests {
    @Test
    void oneSpikesAddedToPlayerSide() {
        String id = "Test1";
        String inputs = "BULKUP\nSPIKES\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {823, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 823 is Corviknight and 473 is Mamoswine)
                         473, 330, 484, 637, 591, 442}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", // Bulk Up in this case is a filler move that shouldn't affect spikes
                          "SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; // Only fill out moves necessary for testing

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedSpikesOnBotSide = 0;
        int actualSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getSpikeStacks();
        int expectedSpikesOnPlayerSide = 1;
        int actualSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getSpikeStacks();

        assertEquals(expectedSpikesOnBotSide, actualSpikesOnBotSide);
        assertEquals(expectedSpikesOnPlayerSide, actualSpikesOnPlayerSide);
    }

    @Test
    void twoSpikesAddedToPlayerSide() {
        String id = "Test2";
        String inputs = "BULKUP\nSPIKES\nBULKUP\nSPIKES\n"; // 2 turns: 4 total actions
        int numTurns = 3;

        int[] pokemon = {823, 135, 468, 791, 230, 452, 
                         473, 330, 484, 637, 591, 442};
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedSpikesOnBotSide = 0;
        int actualSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getSpikeStacks();
        int expectedSpikesOnPlayerSide = 2;
        int actualSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getSpikeStacks();

        assertEquals(expectedSpikesOnBotSide, actualSpikesOnBotSide);
        assertEquals(expectedSpikesOnPlayerSide, actualSpikesOnPlayerSide);
    }

    @Test
    void threeSpikesAddedToPlayerSide() {
        String id = "Test3";
        String inputs = "BULKUP\nSPIKES\nBULKUP\nSPIKES\nBULKUP\nSPIKES\n";
        int numTurns = 4;

        int[] pokemon = {823, 135, 468, 791, 230, 452, 
                         473, 330, 484, 637, 591, 442};
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedSpikesOnBotSide = 0;
        int actualSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getSpikeStacks();
        int expectedSpikesOnPlayerSide = 3;
        int actualSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getSpikeStacks();

        assertEquals(expectedSpikesOnBotSide, actualSpikesOnBotSide);
        assertEquals(expectedSpikesOnPlayerSide, actualSpikesOnPlayerSide);
    }

    @Test
    void oneSpikesAddedToBotSide() {
        String id = "Test4";
        String inputs = "SPIKES\nBULKUP\n";
        int numTurns = 2;

		int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedSpikesOnBotSide = 1;
        int actualSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getSpikeStacks();
        int expectedSpikesOnPlayerSide = 0;
        int actualSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getSpikeStacks();

        assertEquals(expectedSpikesOnBotSide, actualSpikesOnBotSide);
        assertEquals(expectedSpikesOnPlayerSide, actualSpikesOnPlayerSide);
    }

    @Test
    void twoSpikesAddedToBotSide() {
        String id = "Test5";
        String inputs = "SPIKES\nBULKUP\nSPIKES\nBULKUP\n";
        int numTurns = 3;

		int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedSpikesOnBotSide = 2;
        int actualSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getSpikeStacks();
        int expectedSpikesOnPlayerSide = 0;
        int actualSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getSpikeStacks();

        assertEquals(expectedSpikesOnBotSide, actualSpikesOnBotSide);
        assertEquals(expectedSpikesOnPlayerSide, actualSpikesOnPlayerSide);
    }

    @Test
    void threeSpikesAddedToBotSide() {
        String id = "Test6";
        String inputs = "SPIKES\nBULKUP\nSPIKES\nBULKUP\nSPIKES\nBULKUP\n";
        int numTurns = 4;

		int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedSpikesOnBotSide = 3;
        int actualSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getSpikeStacks();
        int expectedSpikesOnPlayerSide = 0;
        int actualSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getSpikeStacks();

        assertEquals(expectedSpikesOnBotSide, actualSpikesOnBotSide);
        assertEquals(expectedSpikesOnPlayerSide, actualSpikesOnPlayerSide);
    }

    @Test
    void oneSpikesAddedToBothSides() {
        String id = "Test7";
        String inputs = "SPIKES\nSPIKES\n";
        int numTurns = 2;

        int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         330, 823, 484, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedSpikesOnBotSide = 1;
        int actualSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getSpikeStacks();
        int expectedSpikesOnPlayerSide = 1;
        int actualSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getSpikeStacks();

        assertEquals(expectedSpikesOnBotSide, actualSpikesOnBotSide);
        assertEquals(expectedSpikesOnPlayerSide, actualSpikesOnPlayerSide);
    }

    // Spikes should not damage the opposing Pokemon already on the field, only those that switch in
    @Test
    void spikesDidNotDealDamageOnInitialUse() {
        String id = "Test8";
        String inputs = "SPIKES\nRAINDANCE\n";
        int numTurns = 2;

        int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

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
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp); // Check that the Spikes user didn't take damage either
    }

    @Test
    void oneSpikesDidDealDamageOnSwitchIn() {
        String id = "Test9";
        String inputs = "SPIKES\nRAINDANCE\nSPIKES\nSWITCH Spiritomb\n"; // Switches happen before any move is used, so the Spiritomb switching in will only land on one layer of spikes
        int numTurns = 3;

        int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // One layer of spikes does 1/8 max HP damage
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp); // Check that only the side with spikes on is being damaged
    }

    @Test
    void twoSpikesDidDealDamageOnSwitchIn() {
        String id = "Test10";
        String inputs = "SPIKES\nRAINDANCE\nSPIKES\nRAINDANCE\nSPIKES\nSWITCH Spiritomb\n";
        int numTurns = 4;

        int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // One layer of spikes does 1/6 max HP damage
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 6);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp);
    }

    @Test
    void threeSpikesDidDealDamageOnSwitchIn() {
        String id = "Test10";
        String inputs = "SPIKES\nRAINDANCE\nSPIKES\nRAINDANCE\nSPIKES\nRAINDANCE\nSPIKES\nSWITCH Spiritomb\n";
        int numTurns = 5;

        int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // One layer of spikes does 1/4 max HP damage
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 4);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp);
    }

    // Trying to place more than 3 layers of spikes should fail
    @Test
    void addMoreThanThreeSpikesFails() {
        String id = "Test11";
        String inputs = "SPIKES\nRAINDANCE\nSPIKES\nRAINDANCE\nSPIKES\nRAINDANCE\nSPIKES\nRAINDANCE\n"; // Attempts to place 4 layers but should end up with only 3
        int numTurns = 5;

        int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Player Pokemon is using spikes so test bot side
        int expectedSpikesOnBotSide = 3;
        int actualSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getSpikeStacks();

        assertEquals(expectedSpikesOnBotSide, actualSpikesOnBotSide);
    }

    @Test
    void flyingTypeSwitchInShouldNotTakeDamageFromSpikes() {
        String id = "Test12";
        String inputs = "SPIKES\nRAINDANCE\nSPIKES\nSWITCH Corviknight\n"; // Only testing one layer of spikes, but it should be the same for any number of layers
        int numTurns = 3;

        int[] pokemon = {473, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"SPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Flying type Pokemon should not taken any damage
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp();
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getMaxHp();
        int actualUserPokemonHp = BattleContextHolder.get().getPlayerActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
        assertEquals(expectedUserPokemonHp, actualUserPokemonHp); // Check that only the side with spikes on is being damaged
    }

    // Tests switching after a Pokemon faints from Spikes: use a move that lowers HP to the range that it will faint if it switches in again
    @Test
    void faintingToSpikesAllowsSwitchIn() {
        String id = "Test13";
        String inputs = "SPIKES\nCALMMIND\nICEPUNCH\nCALMMIND\nICEPUNCH\nSWITCH Corviknight\nSPIKES\nSWITCH Amoonguss\nSWITCH Corviknight\n"; // After fainting, prompt new Pokemon to send out
        int numTurns = 5;

        int[] pokemon = {473, 135, 468, 791, 230, 452,
                         591, 330, 484, 637, 468, 823};
        String[] moves = {"SPIKES", "ICEPUNCH", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "CALMMIND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Amoonguss in this case faints and the bot switches to Corviknight... the available party remaining that you can switch to is now 4 Pokemon
        int expectedOpposingPokemonRemaining = 4;
        int actualOpposingPokemonRemaining = BattleContextHolder.get().getBotPartyHandler().getAvailableParty().length;
        String expectedOpposingActivePokemon = "Corviknight";
        String actualOpposingActivePokemon = BattleContextHolder.get().getBotActivePokemon().getName();

        assertEquals(expectedOpposingPokemonRemaining, actualOpposingPokemonRemaining);
        assertEquals(expectedOpposingActivePokemon, actualOpposingActivePokemon);
    }
}
