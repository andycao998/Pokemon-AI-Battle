package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.AI.PokemonAiService;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.util.List;
import java.util.Scanner;

import javax.swing.text.Document;

public class ConsoleInputHandler implements InputHandler {
    private Scanner scanner;

    private final PokemonAiService aiService;

    private final DocumentGrabber documentGrabber;

    private String playerMove;

    public ConsoleInputHandler(PokemonAiService aiService) {
        scanner = new Scanner(System.in);
        this.aiService = aiService;
        documentGrabber = new DocumentGrabber();
    }

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

    // private String validateBotActionChoice(Pokemon botPokemon, String choice, boolean botFainted) {
    //     boolean valid = false;

    //     String switchIn = "";
    //     if (choice.length() >= 6 && choice.substring(0, 6).equals("SWITCH")) {
    //         switchIn = choice.split(" ")[1];
    //     }
        
    //     Pokemon[] party = BotPartyManager.getInstance().updateAvailableParty(botPokemon);

    //     for (Pokemon pokemon : party) {
    //         if (pokemon.getName().equals(switchIn)) {
    //             valid = true;
    //             break;
    //         }
    //     }

    //     String validFirstMove = "";

    //     for (String move : botPokemon.getMoves()) {
    //         if (botPokemon.getMovePPs(move) <= 0) {
    //             continue;
    //         }
    //         else if (!move.equals(choice)) {
    //             validFirstMove = move;
    //         }

    //         if (move.equals(choice)) {
    //             valid = true;
    //             break;
    //         }
    //     }

    //     if (valid) {
    //         return choice;
    //     }

    //     if (botFainted) {
    //         return "SWITCH " + BotPartyManager.getInstance().getLeadingPokemon();
    //     }

    //     if (validFirstMove.equals("")) {
    //         return "STRUGGLE";
    //     }

    //     return validFirstMove;
    // }

    private String validateBotActionChoice(Pokemon botPokemon, String choice, boolean botFainted, BotPromptHandler prompter) throws InvalidIdentifierException {
        List<String> validActions = prompter.getAllActions();

        for (String action : validActions) {
            if (action.contains(choice)) {
                return choice;
            }
        }

        if (!botFainted) {
            return validActions.get(0);
        }

        return "[SWITCH " + BotPartyManager.getInstance().getLeadingPokemon() + "]";
    }

    public String getBotActionChoice(Pokemon playerPokemon, Pokemon botPokemon, String playerMove, boolean botFainted) throws InvalidIdentifierException {
        String info = TurnEventMessageBuilder.getInstance().getBotPrompt();

        BotPromptHandler prompter = new BotPromptHandler(playerPokemon, botPokemon, playerMove);
        String prompt = prompter.getFinalPrompt(info, botFainted);
        //System.out.println(prompt);
        String response = aiService.queryBot(prompt, documentGrabber.getTurnDocuments(playerPokemon, botPokemon, playerPokemon.getMoves(), botPokemon.getMoves()));
        String choice = response.substring(response.indexOf("[") + 1, response.indexOf("]"));
        choice = validateBotActionChoice(botPokemon, choice, botFainted, prompter);
        // System.out.println(choice);
        
        //System.out.println(choice + " oisahyfoiusahydsagasdfhfdhsdfhsdh");

        return choice;
    }

    public String getPlayerActionChoice() {
        return playerMove;
    }

    public void setPlayerActionChoice(String playerMove) {
        if (playerMove == null) {
            this.playerMove = null;
            return;
        }

        this.playerMove = playerMove.replaceAll("[^a-zA-Z]+", "").toUpperCase();
    }
}
