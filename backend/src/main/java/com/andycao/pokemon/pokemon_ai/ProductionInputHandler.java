package com.andycao.pokemon.pokemon_ai;

import java.util.List;

import com.andycao.pokemon.pokemon_ai.AI.PokemonAiService;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ProductionInputHandler implements InputHandler {
    private final PokemonAiService aiService;

    private final DocumentGrabber documentGrabber;

    private String playerMove;
    private Pokemon playerSwitch;

    public ProductionInputHandler(PokemonAiService aiService, DocumentGrabber documentGrabber) {
        this.aiService = aiService;
        this.documentGrabber = documentGrabber; // Used to curate documents for AI
    }

    /*----------Player Input----------*/

    // Methods connected to frontend input (button presses)

    public String getPlayerMove() {
        return playerMove;
    }

    public void setPlayerMove(String playerMove) {
        if (playerMove == null) {
            this.playerMove = null;
            return;
        }

        this.playerMove = playerMove.replaceAll("[^a-zA-Z]+", "").toUpperCase(); // Reformat frontend input to usable move
    }

    public Pokemon getPlayerSwitch() {
        return playerSwitch;
    }

    public void setPlayerSwitch(String playerSwitch) {
        if (playerSwitch == null) {
            this.playerSwitch = null;
            return;
        }

        // Check valid Pokemon; if invalid, do nothing (user can reselect new option)
        // Pokemon[] availablePokemon = PlayerPartyManager.getInstance().getAvailableParty();
        Pokemon[] availablePokemon = BattleContextHolder.get().getPlayerPartyHandler().getAvailableParty();
        for (Pokemon pokemon : availablePokemon) {
            if (pokemon.getName().equals(playerSwitch)) {
                this.playerSwitch = pokemon;
                return;
            }
        }

        this.playerSwitch = null;
    }

    /*----------AI Input----------*/

    public String getBotMove() {
        return getBotActionChoice(playerMove);
    }

    public String getBotSwitch() {
        return getBotActionChoice("");
    }

    private String getBotActionChoice(String playerMove) {
        Pokemon playerPokemon = BattleContextHolder.get().getPlayerActivePokemon();
        Pokemon botPokemon = BattleContextHolder.get().getBotActivePokemon();
        boolean botFainted = botPokemon.getStatus() == "Fainted";

        String info = BattleContextHolder.get().getTurnMessageHandler().getBotPrompt();
        BotPromptHandler prompter = new BotPromptHandler(playerPokemon, botPokemon, playerMove);
        String prompt = prompter.getFinalPrompt(info, botFainted); // Including turn information, action options, and structured query

        String response = aiService.queryBot(prompt, documentGrabber.getTurnDocuments(playerPokemon, botPokemon, playerPokemon.getMoves(), botPokemon.getMoves()));
        String choice = response.substring(response.indexOf("[") + 1, response.indexOf("]")); // Remove [] formatting
        choice = validateBotActionChoice(botPokemon, choice, botFainted, prompter);

        return choice;
    }

    private String validateBotActionChoice(Pokemon botPokemon, String choice, boolean botFainted, BotPromptHandler prompter) {
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
        return "[SWITCH " + BattleContextHolder.get().getBotPartyHandler().getLeadingPokemon() + "]";
    }
}
