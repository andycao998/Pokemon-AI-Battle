package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.AI.PokemonAiService;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class Battle {
    // All battle handlers
    private InputHandler inputHandler;
    private TurnHandler turnHandler;
    private AccuracyHandler accuracyHandler;
    private DamageHandler damageHandler;
    private FieldSideEffectsHandler fieldSideEffectsHandler;
    private TrainerSideEffectsHandler playerSideEffectsHandler;
    private TrainerSideEffectsHandler botSideEffectsHandler;
    private PartyHandler playerPartyHandler;
    private PartyHandler botPartyHandler;
    private ActionHandler actionHandler;
    private SwitchHandler switchHandler;
    private TurnMessageHandler turnMessageHandler;

    private Pokemon playerActivePokemon;
    private Pokemon botActivePokemon;

    private String sessionId;
    
    public Battle() {

    }

    //Initialize classes to handle different parts of battling like damage, switching, and field effects
    public void createHandlers(PokemonAiService aiService, DocumentGrabber documentGrabber) {
        inputHandler = new ConsoleInputHandler(aiService, documentGrabber); // Current console-based input implementation 
        turnHandler = new TurnHandler(inputHandler); // Gets input on what action a Pokemon will take (use a move or switch)
        accuracyHandler = new AccuracyHandler(); // Determines if a move will hit
        damageHandler = new DamageHandler(); // Determines how much damage a move will do
        actionHandler = new ActionHandler(); // Handles step by step process of using a move
        switchHandler = new SwitchHandler(inputHandler); // Gets input on what Pokemon to switch to
        fieldSideEffectsHandler = new FieldSideEffectsHandler(); // Holds information on battle-wide effects (Ex: Weather, Trick Room)
        playerSideEffectsHandler = new TrainerSideEffectsHandler(); // Holds information on trainer-side (Player) effects (Ex: Entry Hazards, Wish)
        botSideEffectsHandler = new TrainerSideEffectsHandler(); // For ChatGPT's side
        playerPartyHandler = new PartyHandler(); // Holds information on Player's Pokemon team
        botPartyHandler = new PartyHandler(); // For ChatGPT's side
        turnMessageHandler = new TurnMessageHandler(); // Holds battle events that take place during each turn
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Pokemon getPlayerActivePokemon() {
        return playerActivePokemon;
    }

    public Pokemon getBotActivePokemon() {
        return botActivePokemon;
    }

    public void setPlayerActivePokemon(Pokemon pokemon) {
        playerActivePokemon = pokemon;
    }

    public void setBotActivePokemon(Pokemon pokemon) {
        botActivePokemon = pokemon;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public TurnHandler getTurnHandler() {
        return turnHandler;
    }

    public AccuracyHandler getAccuracyHandler() {
        return accuracyHandler;
    }

    public DamageHandler getDamageHandler() {
        return damageHandler;
    }

    public FieldSideEffectsHandler getFieldSideEffectsHandler() {
        return fieldSideEffectsHandler;
    }

    public TrainerSideEffectsHandler getPlayerSideEffectsHandler() {
        return playerSideEffectsHandler;
    }

    public TrainerSideEffectsHandler getBotSideEffectsHandler() {
        return botSideEffectsHandler;
    }

    public void setPlayerSideEffectsHandler(TrainerSideEffectsHandler sideHandler) {
        playerSideEffectsHandler = sideHandler;
    }

    public void setBotSideEffectsHandler(TrainerSideEffectsHandler sideHandler) {
        botSideEffectsHandler = sideHandler;
    }

    public PartyHandler getPlayerPartyHandler() {
        return playerPartyHandler;
    }

    public PartyHandler getBotPartyHandler() {
        return botPartyHandler;
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    public SwitchHandler getSwitchHandler() {
        return switchHandler;
    }

    public TurnMessageHandler getTurnMessageHandler() {
        return turnMessageHandler;
    }
}
