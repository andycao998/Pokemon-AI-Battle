package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;

// Store battle events per turn; will replace TurnEventMessageBuilder
public class TurnMessageHandler {
    private boolean loggingEnabled;

    private String playerLastMove;
    private String botLastMove;

    private List<String> turnHistory;
    private StringBuilder currentTurnHistory;

    private String botPrompt;

    public TurnMessageHandler() {
        turnHistory = new ArrayList<String>();
        currentTurnHistory = new StringBuilder();
        loggingEnabled = true;
    }

    public void setLoggingEnabled(boolean state) {
        loggingEnabled = state;
    }

    public String getCurrentTurnHistory() {
        return currentTurnHistory.toString();
    }

    public void setPlayerLastMove(String moveName) {
        playerLastMove = moveName;
    }

    public void setBotLastMove(String moveName) {
        botLastMove = moveName;
    }

    private void appendStartingText() {
        currentTurnHistory.append("Turn: " + BattleManager.getInstance().getTurn() + "\n");
    }

    public void appendEvent(String event) {
        if (!loggingEnabled) {
            return;
        }

        if (currentTurnHistory.isEmpty()) {
            appendStartingText();
        }

        BattleManager.getInstance().streamEvent(event);

        currentTurnHistory.append(event + "\n");
    }

    // Verbose information display to support AI
    public void appendInformation(Pokemon playerActivePokemon, Pokemon[] playerRemainingParty, Pokemon botActivePokemon, Pokemon botRemainingParty[]) throws InvalidIdentifierException {
        currentTurnHistory.append("\n");
        currentTurnHistory.append("Information:\n");
        currentTurnHistory.append("The weather on the battle is: " + BattleManager.getInstance().getWeather() + "\n");

        currentTurnHistory.append("Player Report:\n");
        if (playerActivePokemon.getStatus().equals("Fainted")) {
            currentTurnHistory.append("N/A (Fainted this turn)\n");
        }
        else {
            currentTurnHistory.append(playerActivePokemon + "\n");
            currentTurnHistory.append(playerActivePokemon.getName() + "'s last used move is " + playerLastMove + "\n");
        }

        currentTurnHistory.append(playerActivePokemon.getName() + " still has these unfainted party members: ");
        if (playerRemainingParty.length == 0) {
            currentTurnHistory.append("None");
            return;
        }

        for (Pokemon member : playerRemainingParty) {
            currentTurnHistory.append(member.getName() + ", ");
        }
        currentTurnHistory.replace(currentTurnHistory.length() - 2, currentTurnHistory.length() - 1, "\n");

        currentTurnHistory.append("Is Stealth Rocks currently active on " + playerActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getStealthRocks(playerActivePokemon) + "\n");
        currentTurnHistory.append("There are " + BattleManager.getInstance().getSpikeStacks(playerActivePokemon) + " Spikes stacks on " + playerActivePokemon.getName() + "'s side\n");
        currentTurnHistory.append("There are " + BattleManager.getInstance().getSpikeStacks(playerActivePokemon) + " Toxic Spikes stacks on " + playerActivePokemon.getName() + "'s side\n");
        currentTurnHistory.append("Is Sticky Web currently active on " + playerActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getStickyWeb(playerActivePokemon) + "\n");

        currentTurnHistory.append("Is Reflect currently active on " + playerActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getReflect(playerActivePokemon) + "\n");
        currentTurnHistory.append("Is Light Screen currently active on " + playerActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getLightScreen(playerActivePokemon) + "\n");
        currentTurnHistory.append("Is Aurora Veil currently active on " + playerActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getAuroraVeil(playerActivePokemon) + "\n");

        currentTurnHistory.append("\n\n");

        currentTurnHistory.append("ChatGPT Report:\n");
        if (botActivePokemon.getStatus().equals("Fainted")) {
            currentTurnHistory.append("N/A (Fainted this turn)\n");
        }
        else {
            currentTurnHistory.append(botActivePokemon + "\n");
            currentTurnHistory.append(botActivePokemon.getName() + "'s last used move is " + botLastMove + "\n");
        }

        currentTurnHistory.append(botActivePokemon.getName() + " still has these unfainted party members: ");
        if (botRemainingParty.length == 0) {
            currentTurnHistory.append("None");
            return;
        }

        for (Pokemon member : botRemainingParty) {
            currentTurnHistory.append(member.getName() + ", ");
        }
        currentTurnHistory.replace(currentTurnHistory.length() - 2, currentTurnHistory.length() - 1, "\n");

        currentTurnHistory.append("Is Stealth Rocks currently active on " + botActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getStealthRocks(botActivePokemon) + "\n");
        currentTurnHistory.append("There are " + BattleManager.getInstance().getSpikeStacks(botActivePokemon) + " Spikes stacks on " + botActivePokemon.getName() + "'s side\n");
        currentTurnHistory.append("There are " + BattleManager.getInstance().getSpikeStacks(botActivePokemon) + " Toxic Spikes stacks on " + botActivePokemon.getName() + "'s side\n");
        currentTurnHistory.append("Is Sticky Web currently active on " + botActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getStickyWeb(botActivePokemon) + "\n");

        currentTurnHistory.append("Is Reflect currently active on " + botActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getReflect(botActivePokemon) + "\n");
        currentTurnHistory.append("Is Light Screen currently active on " + botActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getLightScreen(botActivePokemon) + "\n");
        currentTurnHistory.append("Is Aurora Veil currently active on " + botActivePokemon.getName() + "'s side: " + BattleManager.getInstance().getAuroraVeil(botActivePokemon) + "\n");
    }

    private String getPreviousTurnHistories() {
        // 1 turn
        if (turnHistory.size() == 0) {
            return "";
        }
        // 2 turns
        else if (turnHistory.size() == 1) {
            return turnHistory.get(BattleManager.getInstance().getTurn() - 3) + "\n"; // Turns indexed starting at 1
        }
        
        // >= 3 turns: get the last two turns as well
        return turnHistory.get(BattleManager.getInstance().getTurn() - 4) + "\n" + turnHistory.get(BattleManager.getInstance().getTurn() - 3);
    }

    public String getBotPrompt() {
        return botPrompt;
    }

    private void updateBotPrompt(String turnInfo) {
        botPrompt = turnInfo + "\n";
    }

    // Provide context on first turn before selecting an action
    public void printFirstTurnInformation(Pokemon playerActivePokemon, Pokemon botActivePokemon) throws InvalidIdentifierException {
        // appendInformation(playerActivePokemon, PlayerPartyManager.getInstance().updateAvailableParty(playerActivePokemon), botActivePokemon, BotPartyManager.getInstance().updateAvailableParty(botActivePokemon));

        appendInformation(playerActivePokemon, BattleContextHolder.get().getPlayerPartyHandler().updateAvailableParty(playerActivePokemon), 
                          botActivePokemon, BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(botActivePokemon));
        String turnInfo = currentTurnHistory.toString();
        System.out.println(turnInfo);
        updateBotPrompt(turnInfo);
        appendStartingText();
    }

    public void printTurnHistory() {
        System.out.println(currentTurnHistory.toString());
        String turnInfo = getPreviousTurnHistories() + "\n" + currentTurnHistory;
        updateBotPrompt(turnInfo);
        turnHistory.add(currentTurnHistory.toString());
        currentTurnHistory = new StringBuilder();

        appendStartingText();
    }
}

