package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.AI.PokemonAiService;
import com.andycao.pokemon.pokemon_ai.AI.ServerEventsController;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidStatException;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

// Mediator for communication between different battle mechanic handlers
public final class BattleManager {
    private static BattleManager instance;

    private PokemonAiService aiService;

    private ServerEventsController eventsController;

    // Pokemon currently on the field
    private Pokemon playerActivePokemon;
    private Pokemon botActivePokemon;

    // All battle handlers
    private InputHandler inputHandler;
    private TurnHandler turnHandler;
    private AccuracyHandler accuracyHandler;
    private DamageHandler damageHandler;
    private FieldSideEffectsHandler fieldSideEffectsHandler;
    private TrainerSideEffectsHandler playerSideEffectsHandler;
    private TrainerSideEffectsHandler botSideEffectsHandler;
    private ActionHandler actionHandler;
    private SwitchHandler switchHandler;

    private boolean simulationActive;

    /*----------Initialization and Utility----------*/

    private BattleManager() {
        
    }

    public static BattleManager getInstance() {
        if (instance == null) {
            instance = new BattleManager();
        }

        return instance;
    }

    public void createHandlers() {
        //Initialize classes to handle different parts of battling like damage, switching, and field effects
        inputHandler = new ConsoleInputHandler(aiService); // Current console-based input implementation 
        turnHandler = new TurnHandler(inputHandler); // Gets input on what action a Pokemon will take (use a move or switch)
        accuracyHandler = new AccuracyHandler(); // Determines if a move will hit
        damageHandler = new DamageHandler(); // Determines how much damage a move will do
        actionHandler = new ActionHandler(); // Handles step by step process of using a move
        switchHandler = new SwitchHandler(inputHandler); // Gets input on what Pokemon to switch to
        fieldSideEffectsHandler = new FieldSideEffectsHandler(); // Holds information on battle-wide effects (Ex: Weather, Trick Room)
        playerSideEffectsHandler = new TrainerSideEffectsHandler(); // Holds information on trainer-side (Player) effects (Ex: Entry Hazards, Wish)
        botSideEffectsHandler = new TrainerSideEffectsHandler(); // For ChatGPT's side
    }

    public void setAiService(PokemonAiService aiService) {
        this.aiService = aiService;
    }

    public void setEventsController(ServerEventsController eventsController) {
        this.eventsController = eventsController;
    }

    public Pokemon getPlayerPokemon() {
        return playerActivePokemon;
    }

    public Pokemon getBotPokemon() {
        return botActivePokemon;
    }

    // Receives changes from switch handler to inform of changes in currently active Pokemon (either a Pokemon faints or is switched out)
    public void updatePlayerActivePokemon(Pokemon playerPokemon) {
        playerActivePokemon = playerPokemon;
        updateHandlers();
    }

    public void updateBotActivePokemon(Pokemon botPokemon) {
        botActivePokemon = botPokemon;
        updateHandlers();
    }

    // Inform handlers that require active Pokemon information of switches
    private void updateHandlers() {
        actionHandler.updateActivePokemon(playerActivePokemon, botActivePokemon);
        turnHandler.updateActivePokemon(playerActivePokemon, botActivePokemon);
    }

    // Utility method to find targets or users of a move 
    public Pokemon getOpposing(Pokemon user) {
        if (user.equals(playerActivePokemon)) {
            return botActivePokemon;
        }

        if (simulationActive) {
            return new Pokemon(playerActivePokemon);
        }

        return playerActivePokemon;
    }

    // Utility method to return which side of the field an effect is active on (Ex: entry hazards)
    private TrainerSideEffectsHandler getSideHandler(Pokemon pokemon) {
        if (pokemon.equals(playerActivePokemon)) {
            return playerSideEffectsHandler;
        }
        
        return botSideEffectsHandler;
    }

