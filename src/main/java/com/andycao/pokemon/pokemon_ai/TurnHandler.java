package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidStatException;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Moves.MoveFactory;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class TurnHandler {
    private InputHandler inputHandler;

    private int turn;

    private Pokemon playerActivePokemon;
    private Pokemon botActivePokemon;

    private String botSwitchIn; // Holds the Pokemon selected from the AI to switch to... Switching is two-step command starting with SWITCH and then the Pokemon's name

    private final int MAX_BIND_TURNS = 8;

    public TurnHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        turn = 1;
    }

    /*----------Initialization----------*/

    public void initializeBattle(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException, InvalidStatException {
        TurnEventMessageBuilder.getInstance().appendEvent("A battle started between the Player and ChatGPT!");

        playerActivePokemon = playerPokemon;
        // Include segment afterwards indicating [currentHP/maxHP]: for frontend tracking 
        TurnEventMessageBuilder.getInstance().appendEvent("Player sent out " + playerActivePokemon.getName() + "! " + playerActivePokemon.getCurrentHp() + '/' + playerActivePokemon.getMaxHp());
        
        botActivePokemon = botPokemon;
        TurnEventMessageBuilder.getInstance().appendEvent("ChatGPT sent out " + botActivePokemon.getName() + "! " + botActivePokemon.getCurrentHp() + '/' + botActivePokemon.getMaxHp());

        playerActivePokemon.incrementTurnsOut();
        botActivePokemon.incrementTurnsOut();

        BattleManager.getInstance().onSwitchIn(playerActivePokemon);
        BattleManager.getInstance().onSwitchIn(botActivePokemon);

        botSwitchIn = "";

        BattleManager.getInstance().initializeMoves(playerActivePokemon, botActivePokemon);

        TurnEventMessageBuilder.getInstance().printFirstTurnInformation(playerActivePokemon, botActivePokemon);
        playTurn(playerActivePokemon, botActivePokemon);
    }

    // Called from BattleManager to inform a switch
    public void updateActivePokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        playerActivePokemon = playerPokemon;
        botActivePokemon = botPokemon;
    }

    public int getTurn() {
        return turn;
    }

    public void endTurn() throws InvalidIdentifierException {
        if (!endBattle()) {
            playTurn(playerActivePokemon, botActivePokemon);
        } 
    }

    public boolean endBattle() {
        boolean playerPartyFainted = PlayerPartyManager.getInstance().getLeadingPokemon() == null;
        boolean botPartyFainted = BotPartyManager.getInstance().getLeadingPokemon() == null;
        if (!playerPartyFainted && !botPartyFainted) {
            return false;
        }

        // WIP: Finish winning system and display to frontend
        System.out.println("BATTLE ENDED");

        if (playerPartyFainted) {
            TurnEventMessageBuilder.getInstance().appendEvent("The player has no more Pokemon remaining!");
            TurnEventMessageBuilder.getInstance().appendEvent("ChatGPT wins!");
        }
        else {
            TurnEventMessageBuilder.getInstance().appendEvent("ChatGPT has no more Pokemon remaining!");
            TurnEventMessageBuilder.getInstance().appendEvent("The player wins!");
        }

        return true;
    }

    public String getBotSwitchIn() {
        return botSwitchIn;
    }

    private void fillChosenMoves(Pokemon pokemon1, String moveName1, String moveName2) throws InvalidIdentifierException {
        Move move1 = MoveFactory.generateMove(moveName1);
        Move move2 = MoveFactory.generateMove(moveName2);

        BattleManager.getInstance().fillMoveChoices(pokemon1, move1, move2);
    }

    public void updateTurnReport() throws InvalidIdentifierException {
        TurnEventMessageBuilder.getInstance().appendInformation(playerActivePokemon, PlayerPartyManager.getInstance().getAvailableParty(), 
                                                                botActivePokemon, BotPartyManager.getInstance().getAvailableParty());
        TurnEventMessageBuilder.getInstance().printTurnHistory();
        TurnEventMessageBuilder.getInstance().setPlayerLastMove("");
        TurnEventMessageBuilder.getInstance().setBotLastMove("");
    }

    /*----------Battle Flow----------*/

    private String[] getTurnMoves() throws InvalidIdentifierException {
        Pokemon playerPokemon = BattleManager.getInstance().getPlayerPokemon();
        Pokemon botPokemon = BattleManager.getInstance().getBotPokemon();

        boolean lastPlayerPokemonAlive = false;
        if (PlayerPartyManager.getInstance().updateAvailableParty(playerPokemon).length == 0) {
            lastPlayerPokemonAlive = true;
        }
        
        boolean lastBotPokemonAlive = false;
        if (BotPartyManager.getInstance().updateAvailableParty(botPokemon).length == 0) {
            lastBotPokemonAlive = true;
        }

        // Check if sides can switch
        BattleManager.getInstance().updateSwitching(playerPokemon);
        BattleManager.getInstance().updateSwitching(botPokemon);

        System.out.println("On Field: " + playerPokemon.getName() + ", " + botPokemon.getName());

        String playerMove = inputHandler.getPlayerActionChoice();

        // Loop to pause battle flow until player selects an action from frontend
        while (playerMove == null) {
            BattleManager.getInstance().wait(1000);
            playerMove = inputHandler.getPlayerActionChoice();
        }

        inputHandler.setPlayerActionChoice(null); // Reset for next turn selection

        /*
            String botMove = "IRONHEAD";
            For testing purposes without using AI
        */
        
        String botMove = inputHandler.getBotActionChoice(playerPokemon, botPokemon, playerMove, false);

        // If AI chose a move, continue normally; else split action into 1. SWITCH 2. Pokemon -> store for later
        String[] components = botMove.split(" ");
        if (components[0].equals("SWITCH")) {
            botSwitchIn = components[1];
            botMove = "SWITCH";
        }

        String[] moves = {playerMove, botMove};

        return moves;
    }

    private List<Pair<Pokemon, String>> determineMoveOrder(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException {
        String moves[] = getTurnMoves();
        
        // Pair action choices with Pokemon and get speeds
        Pair<Pokemon, String> playerAction = new ImmutablePair<Pokemon, String>(playerPokemon, moves[0]);
        Pair<Pokemon, String> botAction = new ImmutablePair<Pokemon, String>(botPokemon, moves[1]);

        List<Pair<Pokemon, String>> actionOrder = new ArrayList<Pair<Pokemon, String>>();

        int playerSpeed = playerPokemon.getCurrentSpeed();
        int botSpeed = botPokemon.getCurrentSpeed();

        // Only one side chooses to switch (switching has maximum priority)
        if (playerAction.getValue().equals("SWITCH") && !botAction.getValue().equals("SWITCH")) {
            actionOrder.add(playerAction);
            actionOrder.add(botAction);
        }

        if (!playerAction.getValue().equals("SWITCH") && botAction.getValue().equals("SWITCH")) {
            actionOrder.add(botAction);
            actionOrder.add(playerAction);
        }

        // If both sides choose to switch, follow normal speed rules (faster switches first)
        if (playerAction.getValue().equals("SWITCH") && botAction.getValue().equals("SWITCH")) {
            addActionBasedOnSpeed(actionOrder, playerAction, playerSpeed, botAction, botSpeed);
        }
        
        addActionBasedOnSpeed(actionOrder, playerAction, playerSpeed, botAction, botSpeed);
        return actionOrder;
    }

    private void addActionBasedOnSpeed(List<Pair<Pokemon, String>> actionOrder, Pair<Pokemon, String> playerAction, int playerSpeed, Pair<Pokemon, String> botAction, int botSpeed) throws InvalidIdentifierException {
        // If neither side is switching, check priority first
        if (!playerAction.getValue().equals("SWITCH") && !botAction.getValue().equals("SWITCH")) {
            // Get priorities from moves chosen
            Move playerMove = MoveFactory.generateMove(playerAction.getValue());
            Move botMove = MoveFactory.generateMove(botAction.getValue());

            if (playerMove.getPriority() > botMove.getPriority()) {
                actionOrder.add(playerAction);
                actionOrder.add(botAction);
                return;
            }
            else if (botMove.getPriority() > playerMove.getPriority()) {
                actionOrder.add(botAction);
                actionOrder.add(playerAction);
                return;
            }
        }

        // Moves used are equal in priority: follow normal speed rules
        if (playerSpeed > botSpeed) {
            actionOrder.add(playerAction);
            actionOrder.add(botAction);
        }
        else if (playerSpeed < botSpeed) {
            actionOrder.add(botAction);
            actionOrder.add(playerAction);
        }
        // Speed tie: random choice
        else {
            Random random = new Random();
            if (random.nextInt(2) == 1) {
                actionOrder.add(botAction);
                actionOrder.add(playerAction);
            }
            else {
                actionOrder.add(playerAction);
                actionOrder.add(botAction);
            }
        }

        // Flip order during trick room (priority order functions normally)
        if (BattleManager.getInstance().getTrickRoomTurns() > 0) {
            Collections.reverse(actionOrder);
        }
    }

    public void playTurn(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException {
        BattleManager.getInstance().wait(500);

        List<Pair<Pokemon, String>> actionOrder = determineMoveOrder(playerPokemon, botPokemon);
        
        fillChosenMoves(actionOrder.get(0).getKey(), actionOrder.get(0).getValue(), actionOrder.get(1).getValue()); // Update last used moves in manager

        // WIP: Refactor for readability
        for (int i = 0; i < 2; i++) {
            Pokemon pokemon = actionOrder.get(i).getKey();
            String action = actionOrder.get(i).getValue();
            
            boolean turnInterrupted = false;

            // Normal switching not through move-use
            if (action.equals("SWITCH")) {
                BattleManager.getInstance().switchPokemon(pokemon, "None");

                playerPokemon = BattleManager.getInstance().getPlayerPokemon();
                botPokemon = BattleManager.getInstance().getBotPokemon();
                checkBoundStatus(playerPokemon, botPokemon); // If a switch happened, remove binding status

                continue;
            }
            else {
                // Currently implemented turn interruptions (Pokemon was unable to use its intended move)
                if (turnSpentSleeping(pokemon) && !action.equals("SLEEPTALK")) { // Sleep talk functions while user is asleep
                    TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is fast asleep.");
                    turnInterrupted = true;
                }
                else if (turnSpentParalyzed(pokemon)) {
                    TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is paralyzed! It can't move!");
                    turnInterrupted = true;
                }
                else if (turnSpentFrozen(pokemon)) {
                    TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is frozen solid!");
                    turnInterrupted = true;
                }
                else if (turnSpentConfused(pokemon)) {
                    BattleManager.getInstance().useMove(pokemon, "CONFUSIONDAMAGE"); // Hitting oneself in confusion is treated as a 40 power typeless move against itself
                    turnInterrupted = true;
                }
                else if (soundMoveFailed(pokemon, action)) { // From Throat Chop
                    TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is unable to use a sound-based move!");
                    turnInterrupted = true;
                }
                else if (statusMoveFailed(pokemon, action)) { // From Taunt
                    TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is unable to use a status move!");
                    turnInterrupted = true;
                }
                else {
                    if (pokemon.getLockedMove().getRight() > 0 && !lockedIntoMove(pokemon).equals("")) {
                        if (!(pokemon.getEncoredTurns() > 0 && action.equals("SWITCH"))) {
                            action = lockedIntoMove(pokemon);
                        }
                    }

                    BattleManager.getInstance().useMove(pokemon, action);

                    decreasePP(action, pokemon);

                    pokemon.calculateEffectiveStats(); // Recalculate stats if a move raised/lowered a stat stage
                }

                // Check turn-based effects
                decrementMultiTurnCounter(pokemon);

                checkBoundStatus(playerPokemon, botPokemon);

                if (destinyBondFailedLastTurn(pokemon)) {
                    removeDestinyBond(pokemon);
                }

                pokemon.incrementTurnsOut();
            }

            BattleManager.getInstance().wait(3000);

            // End battle loop early if a Pokemon fainted
            if (playerPokemon.getStatus().equals("Fainted") || botPokemon.getStatus().equals("Fainted")) {
                break;
            }

            Pokemon target = BattleManager.getInstance().getOpposing(pokemon);

            // Skip second Pokemon's turn if they were flinched
            if (target.getFlinched() && i == 0) {
                TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " flinched!");
                target.setFlinched(false);
                target.interruptMultiTurnMove();
                break;
            }

            if (turnInterrupted) {
                pokemon.interruptMultiTurnMove();
                BattleManager.getInstance().setPokemonLastMoveFailed(pokemon, true);
            }
        }

        playerPokemon = BattleManager.getInstance().getPlayerPokemon();
        botPokemon = BattleManager.getInstance().getBotPokemon();

        updateEndOfTurn(playerPokemon, botPokemon);
        endTurn();
    }

    private void updateEndOfTurn(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException {
        playerPokemon.setFlinched(false);
        botPokemon.setFlinched(false);

        burnPokemon(playerPokemon, botPokemon);

        poisonPokemon(playerPokemon, botPokemon);

        badlyPoisonPokemon(playerPokemon, botPokemon);

        dealSandstormDamage(playerPokemon, botPokemon);

        dealBindingDamage(playerPokemon);
        dealBindingDamage(botPokemon);

        cursePokemon(playerPokemon, botPokemon);

        leechPokemon(playerPokemon, botPokemon);

        roostBehavior(playerPokemon, botPokemon);

        BattleManager.getInstance().resetMoveChoices();

        BattleManager.getInstance().decrementWeatherDuration();
        BattleManager.getInstance().decrementScreensDuration();
        notifyWeather();

        updateTrickRoom();

        BattleManager.getInstance().setPlasmaEffect(false); // Plasma Fists effect lasts only 1 turn

        decrementSoundBlockedTurns(playerPokemon);
        decrementSoundBlockedTurns(botPokemon);

        decrementTauntedTurns(playerPokemon);
        decrementTauntedTurns(botPokemon);

        decrementDrowsyTurns(playerPokemon);
        decrementDrowsyTurns(botPokemon);

        decrementEncoredTurns(playerPokemon);
        decrementEncoredTurns(botPokemon);

        playerPokemon.setFirstTurnOfSleep(false);
        botPokemon.setFirstTurnOfSleep(false);
        playerPokemon.decrementSleepTurns();
        botPokemon.decrementSleepTurns();

        BattleManager.getInstance().decrementWishTurns();

        BattleManager.getInstance().resetPokemonSubstitutesEndOfTurn();
        
        playerPokemon.calculateEffectiveStats();
        botPokemon.calculateEffectiveStats();

        // Reset conditionals for next turn
        playerPokemon.setStatsLoweredThisTurn(false);
        playerPokemon.setStatsRaisedThisTurn(false);
        playerPokemon.setLostHpThisTurn(0);
        botPokemon.setStatsLoweredThisTurn(false);
        botPokemon.setStatsRaisedThisTurn(false);
        botPokemon.setLostHpThisTurn(0);

        BattleManager.getInstance().updateSwitching(playerPokemon);
        BattleManager.getInstance().updateSwitching(botPokemon);

        BattleManager.getInstance().setProtectionSideEffectActivation(false);

        BattleManager.getInstance().resetCurrentMoveFailed();

        turn += 1;
        BattleManager.getInstance().updateSides();

        botSwitchIn = "";

        updateTurnReport();

        BattleManager.getInstance().streamEvent("Turn End"); // Notify frontend of turn end
    }

    private void decreasePP(String action, Pokemon pokemon) {
        if (pokemon.getLockedMove().getRight() > 0) {
            return;
        }

        pokemon.decrementMovePP(action);
        if (BattleManager.getInstance().getOpposing(pokemon).getCurrentAbility().equals("Pressure")) {
            pokemon.decrementMovePP(action);
        }
    } 

    /*----------Effects----------*/

    private void burnPokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        if (playerPokemon.getStatus().equals("Burn")) {
            playerPokemon.receiveDamage((int) Math.floor((double) playerPokemon.getMaxHp() / 16), playerPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + " was hurt by its burn!");
        }
        
        if (botPokemon.getStatus().equals("Burn")) {
            botPokemon.receiveDamage((int) Math.floor((double) botPokemon.getMaxHp() / 16), botPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + " was hurt by its burn!");
        }
    }

    private void poisonPokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        if (playerPokemon.getStatus().equals("Poison")) {
            playerPokemon.receiveDamage((int) Math.floor((double) playerPokemon.getMaxHp() / 8), playerPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + " was hurt by its poisoning!");
        }

        if (botPokemon.getStatus().equals("Poison")) {
            botPokemon.receiveDamage((int) Math.floor((double) botPokemon.getMaxHp() / 8), botPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + " was hurt by its poisoning!");
        }
    }

    private void badlyPoisonPokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        if (playerPokemon.getStatus().equals("BadPoison")) {
            playerPokemon.receiveDamage((int) Math.floor(((double) playerPokemon.getTurnsBadlyPoisoned() * playerPokemon.getMaxHp() / 16)), playerPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + " was hurt by its bad poisoning!");
            playerPokemon.incrementBadlyPoisonedTurns();
        }

        if (botPokemon.getStatus().equals("BadPoison")) {
            botPokemon.receiveDamage((int) Math.floor(((double) botPokemon.getTurnsBadlyPoisoned() * botPokemon.getMaxHp() / 16)), botPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + " was hurt by its bad poisoning!");
            botPokemon.incrementBadlyPoisonedTurns();
        }
    }

    private void leechPokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        if (playerPokemon.getLeeched() && !botPokemon.getStatus().equals("Fainted")) {
            int damage = (int) Math.floor((double) playerPokemon.getMaxHp() / 8);
            playerPokemon.receiveDamage(damage, playerPokemon);
            botPokemon.receiveHealing(damage);

            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + "'s HP was leeched by " + botPokemon.getName() + "!");
        }

        if (botPokemon.getLeeched() && !playerPokemon.getStatus().equals("Fainted")) {
            int damage = (int) Math.floor((double) botPokemon.getMaxHp() / 8);
            botPokemon.receiveDamage(damage, botPokemon);
            playerPokemon.receiveHealing(damage);

            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + "'s HP was leeched by " + playerPokemon.getName() + "!");
        }
    }

    private void cursePokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        if (playerPokemon.getCursed()) {
            int damage = (int) Math.floor((double) playerPokemon.getMaxHp() / 4);
            playerPokemon.receiveDamage(damage, playerPokemon);

            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + " took damage from its curse!");
        }
        
        if (botPokemon.getCursed()) {
            int damage = (int) Math.floor((double) botPokemon.getMaxHp() / 4);
            botPokemon.receiveDamage(damage, botPokemon);

            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + " took damage from its curse!");
        }
    }
    
    private boolean turnSpentSleeping(Pokemon pokemon) {
        if (!pokemon.getStatus().equals("Sleep")) {
            return false;
        }

        // For sleep caused by using Rest: guaranteed to sleep for those set turns and only wake up once those turns reach 0
        if (pokemon.getSleepTurns() == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " woke up!");
            pokemon.cureSleep();
            pokemon.setRestSleep(false);
            return false;
        }

        // Sleep is guaranteed to last at least one turn, there is no chance of waking up by normal means
        if (pokemon.getFirstTurnOfSleep()) {
            pokemon.setFirstTurnOfSleep(false); // Remove first turn flag to activate chance-based wake up (independent of set number of sleep turns)
            return true;
        }

        // For non-Rest induced sleep, every turn there is an increased chance of waking up depending on the current number of sleep turns
        if (!pokemon.getRestSleep() && EffectChanceRandomizer.evaluate(1, pokemon.getSleepTurns())) {
            TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " woke up!");
            pokemon.cureSleep();
            return false;
        }
        else {
            return true;
        }
    }

    private boolean turnSpentParalyzed(Pokemon pokemon) {
        if (!pokemon.getStatus().equals("Paralysis")) {
            return false;
        }

        if (EffectChanceRandomizer.evaluate(1, 4)) {
            return true;
        }

        return false;
    }

    private boolean turnSpentFrozen(Pokemon pokemon) {
        if (!pokemon.getStatus().equals("Freeze")) {
            return false;
        }

        if (EffectChanceRandomizer.evaluate(1, 5)) {
            TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " thawed out!");
            pokemon.cureFreeze();
            return false;
        }

        return true;
    }

    private boolean turnSpentConfused(Pokemon pokemon) throws InvalidIdentifierException {
        if (!pokemon.getConfused()) {
            return false;
        }

        TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is confused!");

        if (pokemon.getConfusionTurns() == 0) {
            pokemon.setConfused(false);
            TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " snapped out of its confusion!");
            return false;
        }
        else if (EffectChanceRandomizer.evaluate(1, 3)) {
            pokemon.decrementConfusionTurns();
            return true;
        }
        
        pokemon.decrementConfusionTurns();
        return false;
    }

    private void roostBehavior(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException {
        if (playerPokemon.getRoosted()) {
            playerPokemon.restoreTyping();
        }
        if (botPokemon.getRoosted()) {
            botPokemon.restoreTyping();
        }
    }

    private String lockedIntoMove(Pokemon pokemon) {
        return pokemon.getLockedMove().getLeft(); // Returns move name Pokemon is locked into (Ex: Outrage)
    }

    private void decrementMultiTurnCounter(Pokemon pokemon) {
        if (pokemon.lockedIntoMove()) {
            String moveName = pokemon.getLockedMove().getLeft();
            int turnsLocked = pokemon.getLockedMove().getMiddle();
            int turnCounter = pokemon.getLockedMove().getRight();

            pokemon.setLockedMove(moveName, turnsLocked, turnCounter - 1, turnCounter - 1);
        }
    }

    private void dealSandstormDamage(Pokemon playerPokemon, Pokemon botPokemon) {
        if (!BattleManager.getInstance().getWeather().equals("Sandstorm")) {
            return;
        }

        if (!playerPokemon.containsType("Ground") && !playerPokemon.containsType("Rock") && !playerPokemon.containsType("Steel")) {
            // WIP: Include ability and item checks for immunity

            playerPokemon.receiveDamage((int) Math.floor((double) playerPokemon.getMaxHp() / 16), playerPokemon);
        }

        if (!botPokemon.containsType("Ground") && !botPokemon.containsType("Rock") && !botPokemon.containsType("Steel")) {
            botPokemon.receiveDamage((int) Math.floor((double) botPokemon.getMaxHp() / 16), botPokemon);
        }
    }

    private void notifyWeather() {
        String activeWeather = BattleManager.getInstance().getWeather();

        if (activeWeather.equals("Harsh Sunlight")) {
            TurnEventMessageBuilder.getInstance().appendEvent("The sun is harsh.");
        }
        else if (activeWeather.equals("Rain")) {
            TurnEventMessageBuilder.getInstance().appendEvent("It is raining.");
        }
        else if (activeWeather.equals("Sandstorm")) {
            TurnEventMessageBuilder.getInstance().appendEvent("The sandstorm blows.");
        }
        else if (activeWeather.equals("Hail")) {
            TurnEventMessageBuilder.getInstance().appendEvent("It is hailing.");
        }
        else if (activeWeather.equals("Extremely Harsh Sunlight")) {
            TurnEventMessageBuilder.getInstance().appendEvent("The sun is extremely harsh.");
        }
        else if (activeWeather.equals("Heavy Rain")) {
            TurnEventMessageBuilder.getInstance().appendEvent("It is raining heavily.");
        }
        else if (activeWeather.equals("Strong Winds")) {
            TurnEventMessageBuilder.getInstance().appendEvent("Strong winds are blowing.");
        }
    }

    private void updateTrickRoom() {
        int trickRoomTurns = BattleManager.getInstance().getTrickRoomTurns();

        if (trickRoomTurns > 0) {
            BattleManager.getInstance().setTrickRoom(trickRoomTurns - 1);
            TurnEventMessageBuilder.getInstance().appendEvent("Trick Room is active. Slower Pokemon move first!");
        }
    }

    public void checkBoundStatus(Pokemon playerPokemon, Pokemon botPokemon) {
        int playerBoundTurns = playerPokemon.getBoundTurns();
        int botBoundTurns = botPokemon.getBoundTurns();
        if (playerBoundTurns == 0 && botBoundTurns == 0) {
            return;
        }

        // Distinction between being trapped (Ex: Scary Face) and bound (Ex: Whirlpool)
        String reason;
        if (playerBoundTurns > MAX_BIND_TURNS) {
            reason = "trapped!";
        }
        else {
            reason = "bound!";
        }

        boolean binderNoLongerActive = !playerPokemon.getBinder().equals(playerPokemon) && !playerPokemon.getBinder().equals(botPokemon);
        boolean binderFainted = playerPokemon.getBinder().equals(botPokemon) && botPokemon.getStatus().equals("Fainted");

        if (binderNoLongerActive || binderFainted) {
            playerPokemon.setBound(playerPokemon, 0);
            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + " is no longer " + reason);
        }
        
        binderNoLongerActive = !botPokemon.getBinder().equals(botPokemon) && !botPokemon.getBinder().equals(playerPokemon);
        binderFainted = botPokemon.getBinder().equals(playerPokemon) && playerPokemon.getStatus().equals("Fainted");

        if (binderNoLongerActive || binderFainted) {
            botPokemon.setBound(botPokemon, 0);
            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + " is no longer " + reason);
        }
    }

    // Being bound deals damage while being trapped does not
    private void dealBindingDamage(Pokemon pokemon) {
        if (pokemon.getBoundTurns() == 0 || pokemon.getBoundTurns() > MAX_BIND_TURNS) {
            return;
        }

        int divisor = 8;

        TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is hurt from being bound!");
        pokemon.receiveDamage((int) Math.floor((double) pokemon.getMaxHp() / divisor), pokemon.getBinder());

        pokemon.setBound(pokemon.getBinder(), pokemon.getBoundTurns() - 1);
    }

    private boolean soundMoveFailed(Pokemon pokemon, String moveName) throws InvalidIdentifierException {
        if (pokemon.getSoundBlockedTurns() == 0) {
            return false;
        }

        Move move = MoveFactory.generateMove(moveName);

        if (move.containsFlag("Sound")) {
            return true;
        }

        return false;
    }

    private void decrementSoundBlockedTurns(Pokemon pokemon) {
        int turns = pokemon.getSoundBlockedTurns();
        if (turns > 0) {
            pokemon.setSoundBlocked(turns - 1);
        }
    }

    private boolean statusMoveFailed(Pokemon pokemon, String moveName) throws InvalidIdentifierException {
        if (pokemon.getTauntedTurns() == 0) {
            return false;
        }

        Move move = MoveFactory.generateMove(moveName);

        if (move.getCategory().equals("Status")) {
            return true;
        }

        return false;
    }

    private void decrementTauntedTurns(Pokemon pokemon) {
        int turns = pokemon.getTauntedTurns();
        if (turns > 0) {
            pokemon.setTaunted(turns - 1);
        }
    }

    private void decrementDrowsyTurns(Pokemon pokemon) {
        int turns = pokemon.getDrowsyTurns();
        if (turns > 0) {
            pokemon.setDrowsy(turns - 1, false);
        }
    }

    private boolean destinyBondFailedLastTurn(Pokemon pokemon) {
        if (BattleManager.getInstance().getPokemonLastMove(pokemon).getName().equals("Destiny Bond") && 
            BattleManager.getInstance().getPokemonLastMoveFailed(pokemon)) {

            return true;
        }

        return false;
    }   

    private void removeDestinyBond(Pokemon pokemon) {
        if (!pokemon.getDestinyBond()) {
            return;
        }

        pokemon.setDestinyBond(false);
        TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + "'s Destiny Bond wore off!");
    }

    private void decrementEncoredTurns(Pokemon pokemon) {
        int turns = pokemon.getEncoredTurns();
        if (turns > 0) {
            pokemon.setEncored(pokemon.getEncoredMove(), turns - 1);
        }
    }
}
