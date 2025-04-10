package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddToxicSpikesToFoeSideTests {
    @Test
    void oneToxicSpikesAddedToPlayerSide() {
        String id = "Test1";
        String inputs = "BULKUP\nTOXICSPIKES\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {823, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 823 is Corviknight and 205 is Forretress)
                         205, 330, 484, 637, 591, 442}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", // Bulk Up in this case is a filler move that shouldn't affect toxic spikes
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; // Only fill out moves necessary for testing

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedToxicSpikesOnBotSide = 0;
        int actualToxicSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getToxicSpikeStacks();
        int expectedToxicSpikesOnPlayerSide = 1;
        int actualToxicSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getToxicSpikeStacks();

        assertEquals(expectedToxicSpikesOnBotSide, actualToxicSpikesOnBotSide);
        assertEquals(expectedToxicSpikesOnPlayerSide, actualToxicSpikesOnPlayerSide);
    }

    @Test
    void twoToxicSpikesAddedToPlayerSide() {
        String id = "Test2";
        String inputs = "BULKUP\nTOXICSPIKES\nBULKUP\nTOXICSPIKES\n"; // 2 turns: 4 total actions
        int numTurns = 3;

        int[] pokemon = {823, 135, 468, 791, 230, 452, 
                         205, 330, 484, 637, 591, 442};
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedToxicSpikesOnBotSide = 0;
        int actualToxicSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getToxicSpikeStacks();
        int expectedToxicSpikesOnPlayerSide = 2;
        int actualToxicSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getToxicSpikeStacks();

        assertEquals(expectedToxicSpikesOnBotSide, actualToxicSpikesOnBotSide);
        assertEquals(expectedToxicSpikesOnPlayerSide, actualToxicSpikesOnPlayerSide);
    }

    @Test
    void oneToxicSpikesAddedToBotSide() {
        String id = "Test3";
        String inputs = "TOXICSPIKES\nBULKUP\n";
        int numTurns = 2;

		int[] pokemon = {205, 135, 468, 791, 230, 452, 
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedToxicSpikesOnBotSide = 1;
        int actualToxicSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getToxicSpikeStacks();
        int expectedToxicSpikesOnPlayerSide = 0;
        int actualToxicSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getToxicSpikeStacks();

        assertEquals(expectedToxicSpikesOnBotSide, actualToxicSpikesOnBotSide);
        assertEquals(expectedToxicSpikesOnPlayerSide, actualToxicSpikesOnPlayerSide);
    }

    @Test
    void twoToxicSpikesAddedToBotSide() {
        String id = "Test4";
        String inputs = "TOXICSPIKES\nBULKUP\nTOXICSPIKES\nBULKUP\n";
        int numTurns = 3;

		int[] pokemon = {205, 135, 468, 791, 230, 452, 
                         823, 330, 484, 637, 591, 442};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedToxicSpikesOnBotSide = 2;
        int actualToxicSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getToxicSpikeStacks();
        int expectedToxicSpikesOnPlayerSide = 0;
        int actualToxicSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getToxicSpikeStacks();

        assertEquals(expectedToxicSpikesOnBotSide, actualToxicSpikesOnBotSide);
        assertEquals(expectedToxicSpikesOnPlayerSide, actualToxicSpikesOnPlayerSide);
    }

    @Test
    void oneToxicSpikesAddedToBothSides() {
        String id = "Test5";
        String inputs = "TOXICSPIKES\nTOXICSPIKES\n";
        int numTurns = 2;

        int[] pokemon = {205, 135, 468, 791, 230, 452, 
                         91, 823, 484, 637, 591, 442};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedToxicSpikesOnBotSide = 1;
        int actualToxicSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getToxicSpikeStacks();
        int expectedToxicSpikesOnPlayerSide = 1;
        int actualToxicSpikesOnPlayerSide = BattleContextHolder.get().getPlayerSideEffectsHandler().getToxicSpikeStacks();

        assertEquals(expectedToxicSpikesOnBotSide, actualToxicSpikesOnBotSide);
        assertEquals(expectedToxicSpikesOnPlayerSide, actualToxicSpikesOnPlayerSide);
    }

    // Toxic Spikes should not poison the opposing Pokemon already on the field, only those that switch in
    @Test
    void toxicSpikesDidNotPoisonOnInitialUse() {
        String id = "Test6";
        String inputs = "TOXICSPIKES\nRAINDANCE\n";
        int numTurns = 2;

        int[] pokemon = {205, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedOpposingPokemonStatus = "";
        String actualOpposingPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp();
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonStatus, actualOpposingPokemonStatus);
        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp);
    }

    @Test
    void oneToxicSpikesDidPoisonOnSwitchIn() {
        String id = "Test7";
        String inputs = "TOXICSPIKES\nRAINDANCE\nTOXICSPIKES\nSWITCH Spiritomb\n"; // Switches happen before any move is used, so the Spiritomb switching in will only land on one layer of toxic spikes
        int numTurns = 3;

        int[] pokemon = {205, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        String expectedOpposingPokemonStatus = "Poison";
        String actualOpposingPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonStatus, actualOpposingPokemonStatus);
        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp); // Check that the Pokemon also took poison damage
    }

    @Test
    void twoToxicSpikesDidBadlyPoisonOnSwitchIn() {
        String id = "Test8";
        String inputs = "TOXICSPIKES\nRAINDANCE\nTOXICSPIKES\nRAINDANCE\nTOXICSPIKES\nSWITCH Spiritomb\n";
        int numTurns = 4;

        int[] pokemon = {205, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // One layer of toxic spikes poisons for 1/8 max HP damage, but two layers badly poisons for 1/16 max HP damage (1st turn poisoned)
        String expectedOpposingPokemonStatus = "BadPoison";
        String actualOpposingPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();
        int expectedOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 16);
        int actualOpposingPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        assertEquals(expectedOpposingPokemonStatus, actualOpposingPokemonStatus);
        assertEquals(expectedOpposingPokemonHp, actualOpposingPokemonHp); // Check that the Pokemon also took poison damage
    }

    // Trying to place more than 2 layers of toxic spikes should fail
    @Test
    void addMoreThanTwoToxicSpikesFails() {
        String id = "Test9";
        String inputs = "TOXICSPIKES\nRAINDANCE\nTOXICSPIKES\nRAINDANCE\nTOXICSPIKES\nRAINDANCE\n"; // Attempts to place 3 layers but should end up with only 2
        int numTurns = 4;

        int[] pokemon = {205, 135, 468, 791, 230, 452, 
                         484, 330, 823, 637, 591, 442};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Player Pokemon is using spikes so test bot side
        int expectedToxicSpikesOnBotSide = 2;
        int actualToxicSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getToxicSpikeStacks();

        assertEquals(expectedToxicSpikesOnBotSide, actualToxicSpikesOnBotSide);
    }

    // Flying-type Pokemon are immune to ground hazards like Toxic Spikes
    @Test
    void flyingTypeSwitchInShouldNotBePoisonedFromToxicSpikes() {
        String id = "Test10";
        String inputs = "TOXICSPIKES\nRAINDANCE\nTOXICSPIKES\nSWITCH Togekiss\n"; // Only testing one layer of toxic spikes, but it should be the same for any number of layers
        int numTurns = 3;

        int[] pokemon = {205, 135, 823, 791, 230, 452, 
                         484, 330, 823, 637, 591, 468};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Flying-type Pokemon should not be poisoned
        String expectedOpposingPokemonStatus = "";
        String actualOpposingPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        assertEquals(expectedOpposingPokemonStatus, actualOpposingPokemonStatus);
    }

    @Test
    void steelTypeSwitchInShouldNotBePoisonedFromToxicSpikes() {
        String id = "Test11";
        String inputs = "TOXICSPIKES\nRAINDANCE\nTOXICSPIKES\nSWITCH Lucario\n"; // Only testing one layer of spikes, but it should be the same for any number of layers
        int numTurns = 3;

        int[] pokemon = {205, 135, 823, 791, 230, 452, 
                         484, 330, 823, 637, 591, 448};
        String[] moves = {"TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Steel-type Pokemon should not be poisoned
        String expectedOpposingPokemonStatus = "";
        String actualOpposingPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();

        assertEquals(expectedOpposingPokemonStatus, actualOpposingPokemonStatus);
    }

    @Test
    void poisonTypeSwitchInShouldRemoveAllToxicSpikes() {
        String id = "Test12";
        String inputs = "TOXICSPIKES\nRAINDANCE\nTOXICSPIKES\nRAINDANCE\nCURSE\nSWITCH Muk\n"; // Use a different move at the end to avoid placing another layer of toxic spikes
        int numTurns = 4;

        int[] pokemon = {205, 135, 823, 791, 230, 452, 
                         484, 330, 823, 637, 591, 89};
        String[] moves = {"TOXICSPIKES", "CURSE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Poison-type Pokemon should not be poisoned and should remove all layers of toxic spikes
        String expectedOpposingPokemonStatus = "";
        String actualOpposingPokemonStatus = BattleContextHolder.get().getBotActivePokemon().getStatus();
        int expectedToxicSpikesOnBotSide = 0;
        int actualToxicSpikesOnBotSide = BattleContextHolder.get().getBotSideEffectsHandler().getToxicSpikeStacks();

        assertEquals(expectedOpposingPokemonStatus, actualOpposingPokemonStatus);
        assertEquals(expectedToxicSpikesOnBotSide, actualToxicSpikesOnBotSide);
    }
}
