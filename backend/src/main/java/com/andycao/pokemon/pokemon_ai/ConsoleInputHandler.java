package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.AI.PokemonAiService;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.util.List;
import java.util.Scanner;

public class ConsoleInputHandler implements InputHandler {
    private Scanner scanner;

    private final PokemonAiService aiService;

    private final DocumentGrabber documentGrabber;

    private String playerMove;
    private Pokemon playerSwitch;

    public ConsoleInputHandler(PokemonAiService aiService, DocumentGrabber documentGrabber, Scanner actions) {
        scanner = actions;
        this.aiService = aiService;
        this.documentGrabber = documentGrabber; // Used to curate documents for AI
    }

    /*----------Player Console Input----------*/

    @Override
    public String getPlayerMove() {
        Pokemon pokemon = BattleContextHolder.get().getPlayerActivePokemon();
        boolean lastAlive = BattleContextHolder.get().getPlayerPartyHandler().updateAvailableParty(pokemon).length == 0; // Excluding active Pokemon, are there any party members remaining?
        boolean canSwitch = pokemon.getCanSwitch();

        return getMoveChoice(pokemon, lastAlive, canSwitch);
    }

    @Override
    public void setPlayerMove(String playerMove) {
        // Empty because you control the bot's actions as well (player move information not necessary)
    }

    public String getMoveChoice(Pokemon pokemon, boolean lastAlive, boolean canSwitch) {
        System.out.println("What will " + pokemon.getName() + " do?");

        // Print all moves in a Pokemon's current moveset
        int numMoves = pokemon.getMoves().length;
        for (int i = 0; i < numMoves; i++) {
            String move = pokemon.getMoves()[i];
            int movePP = pokemon.getMovePPs(move);

            // Skip a move if it has no PP remaining
            if (movePP == 0) {
                System.out.println((i + 1) + ". ");
                continue;
            }

            System.out.println((i + 1) + ". " + move + " (" + movePP + " PP)");
        }
        // Last alive flag keeps track of the remaining Pokemon in the party. Trainers are unable to switch if their only unfainted Pokemon is already on the field
        // Can switch flag keeps track of whether the Pokemon is blocked from switching due to a binding/trapping move like Constrict or Mean Look
        if (!lastAlive && canSwitch) {
            System.out.println("5. SWITCH");
        }

        String choice = scanner.nextLine();

        // Validate user input for an actual move or an allowed switch
        for (String moves : pokemon.getMoves()) {
            if (moves.equals(choice) || (choice.equals("SWITCH") && !lastAlive)) {
                return choice;
            }
        }

        return pokemon.getMoves()[0]; // Default select first move if choice fails
    }
    
    @Override
    public Pokemon getPlayerSwitch() {
        Pokemon[] availablePokemon = BattleContextHolder.get().getPlayerPartyHandler().updateAvailableParty(BattleContextHolder.get().getPlayerActivePokemon());

        return getPokemonChoice(availablePokemon);
    }

    @Override
    public void setPlayerSwitch(String playerSwitch) {
        // Empty because you control the bot's actions as well (player move information not necessary)
    }

    public Pokemon getPokemonChoice(Pokemon[] availablePokemon) {
        System.out.println("Who will you switch to?");

        // Print all party members excluding currently active Pokemon
        int numPokemon = availablePokemon.length;
        for (int i = 0; i < numPokemon; i++) {
            System.out.println((i + 1) + ". " + availablePokemon[i].getName());
        }

        String choice = scanner.nextLine();

        // Ensure chosen Pokemon is actually in party
        for (Pokemon pokemon : availablePokemon) {
            if (pokemon.getName().equals(choice)) {
                return pokemon;
            }
        }

        return availablePokemon[0]; // Default select first Pokemon if choice fails
    }

    /*----------Bot Console Input----------*/

    @Override
    public String getBotMove() {
        // Pokemon pokemon = BattleContextHolder.get().getBotActivePokemon();
        // boolean lastAlive = BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(pokemon).length == 0; // Excluding active Pokemon, are there any party members remaining?
        // boolean canSwitch = pokemon.getCanSwitch();

        // return getMoveChoice(pokemon, lastAlive, canSwitch);

        return scanner.nextLine();
    }

    @Override
    public String getBotSwitch() {
        Pokemon[] availablePokemon = BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(BattleContextHolder.get().getBotActivePokemon());

        return "SWITCH " + getPokemonChoice(availablePokemon);
    }
}
