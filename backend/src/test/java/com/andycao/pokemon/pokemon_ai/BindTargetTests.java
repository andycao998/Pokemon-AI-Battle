package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BindTargetTests {
    @Test
    void playerPokemonCanBeBound() {
        String id = "Test1";
        String inputs = "BULKUP\nINFESTATION\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {823, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 823 is Corviknight and 887 is Dragapult)
                         887, 330, 484, 637, 591, 442}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", // Filler move that shouldn't interfere with Infestation
                          "INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedPlayerPokemonBound = true;
        boolean actualPlayerPokemonBound = BattleContextHolder.get().getPlayerActivePokemon().getBoundTurns() > 0;
        boolean expectedBotPokemonBound = false;
        boolean actualBotPokemonBound = BattleContextHolder.get().getBotActivePokemon().getBoundTurns() > 0;

        assertEquals(expectedPlayerPokemonBound, actualPlayerPokemonBound);
        assertEquals(expectedBotPokemonBound, actualBotPokemonBound);
    }

    @Test
    void botPokemonCanBeBound() {
        String id = "Test2";
        String inputs = "INFESTATION\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedPlayerPokemonBound = false;
        boolean actualPlayerPokemonBound = BattleContextHolder.get().getPlayerActivePokemon().getBoundTurns() > 0;
        boolean expectedBotPokemonBound = true;
        boolean actualBotPokemonBound = BattleContextHolder.get().getBotActivePokemon().getBoundTurns() > 0;

        assertEquals(expectedPlayerPokemonBound, actualPlayerPokemonBound);
        assertEquals(expectedBotPokemonBound, actualBotPokemonBound);
    }

    @Test
    void bindDamageIsBasedOnMaxHp() {
        String id = "Test3";
        String inputs = "INFESTATION\nROOST\n"; // Roost to heal from move damage but still take damage from Infestation bound effect
        int numTurns = 2;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "ROOST", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBindDamage = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8);
        int actualBindDamage = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        assertEquals(expectedBindDamage, actualBindDamage);
    }

    @Test
    void bindDamageIsConsistent() {
        String id = "Test4";
        String inputs = "INFESTATION\nROOST\nDRAGONDANCE\nBULKUP\n"; // Filler moves on turn 2 to see Infestation damage
        int numTurns = 3;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "DRAGONDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "ROOST", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBindDamage = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (2 * (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8));
        int actualBindDamage = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        assertEquals(expectedBindDamage, actualBindDamage);
    }

    @Test
    void bindLastsAtLeastThreeTurns() {
        String id = "Test5";
        String inputs = "INFESTATION\nROOST\nDRAGONDANCE\nBULKUP\nDRAGONDANCE\nBULKUP\n";
        int numTurns = 4;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "DRAGONDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "ROOST", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedBotPokemonBound = true;
        boolean actualBotPokemonBound = BattleContextHolder.get().getBotActivePokemon().getBoundTurns() > 0;

        assertEquals(expectedBotPokemonBound, actualBotPokemonBound);
    }

    @Test
    void bindCanEndAfterFourTurns() {
        String id = "Test5";
        String inputs = "INFESTATION\nROOST\nDRAGONDANCE\nBULKUP\nDRAGONDANCE\nBULKUP\nDRAGONDANCE\nBULKUP\n";
        int numTurns = 5;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "DRAGONDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "ROOST", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(4100);
        
		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedBotPokemonBound = false;
        boolean actualBotPokemonBound = BattleContextHolder.get().getBotActivePokemon().getBoundTurns() > 0;

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonBound, actualBotPokemonBound);
    }

    @Test
    void bindCanLastFourTurns() {
        String id = "Test6";
        String inputs = "INFESTATION\nROOST\nDRAGONDANCE\nBULKUP\nDRAGONDANCE\nBULKUP\nDRAGONDANCE\nBULKUP\n";
        int numTurns = 5;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "DRAGONDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "ROOST", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0);
        
		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedBotPokemonBound = true;
        boolean actualBotPokemonBound = BattleContextHolder.get().getBotActivePokemon().getBoundTurns() > 0;

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonBound, actualBotPokemonBound);
    }

    @Test
    void bindEndsAfterFiveTurns() {
        String id = "Test5";
        String inputs = "INFESTATION\nROOST\nDRAGONDANCE\nBULKUP\nDRAGONDANCE\nBULKUP\nDRAGONDANCE\nBULKUP\nDRAGONDANCE\nBULKUP\n";
        int numTurns = 6;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "DRAGONDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "ROOST", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(4100);
        
		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedBotPokemonBound = false;
        boolean actualBotPokemonBound = BattleContextHolder.get().getBotActivePokemon().getBoundTurns() > 0;

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonBound, actualBotPokemonBound);
    }

    @Test
    void pokemonCannotSwitchWhileBound() {
        String id = "Test6";
        String inputs = "INFESTATION\nROOST\n";
        int numTurns = 2;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "ROOST", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedCanSwitch = false;
        boolean actualCanSwitch = BattleContextHolder.get().getBotActivePokemon().getCanSwitch();

        assertEquals(expectedCanSwitch, actualCanSwitch);
    }

    @Test
    void pokemonCanSwitchWithSwitchingMovesWhileBound() {
        String id = "Test7";
        String inputs = "INFESTATION\nUTURN Flygon\n";
        int numTurns = 2;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "UTURN", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Dragapult goes first to set Infestation on Corviknight, but is able to switch using U-Turn
        String expectedBotPokemon = "Flygon";
        String actualBotPokemon = BattleContextHolder.get().getBotActivePokemon().getName();

        assertEquals(expectedBotPokemon, actualBotPokemon);
    }

    @Test
    void bindRemovedAfterSwitching() {
        String id = "Test8";
        String inputs = "INFESTATION\nUTURN Flygon\nDRAGONDANCE\nSWITCH Corviknight\n";
        int numTurns = 3;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "DRAGONDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "UTURN", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Corviknight, which was hit with Infestation, switches back in and should be able to switch like normal
        String expectedBotPokemon = "Corviknight";
        String actualBotPokemon = BattleContextHolder.get().getBotActivePokemon().getName();
        boolean expectedCanSwitch = true;
        boolean actualCanSwitch = BattleContextHolder.get().getBotActivePokemon().getCanSwitch();

        assertEquals(expectedBotPokemon, actualBotPokemon);
        assertEquals(expectedCanSwitch, actualCanSwitch);
    }

    @Test
    void bindRemovedAfterBinderFaints() {
        String id = "Test9";
        String inputs = "INFESTATION\nMOONGEISTBEAM\n";
        int numTurns = 2;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         792, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "MOONGEISTBEAM", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Lunala is hit with Infestation but then KOs Dragapult to free itself
        boolean expectedUserFainted = true;
        boolean actualUserFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");
        boolean expectedCanSwitch = true;
        boolean actualCanSwitch = BattleContextHolder.get().getBotActivePokemon().getCanSwitch();

        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedCanSwitch, actualCanSwitch);
    }

    @Test
    void bindRemovedAfterBinderSwitches() {
        String id = "Test10";
        String inputs = "INFESTATION\nROOST\nSWITCH\nJolteon\nCALMMIND\n";
        int numTurns = 3;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         792, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "ROOST", "CALMMIND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Lunala is hit with Infestation but it is removed after Dragapult switches out, taking only one instance of damage
        int expectedBindDamage = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8);
        int actualBindDamage = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        boolean expectedCanSwitch = true;
        boolean actualCanSwitch = BattleContextHolder.get().getBotActivePokemon().getCanSwitch();

        assertEquals(expectedBindDamage, actualBindDamage);
        assertEquals(expectedCanSwitch, actualCanSwitch);
    }

    @Test
    void bindRemovedAfterRapidSpin() {
        String id = "Test11";
        String inputs = "INFESTATION\nRAPIDSPIN\n";
        int numTurns = 2;

        int[] pokemon = {314, 135, 468, 791, 230, 452,
                         205, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "RAPIDSPIN", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Forretress is hit with Infestation but it is instantly removed with Rapid Spin
        int expectedBindDamage = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 9; // Accounting for Infestation hitting for a little damage
        int actualBindDamage = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        boolean expectedCanSwitch = true;
        boolean actualCanSwitch = BattleContextHolder.get().getBotActivePokemon().getCanSwitch();

        assertEquals(expectedBindDamage, actualBindDamage);
        assertEquals(expectedCanSwitch, actualCanSwitch);
    }

    @Test
    void bindRemovedAfterSubstitute() {
        String id = "Test11";
        String inputs = "INFESTATION\nSUBSTITUTE\n";
        int numTurns = 2;

        int[] pokemon = {314, 135, 468, 791, 230, 452,
                         205, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "SUBSTITUTE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Forretress is hit with Infestation but it is instantly removed with Rapid Spin
        int expectedBindDamage = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 4) - 9; // Accounting for Infestation hitting for a little damage and damage cost of using Substitute
        int actualBindDamage = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        boolean expectedCanSwitch = true;
        boolean actualCanSwitch = BattleContextHolder.get().getBotActivePokemon().getCanSwitch();

        assertEquals(expectedBindDamage, actualBindDamage);
        assertEquals(expectedCanSwitch, actualCanSwitch);
    }

    @Test
    void bindEffectCannotBeRefreshedWhileActive() {
        String id = "Test12";
        String inputs = "INFESTATION\nROOST\nINFESTATION\nBULKUP\nINFESTATION\nBULKUP\nINFESTATION\nBULKUP\n";
        int numTurns = 5;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "DRAGONDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "BULKUP", "ROOST", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(4100);
        
		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Despite using Infestation every turn, Corviknight moves last and is Infestation-free at the end of its turn because the duration was never reset
        boolean expectedCanSwitch = true;
        boolean actualCanSwitch = BattleContextHolder.get().getBotActivePokemon().getCanSwitch();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedCanSwitch, actualCanSwitch);
    }

    @Test
    void ghostTypesCanSwitchWhileBound() {
        String id = "Test13";
        String inputs = "INFESTATION\nSUNNYDAY\nINFESTATION\nSWITCH Flygon";
        int numTurns = 3;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         709, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "SUNNYDAY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Dragapult sets Infestation on Trevenant, but it is able to switch because it is a Ghost-type
        String expectedBotPokemon = "Flygon";
        String actualBotPokemon = BattleContextHolder.get().getBotActivePokemon().getName();

        assertEquals(expectedBotPokemon, actualBotPokemon);
    }

    @Test
    void ghostTypesAreStillHurtByBind() {
        String id = "Test14";
        String inputs = "INFESTATION\nSUNNYDAY\n";
        int numTurns = 2;

        int[] pokemon = {887, 135, 468, 791, 230, 452,
                         709, 330, 484, 637, 591, 442}; 
        String[] moves = {"INFESTATION", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                          "SUNNYDAY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Trevenant still takes normal binding damage and damage from the attack itself
        int expectedBindDamage = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - (int) Math.floor(BattleContextHolder.get().getBotActivePokemon().getMaxHp() / 8) - 12; 
        int actualBindDamage = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        assertEquals(expectedBindDamage, actualBindDamage);
    }
}