    // Utility method for delays
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } 
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("ERROR, QUIT");
            e.printStackTrace();
        }
    }

    /*----------Turn Handler----------*/

    public void startBattle() throws InvalidIdentifierException, InvalidStatException {
        // Grab each team's leading Pokemon
        playerActivePokemon = PlayerPartyManager.getInstance().getLeadingPokemon();
        botActivePokemon = BotPartyManager.getInstance().getLeadingPokemon();

        switchHandler.setActivePokemon(playerActivePokemon, botActivePokemon); // Active Pokemon changes are recorded and updated from the switch handler
        turnHandler.initializeBattle(playerActivePokemon, botActivePokemon); // Turn handler implements battle flow and turn-based combat system
    }

    public int getTurn() {
        return turnHandler.getTurn();
    }

    public boolean endBattle() {
        return turnHandler.endBattle(); // Check if battle ended at the end of a turn
    }

    public void streamEvent(String message) {
        eventsController.sendEvent(message);
    }

    public void updateBoundStatus(Pokemon playerPokemon, Pokemon botPokemon) {
        turnHandler.checkBoundStatus(playerPokemon, botPokemon); // Check if Pokemon are bound/trapped at the end of a turn
    }

    /*----------Accuracy Handler----------*/

    public boolean passAccuracyCheck(Move move, Pokemon user, Pokemon target) {
        return accuracyHandler.passAccuracyCheck(move, user, target);
    }

    /*----------Damage Handler----------*/

    public int dealDamage(Pokemon target, Move move) {
        return damageHandler.dealDamage(target, move); // Most moves fall under this category of damage
    }

    public int dealDirectDamage(Pokemon target, Move move, int damage) {
        return damageHandler.dealDirectDamage(target, move, damage); // For moves that deal a set amount of damage like Seismic Toss
    }

    public int dealTypelessDamage(Pokemon target, Move move) {
        return damageHandler.dealTypelessDamage(target, move); // For unselectable moves and their effects like Struggle
    }

    public void setCriticalHitsEnabled(boolean state) {
        damageHandler.setCriticalHitsEnabled(state);
    }

    /*----------Action Handler----------*/
    
    // Set on first turn of battle to null implementation of moves (no Pokemon have acted yet)
    public void initializeMoves(Pokemon playerPokemon, Pokemon botPokemon) {
        actionHandler.initialize(playerPokemon, botPokemon); 
    }

    public void fillMoveChoices(Pokemon pokemon, Move firstMove, Move secondMove) throws InvalidIdentifierException {
        actionHandler.setPokemonMoveChoices(pokemon, firstMove, secondMove);
    }

    public Pair<Move, Integer> getPokemonMoveChoice(Pokemon pokemon) {
        Move move = actionHandler.getPokemonMoveChoice(pokemon);
        int order = actionHandler.getPokemonMoveOrder(pokemon);
        return new ImmutablePair<Move, Integer>(move, order); // Return a tuple containing the move used and in which order (useful for moves like Protect)
    }

    // Refresh move choices after every turn
    public void resetMoveChoices() {
        actionHandler.resetPokemonMoveChoices();
    }

    public Move getPokemonLastMove(Pokemon pokemon) {
        return actionHandler.getPokemonLastMove(pokemon); // Utility for getting specific attributes of previously used moves (Ex: Counter and Mirror Coat)
    }

    public void appendLastAction(Pokemon currentPokemon, Pokemon newPokemon, String reason) throws InvalidIdentifierException {
        actionHandler.appendLastAction(currentPokemon, newPokemon, reason); // Covers action last turn and includes things like switching out
    }

    // Utilities for move failure -> Stomping Tantrum
    public boolean getPokemonLastMoveFailed(Pokemon pokemon) {
        return actionHandler.getPokemonLastMoveFailed(pokemon);
    }

    public void setPokemonLastMoveFailed(Pokemon pokemon, boolean state) {
        actionHandler.setPokemonLastMoveFailed(pokemon, state);
    }

    // By default (at beginning of turn), assume moves are successful unless failure is otherwise indicated by certain moves and conditions
    public void resetCurrentMoveFailed() {
        actionHandler.resetPokemonLastMovesFailed();
    }

    public void useMove(Pokemon user, String moveName) throws InvalidIdentifierException {
        actionHandler.useMove(user, moveName);
    }

    public void useSimulatedMove(Pokemon user, Pokemon targetCopy, Move move) throws InvalidIdentifierException {
        simulationActive = true;
        actionHandler.simulatedMove(user, targetCopy, move);
        simulationActive = false;
    }

    public void setProtectionSideEffectActivation(boolean state) {
        actionHandler.setProtectionSideEffectActivation(state); // Flag that only allows protection move's secondary effects to activate once per turn
    }

    // Substitute utilities
    public int getPokemonSubstitute(Pokemon pokemon) {
        return actionHandler.getPokemonSubstitute(pokemon);
    }

    public void setPokemonSubstitute(Pokemon user, boolean newSub, int hp) {
        actionHandler.setPokemonSubstitute(user, newSub, hp);
    }

    public boolean getPokemonSubstituteBypassed(Pokemon pokemon) {
        return actionHandler.getPokemonSubstituteBypassed(pokemon);
    }

    public void resetPokemonSubstitutesEndOfTurn() {
        actionHandler.resetPokemonSubstitutesEndOfTurn(); // Refresh flag that allows move user to temporarily bypass a target's substitute
    }

    public void checkSubstitute(Pokemon target, Pokemon user, Move move) {
        actionHandler.checkSubstitute(target, user, move);
    }

    /*----------Switch Handler----------*/

    public void updateSwitching(Pokemon pokemon) {
        switchHandler.canSwitch(pokemon); // Frequently determine whether something is preventing a switch
    }

    public void onSwitchOut(Pokemon pokemon) throws InvalidIdentifierException {
        switchHandler.onSwitchOut(pokemon); // Switch out abilities, effects, and status resets
    }

    public void onSwitchIn(Pokemon pokemon) throws InvalidIdentifierException {
        switchHandler.onSwitchIn(pokemon); // Switch in abilities and effects
    }

    // For normal switching and switching from moves like U-Turn
    public void switchPokemon(Pokemon currentActivePokemon, String switchingMove) throws InvalidIdentifierException {
        if (currentActivePokemon.equals(playerActivePokemon)) {
            switchHandler.switchPokemon(currentActivePokemon, switchingMove, "");
        }
        else {
            switchHandler.switchPokemon(currentActivePokemon, switchingMove, turnHandler.getBotSwitchIn());
        }
    }

    // For switching after a Pokemon faints
    public void updateSides() throws InvalidIdentifierException {
        switchHandler.updateSides();
    }
    
    /*----------Field-Side Effects Handler----------*/

    public String getWeather() {
        return fieldSideEffectsHandler.getWeather();
    }

    // Called from specific moves like Sunny Day or abilities like Drought
    public void setWeather(String weather, int duration) {
        fieldSideEffectsHandler.setWeather(weather, duration); // Most weather effects are implemented in other handlers, such as turn countdown and damage boosts
        String activeWeather = getWeather();

        switch (activeWeather) {
            case "Harsh Sunlight":
                applyHarshSunlight();
                break;
            case "Rain":
                applyRain();
                break;
            case "Sandstorm":
                applySandstorm();
                break;
            case "Hail":
                applyHail();
                break;
            case "Extremely Harsh Sunlight":
                applyExtremelyHarshSunlight();
                break;
            case "Heavy Rain":
                applyHeavyRain();
                break;
            case "Strong Winds":
                applyStrongWinds();
                break;
            default:
                return;
        }
    }

    public void decrementWeatherDuration() {
        fieldSideEffectsHandler.decrementWeatherDuration();
    }

    // Inform other handlers of weather changes
    private void applyHarshSunlight() {
        TurnEventMessageBuilder.getInstance().appendEvent("The sunlight turned harsh!");
        damageHandler.setWeather("Harsh Sunlight"); // Damage handler recognizes a Fire-type boost during Harsh Sunlight
    }

    private void applyRain() {
        TurnEventMessageBuilder.getInstance().appendEvent("It started to rain!");
        damageHandler.setWeather("Rain"); // Damage handler recognizes a Water-type boost during Rain
    }

    private void applySandstorm() {
        TurnEventMessageBuilder.getInstance().appendEvent("A sandstorm brewed!");
        damageHandler.setWeather("Sandstorm"); // Doesn't affect damage handler. Sandstorm's Sp. Def boost is recorded in the Pokemon class
    }

    private void applyHail() {
        TurnEventMessageBuilder.getInstance().appendEvent("It started to hail!");
        damageHandler.setWeather("Hail"); // Doesn't affect damage handler. Hail/Snow's Defense boost is recorded in the Pokemon class
    }

    private void applyExtremelyHarshSunlight() {
        TurnEventMessageBuilder.getInstance().appendEvent("The sunlight turned extremely harsh!");
        damageHandler.setWeather("Extremely Harsh Sunlight"); // Similar to Harsh Sunlight with a Water negation effect
    }

    private void applyHeavyRain() {
        TurnEventMessageBuilder.getInstance().appendEvent("A heavy rain began to fall!");
        damageHandler.setWeather("Heavy Rain"); // Similar to Rain with a Fire negation effect
    }

    private void applyStrongWinds() {
        TurnEventMessageBuilder.getInstance().appendEvent("Mysterious strong winds are protecting Flying-type Pokemon!");
        damageHandler.setWeather("Strong Winds"); // Damage handler recognizes changes in type-chart for this weather
    }
    
    // Trick Room effects occur in the turn handler
    public int getTrickRoomTurns() {
        return fieldSideEffectsHandler.getTrickRoomTurns();
    }

    public void setTrickRoom(int turns) {
        fieldSideEffectsHandler.setTrickRoom(turns);
    }

    // Plasma Fists utilities
    public boolean plasmaEffectActive() {
        return fieldSideEffectsHandler.getPlasmaEffect();
    }

    public void setPlasmaEffect(boolean state) {
        fieldSideEffectsHandler.setPlasmaEffect(state);
    }

    /*----------Trainer-Side Effects Handler----------*/

    // Entry hazard getters and setters
    public boolean getStealthRocks(Pokemon pokemonSide) {
        return getSideHandler(pokemonSide).getStealthRocks();
    }

    public void setStealthRocks(Pokemon pokemonSide) {
        getSideHandler(pokemonSide).setStealthRocks(true);
    }

    public int getSpikeStacks(Pokemon pokemonSide) {
        return getSideHandler(pokemonSide).getSpikeStacks();
    }

    public void setSpikeStacks(Pokemon pokemonSide) {
        TrainerSideEffectsHandler sideHandler = getSideHandler(pokemonSide);
        sideHandler.setSpikeStacks(sideHandler.getSpikeStacks() + 1);
    }

    public int getToxicSpikeStacks(Pokemon pokemonSide) {
        return getSideHandler(pokemonSide).getToxicSpikeStacks();
    }

    public void setToxicSpikeStacks(Pokemon pokemonSide) {
        TrainerSideEffectsHandler sideHandler = getSideHandler(pokemonSide);
        sideHandler.setToxicSpikeStacks(sideHandler.getToxicSpikeStacks() + 1);
    }

    public boolean getStickyWeb(Pokemon pokemonSide) {
        return getSideHandler(pokemonSide).getStickyWeb();
    }

    public void setStickyWeb(Pokemon pokemonSide) throws InvalidIdentifierException {
        getSideHandler(pokemonSide).setStickyWeb(true);
    }

    public void removeEntryHazards(Pokemon pokemonSide) {
        getSideHandler(pokemonSide).removeEntryHazards();
    }

    public void activateEntryHazards(Pokemon pokemonSide) {
        getSideHandler(pokemonSide).applyEntryHazards(pokemonSide);
    }

    // Screens getters and setters
    public boolean getReflect(Pokemon pokemonSide) {
        return getSideHandler(pokemonSide).getReflectTurns() > 0;
    }

    public void setReflect(Pokemon pokemonSide) {
        getSideHandler(pokemonSide).setReflectTurns(5);
    }

    public boolean getLightScreen(Pokemon pokemonSide) {
        return getSideHandler(pokemonSide).getLightScreenTurns() > 0;
    }

    public void setLightScreen(Pokemon pokemonSide) {
        getSideHandler(pokemonSide).setLightScreenTurns(5);
    }

    public boolean getAuroraVeil(Pokemon pokemonSide) {
        return getSideHandler(pokemonSide).getAuroraVeilTurns() > 0;
    }

    public void setAuroraVeil(Pokemon pokemonSide) {
        getSideHandler(pokemonSide).setAuroraVeilTurns(5);
    }

    public void decrementScreensDuration() {
        playerSideEffectsHandler.decrementScreensDuration();
        botSideEffectsHandler.decrementScreensDuration();
    }

    public void removeScreens(Pokemon pokemonSide) {
        getSideHandler(pokemonSide).removeScreens();
    }

    // Wish and Healing Wish getters and setters
    public boolean getHealingWish(Pokemon pokemon) {
        return getSideHandler(pokemon).getHealingWish();
    }

    public void decrementWishTurns() {
        playerSideEffectsHandler.decrementWishTurns(playerActivePokemon);
        botSideEffectsHandler.decrementWishTurns(botActivePokemon);
    }

    public void applyWish(Pokemon user) {
        getSideHandler(user).applyWish(user);
    }

    public void setHealingWish(Pokemon pokemon, boolean state) {
        getSideHandler(pokemon).setHealingWish(state);
    }

    public void activateHealingWish(Pokemon pokemon) {
        getSideHandler(pokemon).receiveHealingWish(pokemon);
    }

    // Court Change specific utility
    public void swapTrainerSideEffects() {
        TrainerSideEffectsHandler temp = playerSideEffectsHandler;
        playerSideEffectsHandler = botSideEffectsHandler;
        botSideEffectsHandler = temp;
    }
}