package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CriticalHitTests {
    @Test
    void singleHitMoveAlwaysCriticalHit() {
        String id = "Test1";
        String inputs = "WICKEDBLOW\nTOXICSPIKES\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {892, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 892 is Urshifu and 205 is Forretress)
                         205, 330, 484, 637, 591, 442}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"WICKEDBLOW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", // Toxic spikes in this case is a filler move that shouldn't affect damage
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; // Only fill out moves necessary for testing

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Damage amounts are tested without RNG, meaning that a wicked blow will always deal the same amount of damage (all other factors equal)
        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 78; // Expected highest damage roll according to Showdown damage calc
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void multiHitMoveAlwaysCriticalHit() {
        String id = "Test2";
        String inputs = "SURGINGSTRIKES\nTOXICSPIKES\n";
        int numTurns = 2;

        int[] pokemon = {892, 135, 468, 791, 230, 452, // Tested using single-strike Urshifu as forms aren't implemented
                         205, 330, 484, 637, 591, 442}; 
        String[] moves = {"SURGINGSTRIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 54;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    // Critical hits will assume the user's attack stat state is 0 if it is less than 0 (ignores attack drops)
    @Test
    void criticalHitsIgnoreUserStatDrops() {
        String id = "Test3";
        String inputs = "STORMTHROW\nCHARM\n";
        int numTurns = 2;

        int[] pokemon = {538, 135, 205, 791, 230, 452, // Throh vs Togekiss
                         468, 330, 484, 637, 591, 442}; 
        String[] moves = {"STORMTHROW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "CHARM", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Togekiss moves first and lowers Throh's attack, but it should deal the same damage ignoring the stat drop
        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 16;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedPlayerPokemonAttackStat = -2;
        int actualPlayerPokemonAttackStat = BattleContextHolder.get().getPlayerActivePokemon().getAttackStage();

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
        assertEquals(expectedPlayerPokemonAttackStat, actualPlayerPokemonAttackStat);
    }

    // Critical hits stack with attack raises on the user
    @Test
    void criticalHitsDoNotIgnoreUserStatRaises() {
        String id = "Test4";
        String inputs = "BULKUP\nRAINDANCE\nSTORMTHROW\nRAINDANCE\n";
        int numTurns = 3;

        int[] pokemon = {538, 135, 205, 791, 230, 452,
                         468, 330, 484, 637, 591, 442}; 
        String[] moves = {"STORMTHROW", "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "RAINDANCE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Throh raises attack before using critical hit move and should deal more damage as a result
        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 24;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedPlayerPokemonAttackStat = 1;
        int actualPlayerPokemonAttackStat = BattleContextHolder.get().getPlayerActivePokemon().getAttackStage();

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
        assertEquals(expectedPlayerPokemonAttackStat, actualPlayerPokemonAttackStat);
    }
    
    // Critical hits will assume the target's defense stat state is 0 if it is greater than 0 (ignores defense raises)
    @Test
    void criticalHitsIgnoreTargetStatRaises() {
        String id = "Test5";
        String inputs = "STORMTHROW\nBULKUP\n";
        int numTurns = 2;

        int[] pokemon = {538, 135, 205, 791, 230, 452,
                         823, 330, 484, 637, 591, 442}; 
        String[] moves = {"STORMTHROW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "BULKUP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Corviknight raises defense before Throh's move, but it should deal the same damage ignoring the stat raise
        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 60;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedBotPokemonDefenseStat = 1;
        int actualBotPokemonDefenseStat = BattleContextHolder.get().getBotActivePokemon().getDefenseStage();

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
        assertEquals(expectedBotPokemonDefenseStat, actualBotPokemonDefenseStat);
    }

    // Critical hits stack with defense drops on the target
    @Test
    void criticalHitsDoNotIgnoreTargetStatDrops() {
        String id = "Test6";
        String inputs = "STORMTHROW\nCLOSECOMBAT\n";
        int numTurns = 2;

        int[] pokemon = {538, 135, 205, 791, 230, 452,
                         648, 330, 484, 637, 591, 442}; 
        String[] moves = {"STORMTHROW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "CLOSECOMBAT", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Meloetta goes first and drops its defense using Close Combat, and the next critical hit should deal more damage as a result
        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 114;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
        int expectedBotPokemonDefenseStat = -1;
        int actualBotPokemonDefenseStat = BattleContextHolder.get().getBotActivePokemon().getDefenseStage();

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
        assertEquals(expectedBotPokemonDefenseStat, actualBotPokemonDefenseStat);
    }

    @Test
    void criticalHitsAreFiftyPercentStronger() {
        String id = "Test7";
        String inputs = "STORMTHROW\nSUNNYDAY\n";
        int numTurns = 2;

        int[] pokemon = {538, 135, 205, 791, 230, 452,
                         330, 468, 484, 637, 591, 442}; 
        String[] moves = {"STORMTHROW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "SUNNYDAY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        
        // Battle 1 - Critical hit is allowed
        BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int botPokemonDamageFromCrit = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        // Battle 2 - Critical hit isn't allowed
        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int botPokemonDamageFromNoCrit = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
		
        int expectedDamageIfCrit = (int) Math.floor(botPokemonDamageFromNoCrit * 1.5); // Because critical hit multiplier is applied before everything is rounded, the integer amount returned * 1.5 may not be completely accurate but is true for the purposes of this test
        int actualDamageIfCrit = botPokemonDamageFromCrit;
  
        assertEquals(expectedDamageIfCrit, actualDamageIfCrit);
    }

    @Test
    void criticalHitsAreAffectedByBurn() {
        String id = "Test8";
        String inputs = "STORMTHROW\nWILLOWISP\n";
        int numTurns = 2;

        int[] pokemon = {538, 135, 205, 791, 230, 452,
                         59, 468, 484, 637, 591, 442}; 
        String[] moves = {"STORMTHROW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        
        // Battle 1 - Burn is applied
        BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int actualDamageFromBurnedCrit = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        // Battle 2 - Burn isn't applied
        inputs = "STORMTHROW\nSUNNYDAY\n"; // Remove burning move
        moves[24] = "SUNNYDAY";

        BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int actualDamageFromNonBurnedCrit = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - BattleContextHolder.get().getBotActivePokemon().getCurrentHp();
		
        int expectedDamageFromBurnedCrit = 36; // Roughly half from the 0.5x burn multiplier (slight expected rounding discrepancy)
        int expectedDamageFromNonBurnedCrit = 73;
  
        assertEquals(expectedDamageFromBurnedCrit, actualDamageFromBurnedCrit);
        assertEquals(expectedDamageFromNonBurnedCrit, actualDamageFromNonBurnedCrit);
    }

    @Test
    void normalMoveCriticalHitsOnSeed() {
        String id = "Test9";
        String inputs = "DRAGONCLAW\nTOXICSPIKES\n";
        int numTurns = 2;

        int[] pokemon = {487, 135, 468, 892, 230, 452,
                         205, 330, 484, 637, 591, 442}; 
        String[] moves = {"DRAGONCLAW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(0); // Seed that will always cause Dragon Claw to crit

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 31;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void normalMoveDoesNotCriticalHitOnSeed() {
        String id = "Test10";
        String inputs = "DRAGONCLAW\nTOXICSPIKES\n";
        int numTurns = 2;

        int[] pokemon = {487, 135, 468, 892, 230, 452,
                         205, 330, 484, 637, 591, 442}; 
        String[] moves = {"DRAGONCLAW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(1); // Seed that will always cause Dragon Claw to not crit

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 21;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void highCritChanceMoveCriticalHitsOnSeed() {
        String id = "Test11";
        String inputs = "SHADOWCLAW\nTOXICSPIKES\n";
        int numTurns = 2;

        int[] pokemon = {487, 135, 468, 892, 230, 452,
                         205, 330, 484, 637, 591, 442}; 
        String[] moves = {"SHADOWCLAW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(4100);

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 55;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }

    @Test
    void highCritChanceMoveDoesNotCriticalHitOnSeed() {
        String id = "Test11";
        String inputs = "SHADOWCLAW\nTOXICSPIKES\n";
        int numTurns = 2;

        int[] pokemon = {487, 135, 468, 892, 230, 452,
                         205, 330, 484, 637, 591, 442}; 
        String[] moves = {"SHADOWCLAW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "TOXICSPIKES", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        EffectChanceRandomizer.setTestSeed(5632); // All seeds from sample range 4100-5631 result in crits 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves, true);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        int expectedBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getMaxHp() - 37;
        int actualBotPokemonHp = BattleContextHolder.get().getBotActivePokemon().getCurrentHp();

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedBotPokemonHp, actualBotPokemonHp);
    }
}
