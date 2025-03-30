package com.andycao.pokemon.pokemon_ai;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidStatException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

class BattleTests {
    static Pokemon[] generateParties(int[] pokemon, String[] moves) {
        try {
            Pokemon[] parties = new Pokemon[12];

            for (int i = 0; i < pokemon.length; i++) {
                int dexNumber = pokemon[i];
                Pokemon p = new Pokemon(dexNumber);

                String move1 = moves[4 * i];
                String move2 = moves[4 * i + 1];
                String move3 = moves[4 * i + 2];
                String move4 = moves[4 * i + 3];
                String[] moveset = {move1, move2, move3, move4};

                p.setMoves(moveset);
                parties[i] = p;
            }

            // Pokemon jolteon = new Pokemon(135);
            // Pokemon togekiss = new Pokemon(468);
            // Pokemon solgaleo = new Pokemon(791);
            // Pokemon mamoswine = new Pokemon(473);
            // Pokemon kingdra = new Pokemon(230);
            // Pokemon drapion = new Pokemon(452);

            // Pokemon flygon = new Pokemon(330);
            // Pokemon corviknight = new Pokemon(823);
            // Pokemon palkia = new Pokemon(484);
            // Pokemon volcarona = new Pokemon(637);
            // Pokemon amoonguss = new Pokemon(591);
            // Pokemon spiritomb = new Pokemon(442);

            // String[] moveset1 = {"THUNDERBOLT", "HYPERVOICE", "SHADOWBALL", "VOLTSWITCH"};
            // jolteon.setMoves(moveset1);
            // String[] moveset2 = {"AURASPHERE", "AIRSLASH", "DAZZLINGGLEAM", "NASTYPLOT"};
            // togekiss.setMoves(moveset2);
            // String[] moveset3 = {"SUNSTEELSTRIKE", "FLAREBLITZ", "PSYCHICFANGS", "CLOSECOMBAT"};
            // solgaleo.setMoves(moveset3);
            // String[] moveset4 = {"SPIKES"};
            // mamoswine.setMoves(moveset4);
            // String[] moveset5 = {"DRACOMETEOR", "FLIPTURN", "HURRICANE", "HYDROPUMP"};
            // kingdra.setMoves(moveset5);
            // String[] moveset6 = {"SWORDSDANCE", "KNOCKOFF", "POISONJAB", "EARTHQUAKE"};
            // drapion.setMoves(moveset6);

            // String[] moveset7 = {"EARTHQUAKE", "OUTRAGE", "FIREPUNCH", "UTURN"};
            // flygon.setMoves(moveset7);
            // String[] moveset8 = {"BODYPRESS", "BRAVEBIRD", "IRONHEAD", "BULKUP"};
            // corviknight.setMoves(moveset8);
            // String[] moveset9 = {"DRACOMETEOR", "HYDROPUMP", "ICEBEAM", "THUNDERBOLT"};
            // palkia.setMoves(moveset9);
            // String[] moveset10 = {"QUIVERDANCE", "BUGBUZZ", "FIREBLAST", "ROOST"};
            // volcarona.setMoves(moveset10);
            // String[] moveset11 = {"SPORE", "SLUDGEBOMB", "GIGADRAIN", "TOXIC"};
            // amoonguss.setMoves(moveset11);
            // String[] moveset12 = {"SHADOWBALL", "SUCKERPUNCH", "FOULPLAY", "WILLOWISP"};
            // spiritomb.setMoves(moveset12);

            // Pokemon[] parties = {mamoswine, jolteon, togekiss, solgaleo, kingdra, drapion, corviknight, flygon, palkia, volcarona, amoonguss, spiritomb};
            return parties;
        } 
        // WIP: create more specific exceptions
        catch (InvalidStatException | InvalidIdentifierException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void generateTestBattle(String id, String inputs, int[] testPokemon, String[] testMoves) {
        DocumentGrabber documentGrabber = new DocumentGrabber();
		BattleService.getInstance().setDocuments(documentGrabber); // Inject document grabber for mapping between Pokemon names to ids

        Thread battleInstance = new Thread(() -> {
            BattleContextHolder.set(BattleContextHolder.get(), id); // Add battle to holder and concurrent hashmap
            BattleContextHolder.get().setSessionId(id); // Label battle by sessionId
            
            System.out.println("Starting battle for session: " + id);

            Pokemon[] parties = generateParties(testPokemon, testMoves);
            Pokemon[] playerParty = {parties[0], parties[1], parties[2], parties[3], parties[4], parties[5]};
            Pokemon[] botParty = {parties[6], parties[7], parties[8], parties[9], parties[10], parties[11]};

            // Create buffer to feed in player's and bot's actions for a set amount of turns
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inputs.getBytes());

            Scanner scanner = new Scanner(inputStream);

            BattleService.getInstance().initializeTestBattle(id, playerParty, botParty, scanner);

            scanner.close();

            BattleContextHolder.remove();
        });

        battleInstance.start();

        try {
            battleInstance.join();
        } 
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Battle thread interrupted", e);
        }
    }

    static void waitUntilBattleReady(String id) {
        long startTime = System.currentTimeMillis();
        int timeout = 120000; // 120 seconds max wait time

        while (true) {
            Battle context = BattleContextHolder.getSessionById(id);

            if (context != null) {
                BattleContextHolder.set(context, id);

                if (BattleContextHolder.get().getBattleReady()) {
                    break;
                }
            }

            if (System.currentTimeMillis() - startTime > timeout) {
                throw new RuntimeException("Battle initialization timeout for session: " + id);
            }

            try {
                Thread.sleep(500);
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Battle initialization interrupted", e);
            }
        }
    }
}
