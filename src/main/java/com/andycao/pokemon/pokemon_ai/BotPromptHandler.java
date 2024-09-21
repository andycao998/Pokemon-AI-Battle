package com.andycao.pokemon.pokemon_ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
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

        botParty = BotPartyManager.getInstance().updateAvailableParty(botPokemon);

        this.playerMove = playerMove;
    }

    public String getFinalPrompt(String battleInfo, boolean botFainted) throws InvalidIdentifierException {
        StringBuilder prompt = new StringBuilder();
        prompt.append(battleInfo);

        if (!botFainted) {
            prompt.append("Choose an action for " + botPokemon.getName() + ". You are against " + playerPokemon.getName() + ".\n");

            if (!playerMove.equals("SWITCH")) {
                prompt.append("Your opponent is attempting to use the move " + playerMove + ".\n");
                prompt.append(evaluatePlayerMove());
            }
            else {
                prompt.append("Your opponent is attempting to switch into one of its unfainted party members.\n");
            }
            
            prompt.append("You can choose to use a move or switch. These are your valid actions for this turn. Damaging moves will have their expected damage listed. Pick one: \n");
            prompt.append(getAllActionsVerbose());
            //System.out.println(getAllMoves() + getAllSwitches());
        }
        else {
            prompt.append("Choose a Pokemon to replace your now fainted " + botPokemon.getName() + ". You are against " + playerPokemon.getName() + ".\n");
            
            prompt.append("You must choose a party member to switch to. These are your valid switch-ins for this turn. Pick one: \n");
            prompt.append(getAllSwitches());
            //System.out.println(getAllSwitches());
        }
        prompt.append("Provide your answer first and only in brackets, exactly as formatted: [TACKLE] or [SWITCH Pikachu] or [UTURN Pikachu]. Do not include parentheses. Explain in a short response after.");

        return prompt.toString();
    }

    private int calculateSimulatedDamage(Move move) throws InvalidIdentifierException {
        TurnEventMessageBuilder.getInstance().setLoggingEnabled(false);

        Pokemon playerPokemonCopy = new Pokemon(playerPokemon);
        int startHp = playerPokemonCopy.getCurrentHp();

        int attackStage = botPokemon.getAttackStage();
        int defenseStage = botPokemon.getDefenseStage();
        int spAttackStage = botPokemon.getSpAttackStage();
        int spDefenseStage = botPokemon.getSpDefenseStage();
        int speedStage = botPokemon.getSpeedStage();
        int accuracyStage = botPokemon.getAccuracyStage();
        int evasionStage = botPokemon.getEvasionStage();

        BattleManager.getInstance().setCriticalHitsEnabled(false);

        BattleManager.getInstance().useSimulatedMove(botPokemon, playerPokemonCopy, move);

        int endHp = playerPokemonCopy.getCurrentHp();

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

        BattleManager.getInstance().setCriticalHitsEnabled(true);

        TurnEventMessageBuilder.getInstance().setLoggingEnabled(true);

        return startHp - endHp;
    }

    private String evaluatePlayerMove() throws InvalidIdentifierException {
        StringBuilder evaluation = new StringBuilder();
        evaluation.append(playerMove + " is " + getEffectiveness(playerMove, botPokemon) + " against " + botPokemon.getName() + ".\n");

        for (Pokemon pokemon : botParty) {
            evaluation.append(playerMove + " is " + getEffectiveness(playerMove, pokemon) + " against " + pokemon.getName() + ".\n");
        }

        return evaluation.toString();
    }

    private String getEffectiveness(String moveName, Pokemon target) throws InvalidIdentifierException {
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

    public List<String> getAllActions() throws InvalidIdentifierException {
        List<String> actions = new ArrayList<String>();
        actions.addAll(getAllMoves());
        actions.addAll(getAllSwitches());

        validActionsThisTurn = actions;
        return validActionsThisTurn;
    }

    private String getAllActionsVerbose() throws InvalidIdentifierException {
        getAllActions();

        StringBuilder actions = new StringBuilder();

        for (String action : validActionsThisTurn) {
            actions.append(action + ", ");
        }

        //System.out.println(actions.toString());
        return actions.toString();
    }

    private List<String> getAllMoves() throws InvalidIdentifierException {
        //StringBuilder moveInfo = new StringBuilder();
        

        String[] currentMoves = botPokemon.getMoves();
        Move move1 = MoveFactory.generateMove(currentMoves[0]);
        Move move2 = MoveFactory.generateMove(currentMoves[1]);
        Move move3 = MoveFactory.generateMove(currentMoves[2]);
        Move move4 = MoveFactory.generateMove(currentMoves[3]);
        Move[] moves = {move1, move2, move3, move4};
        List<String> moveOptions = new ArrayList<String>();
        // moveOptions.addAll(Arrays.asList(moves));

        for (Move move : moves) {
            if (move.getCategory().equals("Status")) {
                moveOptions.add("[" + move.getId() + "]");
                continue;
            }

            if (!move.getFunctionCode().contains("SwitchOutUser")) {
                moveOptions.add("[" + move.getId() + "]: " + calculateSimulatedDamage(move) + " damage");
                continue;
            }

            int simulatedDamage = calculateSimulatedDamage(move);
            for (Pokemon pokemon : botParty) {
                moveOptions.add("[" + move.getId() + " " + pokemon.getName() + "]: " + simulatedDamage + " damage");
            }
        }

        if (!botPokemon.lockedIntoMove()) {
            return moveOptions;
        }

        List<String> lockedMoveList = new ArrayList<String>();
        String moveLockedInto = botPokemon.getLockedMove().getLeft();

        for (String option : moveOptions) {
            if (option.contains(" ") && option.contains("[" + moveLockedInto)) {
                lockedMoveList.add(option);
            }
            else if (option.equals("[" + moveLockedInto + "]")) {
                lockedMoveList.add(option);
                break;
            }
        }

        return lockedMoveList;
        // for (Move move : moves) {
        //     moveInfo.append("[" + move.getId() + "]");
        //     if (!move.getCategory().equals("Status")) {
        //         moveInfo.append(": " + calculateSimulatedDamage(move) + " damage");
        //     }

        //     moveInfo.append(", ");
        // }
        
        // return moveInfo.toString();
    }

    // private String getAllSwitches() {
    //     if (!botPokemon.getCanSwitch()) {
    //         return "";
    //     }

    //     StringBuilder partyInfo = new StringBuilder();

    //     for (Pokemon pokemon : botParty) {
    //         partyInfo.append("[SWITCH " + pokemon.getName() + "], ");
    //     }

    //     return partyInfo.toString();
    // }

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
}
