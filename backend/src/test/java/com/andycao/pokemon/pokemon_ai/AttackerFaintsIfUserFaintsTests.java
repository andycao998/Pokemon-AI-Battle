package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AttackerFaintsIfUserFaintsTests {
    @Test
    void destinyBondWorksForPlayerPokemon() {
        String id = "Test1";
        String inputs = "DESTINYBOND\nSHADOWBALL\n"; // Each turn is made up of two choices: the player action and the bot action deliminated by new lines
        int numTurns = 2; // This test only needs to run for one turn --> check at the beginning of the second turn

        int[] pokemon = {94, 135, 468, 791, 230, 452, // Represent Pokedex numbers for the Pokemon (e.g. 94 is Gengar and 487 is Giratina)
                         487, 330, 484, 637, 591, 442}; // 0-5 (first 6) are the player's Pokemon; remaining 6 are the bot's Pokemon
        String[] moves = {"DESTINYBOND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "SHADOWBALL", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; // Filler move to cause Gengar to faint

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); // Let battle initialize first

        // Specify battle to run tests on
        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        // Only apply tests after desired battle state is reached
        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedUserFainted = true;
        boolean actualUserFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");
        boolean expectedAttackerFainted = true;
        boolean actualAttackerFainted = BattleContextHolder.get().getBotActivePokemon().getStatus().equals("Fainted");

        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedAttackerFainted, actualAttackerFainted);
    }

    @Test
    void destinyBondWorksForBotPokemon() {
        String id = "Test2";
        String inputs = "SHADOWBALL\nDESTINYBOND\n"; 
        int numTurns = 2;

        int[] pokemon = {487, 135, 468, 791, 230, 452,
                         94, 330, 484, 637, 591, 442}; 
        String[] moves = {"SHADOWBALL", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "DESTINYBOND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        boolean expectedUserFainted = true;
        boolean actualUserFainted = BattleContextHolder.get().getBotActivePokemon().getStatus().equals("Fainted");
        boolean expectedAttackerFainted = true;
        boolean actualAttackerFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");

        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedAttackerFainted, actualAttackerFainted);
    }

    @Test
    void switchSequenceWorksAfterDestinyBondFaints() {
        String id = "Test3";
        String inputs = "SHADOWBALL\nDESTINYBOND\nSWITCH Jolteon\nSWITCH Palkia\n"; 
        int numTurns = 2;

        int[] pokemon = {487, 135, 468, 791, 230, 452,
                         94, 330, 484, 637, 591, 442}; 
        String[] moves = {"SHADOWBALL", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "DESTINYBOND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id);

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Switched to another Pokemon after fainting
        boolean expectedUserFainted = false;
        boolean actualUserFainted = BattleContextHolder.get().getBotActivePokemon().getStatus().equals("Fainted");
        boolean expectedAttackerFainted = false;
        boolean actualAttackerFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");

        String expectedPlayerPokemon = "Jolteon";
        String actualPlayerPokemon = BattleContextHolder.get().getPlayerActivePokemon().getName();
        String expectedBotPokemon = "Palkia";
        String actualBotPokemon = BattleContextHolder.get().getBotActivePokemon().getName();

        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedAttackerFainted, actualAttackerFainted);
        assertEquals(expectedPlayerPokemon, actualPlayerPokemon);
        assertEquals(expectedBotPokemon, actualBotPokemon);
    }

    @Test
    void destinyBondFailsIfAlreadyActive() {
        String id = "Test4";
        String inputs = "DESTINYBOND\nDRAGONCLAW\nDESTINYBOND\nDRAGONCLAW\n";
        int numTurns = 3;

        int[] pokemon = {94, 135, 468, 791, 230, 452, 
                         487, 330, 484, 637, 591, 442}; 
        String[] moves = {"DESTINYBOND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "DRAGONCLAW", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); 

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Pokemon uses Destiny Bond --> Attacker doesn't faint user --> User uses Destiny Bond again, but is already active --> Move fails and Destiny Bond wears off after turn
        boolean expectedUserMoveFailed = true;
        boolean actualUserMoveFailed = BattleContextHolder.get().getActionHandler().getPokemonCurrentMoveFailed(BattleContextHolder.get().getPlayerActivePokemon());
        boolean expectedUserDestinyBondActive = false;
        boolean actualUserDestinyBondActive = BattleContextHolder.get().getPlayerActivePokemon().getDestinyBond();

        assertEquals(expectedUserMoveFailed, actualUserMoveFailed);
        assertEquals(expectedUserDestinyBondActive, actualUserDestinyBondActive);
    }

    @Test
    void destinyBondLastsUntilEndOfNextTurn() {
        String id = "Test5";
        String inputs = "DESTINYBOND\nSHADOWBALL\nDESTINYBOND\nSHADOWBALL\n";
        int numTurns = 3;

        int[] pokemon = {110, 135, 468, 791, 230, 452, 
                         487, 330, 484, 637, 591, 442}; 
        String[] moves = {"DESTINYBOND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "SHADOWBALL", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); 

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Faster Pokemon uses a move that doesn't faint slower Pokemon --> Slower Pokemon uses Destiny Bond --> Faster Pokemon goes again and faints user and effect should still be active
        boolean expectedUserFainted = true;
        boolean actualUserFainted = BattleContextHolder.get().getBotActivePokemon().getStatus().equals("Fainted");
        boolean expectedAttackerFainted = true;
        boolean actualAttackerFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");

        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedAttackerFainted, actualAttackerFainted);
    }

    @Test
    void destinyBondWearsOffAfterEndOfNextTurn() {
        String id = "Test6";
        String inputs = "DESTINYBOND\nHEX\nDESTINYBOND\nHEX\nDESTINYBOND\nHEX\n"; // Slower Pokemon attempts to use Destiny Bond again but never gets to because it is fainted
        int numTurns = 4;

        int[] pokemon = {110, 135, 468, 791, 230, 452, 
                         487, 330, 484, 637, 591, 442}; 
        String[] moves = {"DESTINYBOND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "HEX", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); 

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Faster Pokemon uses a move that doesn't faint slower Pokemon --> Slower Pokemon uses Destiny Bond --> Faster Pokemon goes again and faints user and effect should still be active
        boolean expectedUserFainted = true;
        boolean actualUserFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");
        boolean expectedAttackerFainted = false;
        boolean actualAttackerFainted = BattleContextHolder.get().getBotActivePokemon().getStatus().equals("Fainted");

        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedAttackerFainted, actualAttackerFainted);
    }

    @Test
    void destinyBondWearsOffEvenWhenUnableToAct() {
        String id = "Test7";
        String inputs = "DESTINYBOND\nSPORE\nDESTINYBOND\nSPORE\n"; // Slower Pokemon attempts to use Destiny Bond again but never gets to because it is fainted
        int numTurns = 3;

        int[] pokemon = {110, 135, 468, 791, 230, 452, 
                         591, 330, 484, 637, 591, 442}; 
        String[] moves = {"DESTINYBOND", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "SPORE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); 

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Pokemon uses Destiny Bond --> Attacker puts user to sleep --> User cannot act next turn and Destiny Bond wears off
        boolean expectedUserDestinyBondActive = false;
        boolean actualUserDestinyBondActive = BattleContextHolder.get().getPlayerActivePokemon().getDestinyBond();

        assertEquals(expectedUserDestinyBondActive, actualUserDestinyBondActive);
    }

    @Test
    void destinyBondFailsIfUserFaintsToWeather() {
        String id = "Test8";
        String inputs = "SUNNYDAY\nPSYCHIC\nDESTINYBOND\nSANDSTORM\n"; // Psychic doesn't cause fainting but brings Destiny Bond user low enough to faint to weather
        int numTurns = 3;

        int[] pokemon = {110, 135, 468, 791, 230, 452, 
                         437, 330, 484, 637, 591, 442}; 
        String[] moves = {"DESTINYBOND", "SUNNYDAY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "PSYCHIC", "SANDSTORM", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

		BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); 

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Pokemon uses Destiny Bond before expecting to faint to weather
        boolean expectedUserDestinyBondActive = true;
        boolean actualUserDestinyBondActive = BattleContextHolder.get().getPlayerActivePokemon().getDestinyBond();
        boolean expectedUserFainted = true;
        boolean actualUserFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");
        boolean expectedAttackerFainted = false;
        boolean actualAttackerFainted = BattleContextHolder.get().getBotActivePokemon().getStatus().equals("Fainted");

        assertEquals(expectedUserDestinyBondActive, actualUserDestinyBondActive);
        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedAttackerFainted, actualAttackerFainted);
    }

    @Test
    void destinyBondFailsIfUserFaintsToStatus() {
        String id = "Test9";
        String inputs = "SUNNYDAY\nPSYCHIC\nDESTINYBOND\nWILLOWISP\n"; // Psychic doesn't cause fainting but brings Destiny Bond user low enough to faint to burn
        int numTurns = 3;

        int[] pokemon = {110, 135, 468, 791, 230, 452, 
                         437, 330, 484, 637, 591, 442}; 
        String[] moves = {"DESTINYBOND", "SUNNYDAY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "PSYCHIC", "WILLOWISP", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 

        EffectChanceRandomizer.setTestSeed(0); // Seeded to guarantee Will-O-Wisp hits
        
        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); 

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Pokemon uses Destiny Bond before expecting to faint to weather
        boolean expectedUserDestinyBondActive = true;
        boolean actualUserDestinyBondActive = BattleContextHolder.get().getPlayerActivePokemon().getDestinyBond();
        boolean expectedUserFainted = true;
        boolean actualUserFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");
        boolean expectedAttackerFainted = false;
        boolean actualAttackerFainted = BattleContextHolder.get().getBotActivePokemon().getStatus().equals("Fainted");

        EffectChanceRandomizer.setTestSeed(-1);

        assertEquals(expectedUserDestinyBondActive, actualUserDestinyBondActive);
        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedAttackerFainted, actualAttackerFainted);
    }

    @Test
    void destinyBondWorksIfTurnOrderIsChanged() {
        String id = "Test10";
        String inputs = "DESTINYBOND\nTRICKROOM\nDESTINYBOND\nPSYCHIC\n"; // Original turn order: Destiny Bond --> Trick Room... After: Psychic --> Destiny Bond
        int numTurns = 3;

        int[] pokemon = {110, 135, 468, 791, 230, 452, 
                         80, 330, 484, 637, 591, 442}; 
        String[] moves = {"DESTINYBOND", "SUNNYDAY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                          "PSYCHIC", "TRICKROOM", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; 
        
        BattleTests.generateTestBattle(id, inputs, pokemon, moves);

        BattleTests.waitUntilBattleReady(id); 

        Battle context = BattleContextHolder.getSessionById(id);
        BattleContextHolder.set(context, id);

        while (BattleContextHolder.get().getTurnHandler().getTurn() != numTurns) {
            BattleManager.getInstance().wait(500);
        }

        // Faster Pokemon uses Destiny Bond before turn order is reversed by Trick Room --> Slower Pokemon now faints user before Destiny Bond wears off and both faint as a result
        boolean expectedUserFainted = true;
        boolean actualUserFainted = BattleContextHolder.get().getPlayerActivePokemon().getStatus().equals("Fainted");
        boolean expectedAttackerFainted = true;
        boolean actualAttackerFainted = BattleContextHolder.get().getBotActivePokemon().getStatus().equals("Fainted");

        assertEquals(expectedUserFainted, actualUserFainted);
        assertEquals(expectedAttackerFainted, actualAttackerFainted);
    }
}