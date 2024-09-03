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

    private String botSwitchIn;

    private final int MAX_BIND_TURNS = 8;

    public TurnHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        turn = 1;
    }

    public void initializeBattle(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException, InvalidStatException {
        TurnEventMessageBuilder.getInstance().appendEvent("A battle started between the Player and ChatGPT!");

        playerActivePokemon = playerPokemon;
        TurnEventMessageBuilder.getInstance().appendEvent("Player sent out " + playerActivePokemon.getName() + "!");
        
        botActivePokemon = botPokemon;
        TurnEventMessageBuilder.getInstance().appendEvent("ChatGPT sent out " + botActivePokemon.getName() + "!");

        playerActivePokemon.incrementTurnsOut();
        botActivePokemon.incrementTurnsOut();

        BattleManager.getInstance().onSwitchIn(playerActivePokemon);
        BattleManager.getInstance().onSwitchIn(botActivePokemon);

        botSwitchIn = "";

        BattleManager.getInstance().initializeMoves(playerActivePokemon, botActivePokemon);

        TurnEventMessageBuilder.getInstance().printFirstTurnInformation(playerActivePokemon, botActivePokemon);
        playTurn(playerActivePokemon, botActivePokemon);
    }

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
        if (PlayerPartyManager.getInstance().getLeadingPokemon() != null && BotPartyManager.getInstance().getLeadingPokemon() != null) {
            return false;
        }
        else {
            System.out.println("BATTLE ENDED");
            return true;
        }
    }

    public String getBotSwitchIn() {
        return botSwitchIn;
    }








    private void addActionBasedOnSpeed(List<Pair<Pokemon, String>> actionOrder, Pair<Pokemon, String> playerAction, int playerSpeed, Pair<Pokemon, String> botAction, int botSpeed) throws InvalidIdentifierException {
        //System.out.println(playerAction.getValue() + " yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
        //System.out.println(botAction.getValue() + " hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        if (!playerAction.getValue().equals("SWITCH") && !botAction.getValue().equals("SWITCH")) {
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

        if (playerSpeed > botSpeed) {
            actionOrder.add(playerAction);
            actionOrder.add(botAction);
        }
        else if (playerSpeed < botSpeed) {
            actionOrder.add(botAction);
            actionOrder.add(playerAction);
        }
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

        if (BattleManager.getInstance().getTrickRoomTurns() > 0) {
            Collections.reverse(actionOrder);
        }
    }

    private List<Pair<Pokemon, String>> determineMoveOrder(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException {
        String moves[] = getTurnMoves();
        
        Pair<Pokemon, String> playerAction = new ImmutablePair<Pokemon, String>(playerPokemon, moves[0]);
        Pair<Pokemon, String> botAction = new ImmutablePair<Pokemon, String>(botPokemon, moves[1]);

        List<Pair<Pokemon, String>> actionOrder = new ArrayList<Pair<Pokemon, String>>();

        int playerSpeed = playerPokemon.getCurrentSpeed();
        int botSpeed = botPokemon.getCurrentSpeed();

        if (playerAction.getValue().equals("SWITCH") && !botAction.getValue().equals("SWITCH")) {
            actionOrder.add(playerAction);
            actionOrder.add(botAction);
        }

        if (!playerAction.getValue().equals("SWITCH") && botAction.getValue().equals("SWITCH")) {
            actionOrder.add(botAction);
            actionOrder.add(playerAction);
        }

        if (playerAction.getValue().equals("SWITCH") && botAction.getValue().equals("SWITCH")) {
            addActionBasedOnSpeed(actionOrder, playerAction, playerSpeed, botAction, botSpeed);
        }
        
        addActionBasedOnSpeed(actionOrder, playerAction, playerSpeed, botAction, botSpeed);
        return actionOrder;
    }

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

        BattleManager.getInstance().updateSwitching(playerPokemon);
        BattleManager.getInstance().updateSwitching(botPokemon);

        System.out.println("On Field: " + playerPokemon.getName() + ", " + botPokemon.getName());
        String playerMove = inputHandler.getMoveChoice(playerPokemon, lastPlayerPokemonAlive, playerPokemon.getCanSwitch());
        String botMove = "EARTHQUAKE";//inputHandler.getBotActionChoice(playerPokemon, botPokemon, playerMove, false);//inputHandler.getMoveChoice(botPokemon, lastBotPokemonAlive, botPokemon.getCanSwitch());

        // String[] components = botMove.split(" ");
        // if (components[0].equals("SWITCH")) {
        //     botSwitchIn = components[1];
        //     botMove = "SWITCH";
        // }

        if (botMove.length() >= 6 && botMove.substring(0, 6).equals("SWITCH")) {
            String[] components = botMove.split(" ");
            botSwitchIn = components[1];
            botMove = "SWITCH"; // components [0]
        }

        if (botMove.contains(" ")) {
            String[] components = botMove.split(" ");
            botSwitchIn = components[1];
            botMove = components[0];
        }

        //System.out.println(playerMove + "  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        String[] moves = {playerMove, botMove};

        return moves;
    }

    public void playTurn(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException {
        BattleManager.getInstance().wait(500);

        // String[] moves = getTurnMoves();
        // Pokemon[] order = getTurnOrder(playerPokemon, botPokemon);
        List<Pair<Pokemon, String>> actionOrder = determineMoveOrder(playerPokemon, botPokemon);
        
        fillChosenMoves(actionOrder.get(0).getKey(), actionOrder.get(0).getValue(), actionOrder.get(1).getValue());

        for (int i = 0; i < 2; i++) {
            Pokemon pokemon = actionOrder.get(i).getKey();
            String action = actionOrder.get(i).getValue();
            
            boolean turnInterrupted = false;

            if (action.equals("SWITCH")) {
                BattleManager.getInstance().switchPokemon(pokemon, "None");

                // firstTurnSkipped(pokemon);
                playerPokemon = BattleManager.getInstance().getPlayerPokemon();
                botPokemon = BattleManager.getInstance().getBotPokemon();
                checkBoundStatus(playerPokemon, botPokemon);

                continue; // remove if statement later
            }
            else {
                if (turnSpentSleeping(pokemon) && !action.equals("SLEEPTALK")) {
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
                    BattleManager.getInstance().useMove(pokemon, "CONFUSIONDAMAGE");
                    turnInterrupted = true;
                }
                else if (soundMoveFailed(pokemon, action)) {
                    TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is unable to use a sound-based move!");
                    turnInterrupted = true;
                }
                else if (statusMoveFailed(pokemon, action)) {
                    TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is unable to use a status move!");
                    turnInterrupted = true;
                }
                else {
                    if (pokemon.getLockedMove().getRight() > 0 && !lockedIntoMove(pokemon).equals("")) {
                        if (pokemon.getEncoredTurns() > 0 && action.equals("SWITCH")) {

                        }
                        else {
                            action = lockedIntoMove(pokemon);
                        }
                    }
                    //System.out.println(action + " zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
                    BattleManager.getInstance().useMove(pokemon, action);

                    decreasePP(action, pokemon);

                    pokemon.calculateEffectiveStats();
                }

                decrementMultiTurnCounter(pokemon);

                checkBoundStatus(playerPokemon, botPokemon);

                if (destinyBondFailedLastTurn(pokemon)) {
                    removeDestinyBond(pokemon);
                }

                pokemon.incrementTurnsOut();
            }

            BattleManager.getInstance().wait(3000);

            if (playerPokemon.getStatus().equals("Fainted") || botPokemon.getStatus().equals("Fainted")) {
                break;
            }

            Pokemon target = BattleManager.getInstance().getOpposing(pokemon);

            if (target.getFlinched() && i == 0) {
                TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " flinched!");
                target.setFlinched(false);
                target.interruptMultiTurnMove(); // interruptMultiTurnMove(target);
                break;
            }

            // if (pokemon.getStatus().equals("Paralysis") && EffectChanceRandomizer.evaluate(1, 4)) {
            //     TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is paralyzed! It can't move!");
            //     turnInterrupted = true;
            // }

            if (turnInterrupted) {
                pokemon.interruptMultiTurnMove(); // interruptMultiTurnMove(pokemon);
                BattleManager.getInstance().setPokemonLastMoveFailed(pokemon, true);
            }
        }

        playerPokemon = BattleManager.getInstance().getPlayerPokemon();
        botPokemon = BattleManager.getInstance().getBotPokemon();

        updateEndOfTurn(playerPokemon, botPokemon);
        endTurn();
    }

    private void updateEndOfTurn(Pokemon playerPokemon, Pokemon botPokemon) throws InvalidIdentifierException {
        // boolean playerSideFainted = playerPokemon.getStatus().equals("Fainted");
        // boolean botSideFainted = botPokemon.getStatus().equals("Fainted");

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

        // playerPokemon.setProtection("None", 0);
        // botPokemon.setProtection("None", 0);

        BattleManager.getInstance().resetMoveChoices();

        BattleManager.getInstance().decrementWeatherDuration();
        BattleManager.getInstance().decrementScreensDuration();
        notifyWeather();

        updateTrickRoom();

        BattleManager.getInstance().setPlasmaEffect(false);

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

        BattleManager.getInstance().streamEvent("Turn End");
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

    private void burnPokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        if (playerPokemon.getStatus().equals("Burn")) {
            playerPokemon.receiveDamage((int) Math.floor((double) playerPokemon.getMaxHp() / 16), playerPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + " was hurt by its burn!");
            // System.out.println(playerPokemon.getName() + " was hurt by its burn!");
        }
        
        if (botPokemon.getStatus().equals("Burn")) {
            botPokemon.receiveDamage((int) Math.floor((double) botPokemon.getMaxHp() / 16), botPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + " was hurt by its burn!");
            // System.out.println(botPokemon.getName() + " was hurt by its burn!");
        }
    }

    private void poisonPokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        if (playerPokemon.getStatus().equals("Poison")) {
            playerPokemon.receiveDamage((int) Math.floor((double) playerPokemon.getMaxHp() / 8), playerPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + " was hurt by its poisoning!");
            // System.out.println(playerPokemon.getName() + " was hurt by its poisoning!");
        }

        if (botPokemon.getStatus().equals("Poison")) {
            botPokemon.receiveDamage((int) Math.floor((double) botPokemon.getMaxHp() / 8), botPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + " was hurt by its poisoning!");
            // System.out.println(botPokemon.getName() + " was hurt by its poisoning!");
        }
    }

    private void badlyPoisonPokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        if (playerPokemon.getStatus().equals("BadPoison")) {
            playerPokemon.receiveDamage((int) Math.floor(((double) playerPokemon.getTurnsBadlyPoisoned() * playerPokemon.getMaxHp() / 16)), playerPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(playerPokemon.getName() + " was hurt by its bad poisoning!");
            playerPokemon.incrementBadlyPoisonedTurns();
            // System.out.println(playerPokemon.getName() + " was hurt by its bad poisoning!");
        }

        if (botPokemon.getStatus().equals("BadPoison")) {
            botPokemon.receiveDamage((int) Math.floor(((double) botPokemon.getTurnsBadlyPoisoned() * botPokemon.getMaxHp() / 16)), botPokemon);
            TurnEventMessageBuilder.getInstance().appendEvent(botPokemon.getName() + " was hurt by its bad poisoning!");
            botPokemon.incrementBadlyPoisonedTurns();
            // System.out.println(botPokemon.getName() + " was hurt by its bad poisoning!");
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

        // if (pokemon.getStatus().equals("Freeze")) {
        //     if (EffectChanceRandomizer.evaluate(1, 5)) {
        //         TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " thawed out!");
        //         pokemon.cureFreeze();
        //         // System.out.println(pokemon.getName() + " thawed out!");
        //         return false;
        //     }

        //     return true;
        // }
        
        // return false;
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

        // if (pokemon.getConfused()) {
        //     TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is confused!");

        //     if (pokemon.getConfusionTurns() == 0) {
        //         pokemon.setConfused(false);
        //         TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " snapped out of its confusion!");
        //         return false;
        //     }
        //     else if (EffectChanceRandomizer.evaluate(1, 3)) {
        //         pokemon.decrementConfusionTurns();
        //         return true;
        //     }
            
        //     pokemon.decrementConfusionTurns();
        //     return false;
        // }

        // return false;
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
        // choice item

        return pokemon.getLockedMove().getLeft();
    }

    private void decrementMultiTurnCounter(Pokemon pokemon) {
        if (pokemon.lockedIntoMove()) {
            String moveName = pokemon.getLockedMove().getLeft();
            int turnsLocked = pokemon.getLockedMove().getMiddle();
            int turnCounter = pokemon.getLockedMove().getRight();

            pokemon.setLockedMove(moveName, turnsLocked, turnCounter - 1, turnCounter - 1);
        }
    }

    // public void interruptMultiTurnMove(Pokemon user) {
    //     int turnsLocked = user.getLockedMove().getMiddle();
    //     // int turnCounter = user.getLockedMove().getRight();

    //     if (turnsLocked > 0) {    
    //     //    if (turnCounter == 0) {
    //     //         user.setConfused(true);
    //     //         TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " is confused!");
    //     //     }
    //         user.setLockedMove("", user.getLockedMove().getMiddle(), 0, user.getLockedMove().getRight() - 1);
    //     }

    //     if (!user.getInvulnerable().isEmpty()) {
    //         user.setInvulnerable("");
    //     }
    // }

    private void dealSandstormDamage(Pokemon playerPokemon, Pokemon botPokemon) {
        if (!BattleManager.getInstance().getWeather().equals("Sandstorm")) {
            return;
        }

        String playerPokemonType1 = playerPokemon.getType1();
        String playerPokemonType2 = playerPokemon.getType2();
        String botPokemonType1 = botPokemon.getType1();
        String botPokemonType2 = botPokemon.getType2();

        if (!playerPokemonType1.equals("Ground") && !playerPokemonType1.equals("Rock") && !playerPokemonType1.equals("Steel") &&
            !playerPokemonType2.equals("Ground") && !playerPokemonType2.equals("Rock") && !playerPokemonType2.equals("Steel")) {
            
            // Include ability and item checks for immunity
            playerPokemon.receiveDamage((int) Math.floor((double) playerPokemon.getMaxHp() / 16), playerPokemon);
        }

        if (!botPokemonType1.equals("Ground") && !botPokemonType1.equals("Rock") && !botPokemonType1.equals("Steel") &&
            !botPokemonType2.equals("Ground") && !botPokemonType2.equals("Rock") && !botPokemonType2.equals("Steel")) {
            
            // Include ability and item checks for immunity
            botPokemon.receiveDamage((int) Math.floor((double) botPokemon.getMaxHp() / 16), botPokemon);
        }
    }

    private void fillChosenMoves(Pokemon pokemon1, String moveName1, String moveName2) throws InvalidIdentifierException {
        Move move1 = MoveFactory.generateMove(moveName1);
        Move move2 = MoveFactory.generateMove(moveName2);

        BattleManager.getInstance().fillMoveChoices(pokemon1, move1, move2);
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

    private void dealBindingDamage(Pokemon pokemon) {
        if (pokemon.getBoundTurns() == 0 || pokemon.getBoundTurns() > MAX_BIND_TURNS) {
            return;
        }

        int divisor = 8;

        TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " is hurt from being bound!");
        pokemon.receiveDamage((int) Math.floor((double) pokemon.getMaxHp() / divisor), pokemon.getBinder());

        pokemon.setBound(pokemon.getBinder(), pokemon.getBoundTurns() - 1);
    }

    // public void canSwitch(Pokemon pokemon) {
    //     if (pokemon.getType1().equals("Ghost") || pokemon.getType2().equals("Ghost")) {
    //         pokemon.setCanSwitch(true);
    //         return;
    //     }

    //     if (pokemon.getCurrentAbility().equals("Run Away")) {
    //         pokemon.setCanSwitch(true);
    //         return;
    //     }

    //     if (pokemon.getBoundTurns() > 0) {
    //         pokemon.setCanSwitch(false);
    //         return;
    //     }

    //     if (pokemon.lockedIntoMove() && pokemon.getEncoredTurns() > 0) {
    //         pokemon.setCanSwitch(true);
    //         return;
    //     }
    //     else if (pokemon.lockedIntoMove()) {
    //         System.out.println(pokemon.getLockedMove().getLeft());
    //         pokemon.setCanSwitch(false);
    //         return;
    //     }

    //     pokemon.setCanSwitch(true);
    // }

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

    public void updateTurnReport() throws InvalidIdentifierException {
        TurnEventMessageBuilder.getInstance().appendInformation(playerActivePokemon, PlayerPartyManager.getInstance().getAvailableParty(), 
                                                                botActivePokemon, BotPartyManager.getInstance().getAvailableParty());
        TurnEventMessageBuilder.getInstance().printTurnHistory();
        TurnEventMessageBuilder.getInstance().setPlayerLastMove("");
        TurnEventMessageBuilder.getInstance().setBotLastMove("");
    }
}
