package com.andycao.pokemon.pokemon_ai;

import java.util.ArrayList;
import java.util.List;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Moves.NullMove;
import com.andycao.pokemon.pokemon_ai.Moves.MoveFactory;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class BotPromptHandler {
    private Pokemon playerPokemon;
    private Pokemon botPokemon;

    private Pokemon[] botParty;

    private String playerMove;

    private List<String> validActionsThisTurn;

    public BotPromptHandler(Pokemon playerPokemon, Pokemon botPokemon, String playerMove) {
        this.playerPokemon = playerPokemon;
        this.botPokemon = botPokemon;

        // botParty = BotPartyManager.getInstance().updateAvailableParty(botPokemon);
        botParty = BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(botPokemon);

        this.playerMove = playerMove;
    }

    /*----------Prompt Building----------*/

    // String together prompt and valid actions for AI
    public String getFinalPrompt(String battleInfo, boolean botFainted) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(battleInfo); // Contains information on previous few turns, entry hazards, current active Pokemon, etc.

        // AI can use a move or switch
        if (!botFainted) {
            prompt.append("Choose an action for " + botPokemon.getName() + ". You are against " + playerPokemon.getName() + ".\n");

            // AI receives information on player selected action
            if (!playerMove.equals("SWITCH")) {
                prompt.append("Your opponent is attempting to use the move " + playerMove + ".\n");
                prompt.append(evaluatePlayerMove()); // Simulate damage
            }
            else {
                prompt.append("Your opponent is attempting to switch into one of its unfainted party members.\n"); // AI knows player will switch but not to which Pokemon
            }
            
            prompt.append("You can choose to use a move or switch. These are your valid actions for this turn. Damaging moves will have their expected damage listed. Pick one: \n");
            prompt.append(getAllActionsVerbose()); // In [MOVENAME]/[SWITCH Pokemon] format
        }
        // AI is forced to switch
        else {
            prompt.append("Choose a Pokemon to replace your now fainted " + botPokemon.getName() + ". You are against " + playerPokemon.getName() + ".\n");
            
            prompt.append("You must choose a party member to switch to. These are your valid switch-ins for this turn. Pick one: \n");
            prompt.append(getAllSwitches());
        }

        prompt.append("Provide your answer first and only in brackets, exactly as formatted: [TACKLE] or [SWITCH Pikachu] or [UTURN Pikachu]. Do not include parentheses. Explain in a short response after.");

        return prompt.toString();
    }

    // For ConsoleInputHandler: actions in list form
    public List<String> getAllActions() {
        List<String> actions = new ArrayList<String>();
        actions.addAll(getAllMoves());
        actions.addAll(getAllSwitches());

        validActionsThisTurn = actions;
        return validActionsThisTurn;
    }

    // For AI prompt: all actions (moves and switches) listed in [] formatting including simulated damage numbers
    private String getAllActionsVerbose() {
        getAllActions();

        StringBuilder actions = new StringBuilder();

        for (String action : validActionsThisTurn) {
            actions.append(action + ", ");
        }

        return actions.toString();
    }

    private List<String> getAllMoves() {
        String[] currentMoves = botPokemon.getMoves();
        List<Move> moves = new ArrayList<Move>();

        // Each Pokemon should have 4 moves
        for (int i = 0; i < 4; i++) {
            Move move = MoveFactory.generateMove(currentMoves[i]);

            if (!move.equals(new NullMove())) {
                moves.add(move);
            }
        }
        
        // Move move1 = MoveFactory.generateMove(currentMoves[0]);
        // Move move2 = MoveFactory.generateMove(currentMoves[1]);
        // Move move3 = MoveFactory.generateMove(currentMoves[2]);
        // Move move4 = MoveFactory.generateMove(currentMoves[3]);
        // Move[] moves = {move1, move2, move3, move4};

        List<String> moveOptions = new ArrayList<String>();

        for (Move move : moves) {
            // Status moves
            if (move.getCategory().equals("Status")) {
                moveOptions.add("[" + move.getId() + "]");
                continue;
            }

            // Damaging moves
            if (!move.getFunctionCode().contains("SwitchOutUser")) {
                moveOptions.add("[" + move.getId() + "]: " + calculateSimulatedDamage(move) + " damage");
                continue;
            }

            // For moves that have secondary effect of switching user out afterwards (U-Turn, Volt Switch, etc.)
            int simulatedDamage = calculateSimulatedDamage(move);
            for (Pokemon pokemon : botParty) {
                moveOptions.add("[" + move.getId() + " " + pokemon.getName() + "]: " + simulatedDamage + " damage");
            }
        }

        if (!botPokemon.lockedIntoMove()) {
            return moveOptions;
        }

        // Only return move that AI's Pokemon is locked into
        List<String> lockedMoveList = new ArrayList<String>();
        String moveLockedInto = botPokemon.getLockedMove().getLeft();

        for (String option : moveOptions) {
            // Locked into move is a switching move (U-Turn, Volt Switch, etc.)
            if (option.contains(" ") && option.contains("[" + moveLockedInto)) {
                lockedMoveList.add(option);
            }
            // Normal move
            else if (option.equals("[" + moveLockedInto + "]")) {
                lockedMoveList.add(option);
                break;
            }
        }

        return lockedMoveList;
    }

    private List<String> getAllSwitches() {
        List<String> switches = new ArrayList<String>();

        if (!botPokemon.getCanSwitch()) {
            return switches;
        }

        for (Pokemon pokemon : botParty) {
            switches.add("[SWITCH " + pokemon.getName() + "]");
        }

        return switches;
    }

    /*----------Simulations----------*/

    private int calculateSimulatedDamage(Move move) {
        // TurnEventMessageBuilder.getInstance().setLoggingEnabled(false); // Prevent logging while in simulation

        BattleContextHolder.get().getTurnMessageHandler().setLoggingEnabled(false); // Prevent logging while in simulation

        Pokemon playerPokemonCopy = new Pokemon(playerPokemon);
        int startHp = playerPokemonCopy.getCurrentHp();

        // Record AI's current Pokemon's stats in case they change after a move/ability proc
        int attackStage = botPokemon.getAttackStage();
        int defenseStage = botPokemon.getDefenseStage();
        int spAttackStage = botPokemon.getSpAttackStage();
        int spDefenseStage = botPokemon.getSpDefenseStage();
        int speedStage = botPokemon.getSpeedStage();
        int accuracyStage = botPokemon.getAccuracyStage();
        int evasionStage = botPokemon.getEvasionStage();

        BattleManager.getInstance().setCriticalHitsEnabled(false); // Remove chance of critical hits that alter decision making

        BattleManager.getInstance().useSimulatedMove(botPokemon, playerPokemonCopy, move);

        int endHp = playerPokemonCopy.getCurrentHp();

        // Restore any stat changes
        int difference = attackStage - botPokemon.getAttackStage();
        if (difference != 0) {
            botPokemon.updateAttackStage(difference, botPokemon, true);
        }
        difference = defenseStage - botPokemon.getDefenseStage();
        if (difference != 0) {
            botPokemon.updateDefenseStage(difference, botPokemon, true);
        }
        difference = spAttackStage - botPokemon.getSpAttackStage();
        if (difference != 0) {
            botPokemon.updateSpAttackStage(difference, botPokemon, true);
        }
        difference = spDefenseStage - botPokemon.getSpDefenseStage();
        if (difference != 0) {
            botPokemon.updateSpDefenseStage(difference, botPokemon, true);
        }
        difference = speedStage - botPokemon.getSpeedStage();
        if (difference != 0) {
            botPokemon.updateSpeedStage(difference, botPokemon, true);
        }
        difference = accuracyStage - botPokemon.getAccuracyStage();
        if (difference != 0) {
            botPokemon.updateAccuracyStage(difference, botPokemon, true);
        }
        difference = evasionStage - botPokemon.getEvasionStage();
        if (difference != 0) {
            botPokemon.updateEvasionStage(difference, botPokemon, true);
        }

        // Reenable normal battle mechanics
        BattleManager.getInstance().setCriticalHitsEnabled(true);

        // TurnEventMessageBuilder.getInstance().setLoggingEnabled(true);
        BattleContextHolder.get().getTurnMessageHandler().setLoggingEnabled(true);

        return startHp - endHp;
    }

    // Evaluation of a move's effectiveness (super effective, not very, etc.)
    private String evaluatePlayerMove() {
        StringBuilder evaluation = new StringBuilder();
        evaluation.append(playerMove + " is " + getEffectiveness(playerMove, botPokemon) + " against " + botPokemon.getName() + ".\n");

        for (Pokemon pokemon : botParty) {
            evaluation.append(playerMove + " is " + getEffectiveness(playerMove, pokemon) + " against " + pokemon.getName() + ".\n");
        }

        return evaluation.toString();
    }

    // WIP: Refactor to another class 
    private String getEffectiveness(String moveName, Pokemon target) {
        Move move = MoveFactory.generateMove(moveName);

        String type = move.getType();

        double effectiveness = 1.0;
        switch (type) {
            case "NORMAL":
                effectiveness = target.getDamageMultiplierFromNormal();
                break;
            case "FIRE":
                effectiveness = target.getDamageMultiplierFromFire();
                break;
            case "WATER":
                effectiveness = target.getDamageMultiplierFromWater();
                break;
            case "ELECTRIC":
                effectiveness = target.getDamageMultiplierFromElectric();
                break;
            case "GRASS":
                effectiveness = target.getDamageMultiplierFromGrass();
                break;
            case "ICE":
                effectiveness = target.getDamageMultiplierFromIce();
                break;
            case "FIGHTING":
                effectiveness = target.getDamageMultiplierFromFighting();
                break;
            case "POISON":
                effectiveness = target.getDamageMultiplierFromPoison();
                break;
            case "GROUND":
                effectiveness = target.getDamageMultiplierFromGround();
                break;
            case "FLYING":
                effectiveness = target.getDamageMultiplierFromFlying();
                break;
            case "PSYCHIC":
                effectiveness = target.getDamageMultiplierFromPsychic();
                break;
            case "BUG":
                effectiveness = target.getDamageMultiplierFromBug();
                break;
            case "ROCK":
                effectiveness = target.getDamageMultiplierFromRock();
                break;
            case "GHOST":
                effectiveness = target.getDamageMultiplierFromGhost();
                break;
            case "DRAGON":
                effectiveness = target.getDamageMultiplierFromDragon();
                break;
            case "DARK":
                effectiveness = target.getDamageMultiplierFromDark();
                break;
            case "STEEL":
                effectiveness = target.getDamageMultiplierFromSteel();
                break;
            case "FAIRY":
                effectiveness = target.getDamageMultiplierFromFairy();
                break;
        }

        if (effectiveness > 1.0) {
            return "super effective";
        }
        else if (effectiveness == 0.0) {
            return "not effective at all";
        }
        else if (effectiveness < 1.0) {
            return "not very effective";
        }
        else {
            return "neutral";
        }
    }
}
