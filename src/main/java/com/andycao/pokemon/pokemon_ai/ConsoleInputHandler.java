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

    public ConsoleInputHandler(PokemonAiService aiService) {
        scanner = new Scanner(System.in);
        this.aiService = aiService;
        documentGrabber = new DocumentGrabber(); // Used to curate documents for AI
    }

    /*----------Player Console Input (NOT IN USE)----------*/

    @Override
    public String getMoveChoice(Pokemon pokemon, boolean lastAlive, boolean canSwitch) throws InvalidIdentifierException {
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

        throw new InvalidIdentifierException("Move " + choice + " not found in Pokemon's moveset");
    }
    
    @Override
    public Pokemon getPokemonChoice(Pokemon[] availablePokemon) throws InvalidIdentifierException {
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

        throw new InvalidIdentifierException(choice + " is not a valid Pokemon or part of your team");
    }

    /*----------Player Input----------*/

    // Methods connected to frontend input (button presses)

    public String getPlayerActionChoice() {
        return playerMove;
    }

    public void setPlayerActionChoice(String playerMove) {
        if (playerMove == null) {
            this.playerMove = null;
            return;
        }

        this.playerMove = playerMove.replaceAll("[^a-zA-Z]+", "").toUpperCase(); // Reformat frontend input to usable move
    }

    public Pokemon getPlayerPokemonChoice() {
        return playerSwitch;
    }

    public void setPlayerPokemonChoice(String playerSwitch) {
        if (playerSwitch == null) {
            this.playerSwitch = null;
            return;
        }

        // Check valid Pokemon; if invalid, do nothing (user can reselect new option)
        Pokemon[] availablePokemon = PlayerPartyManager.getInstance().getAvailableParty();
        for (Pokemon pokemon : availablePokemon) {
            if (pokemon.getName().equals(playerSwitch)) {
                this.playerSwitch = pokemon;
                return;
            }
        }

        this.playerSwitch = null;
    }

    /*----------AI Input----------*/

    public String getBotActionChoice(Pokemon playerPokemon, Pokemon botPokemon, String playerMove, boolean botFainted) throws InvalidIdentifierException {
        String info = TurnEventMessageBuilder.getInstance().getBotPrompt();
        BotPromptHandler prompter = new BotPromptHandler(playerPokemon, botPokemon, playerMove);
        String prompt = prompter.getFinalPrompt(info, botFainted); // Including turn information, action options, and structured query

        String response = aiService.queryBot(prompt, documentGrabber.getTurnDocuments(playerPokemon, botPokemon, playerPokemon.getMoves(), botPokemon.getMoves()));
        String choice = response.substring(response.indexOf("[") + 1, response.indexOf("]")); // Remove [] formatting
        choice = validateBotActionChoice(botPokemon, choice, botFainted, prompter);

        return choice;
    }

    private String validateBotActionChoice(Pokemon botPokemon, String choice, boolean botFainted, BotPromptHandler prompter) throws InvalidIdentifierException {
        List<String> validActions = prompter.getAllActions();

        // AI selects one of the actions provided to it
        for (String action : validActions) {
            if (action.contains(choice)) {
                return choice;
            }
        }

        // AI doesn't select a valid action but is not fainted: choose 1st one (usually the Pokemon's first move)
        if (!botFainted) {
            return validActions.get(0);
        }

        // Invalid action with fainted Pokemon: forced to switch into the next leading Pokemon
        return "[SWITCH " + BotPartyManager.getInstance().getLeadingPokemon() + "]";
    }
}
