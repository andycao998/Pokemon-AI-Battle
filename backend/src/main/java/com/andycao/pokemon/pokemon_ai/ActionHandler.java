package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Abilities.ModifyMoveAbilities;
import com.andycao.pokemon.pokemon_ai.Abilities.TypeImmunityAbilities;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Moves.MoveFactory;
import com.andycao.pokemon.pokemon_ai.Moves.NullMove;
import com.andycao.pokemon.pokemon_ai.Moves.ProtectUserBanefulBunkerFunctionCode;
import com.andycao.pokemon.pokemon_ai.Moves.ProtectUserFromDamagingMovesKingsShieldFunctionCode;
import com.andycao.pokemon.pokemon_ai.Moves.ProtectUserFromTargetingMovesSpikyShieldFunctionCode;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ActionHandler {
    private Pokemon playerActivePokemon;
    private Pokemon botActivePokemon;

    private Pair<Move, Integer> playerMoveChoice; // Used to keep track of selected move and the order each Pokemon will act (depending on move priority and user speed)
    private Pair<Move, Integer> botMoveChoice;

    private Move playerLastMove; // Used to keep track of last move used by each Pokemon (may differ from selected choice if its turn was skipped for example)
    private Move botLastMove;

    private boolean playerLastMoveFailed; // Used to keep track of move failure for Stomping Tantrum
    private boolean botLastMoveFailed;

    private boolean playerCurrentMoveFailed; // Used to determine if the last move failed or not (current move rolls over to become the last move at turn end)
    private boolean botCurrentMoveFailed;    // If true, a move failed this turn, which will double the power of Stomping Tantrum if used next turn

    private boolean protectionSideEffectActivated; // For protecting moves that have side effects: they should only apply once per turn (in case of multi-hit moves)

    private int playerActivePokemonSubstitute; // Keeps track of substitute HP or 0 if no substitute is active for that Pokemon
    private int botActivePokemonSubstitute;

    private boolean playerSubstituteBypassedThisTurn; // Flag to predetermine if a move will ignore a substitute and directly attack the target
    private boolean botSubstituteBypassedThisTurn;

    private boolean simulationActive;

    /*----------Initialization, Setters, and Getters----------*/

    public void initialize(Pokemon playerPokemon, Pokemon botPokemon) {
        playerActivePokemon = playerPokemon;
        botActivePokemon = botPokemon;
        
        playerLastMove = new NullMove(); // Battle begins: last move hasn't happened
        botLastMove = new NullMove();
    }

    public void updateActivePokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        playerActivePokemon = playerPokemon;
        botActivePokemon = botPokemon;
    }

    public Move getPokemonMoveChoice(Pokemon pokemon) {
        if (pokemon.equals(playerActivePokemon)) {
            return playerMoveChoice.getKey();
        }
        
        return botMoveChoice.getKey();
    }

    // Order: first Pokemon to act is designated 0, second is 1
    public int getPokemonMoveOrder(Pokemon pokemon) {
        if (pokemon.equals(playerActivePokemon)) {
            return playerMoveChoice.getValue();
        }

        return botMoveChoice.getValue();
    }

    public void setPokemonMoveChoices(Pokemon first, Move firstMove, Move secondMove) {
        if (first.equals(playerActivePokemon)) {
            playerMoveChoice = new ImmutablePair<Move,Integer>(firstMove, 0);
            botMoveChoice = new ImmutablePair<Move,Integer>(secondMove, 1);
        }
        else {
            botMoveChoice = new ImmutablePair<Move,Integer>(firstMove, 0);
            playerMoveChoice = new ImmutablePair<Move,Integer>(secondMove, 1);
        }
    }

    public void resetPokemonMoveChoices() {
        playerMoveChoice = new ImmutablePair<Move,Integer>(new NullMove(), -1);
        botMoveChoice = new ImmutablePair<Move,Integer>(new NullMove(), -1);
    }

    public Move getPokemonLastMove(Pokemon pokemon) {
        if (pokemon.equals(playerActivePokemon)) {
            return playerLastMove;
        }
        
        return botLastMove;
    }

    public boolean getPokemonLastMoveFailed(Pokemon pokemon) {
        if (pokemon.equals(playerActivePokemon)) {
            return playerLastMoveFailed;
        }

        return botLastMoveFailed;
    }

    public void setPokemonLastMoveFailed(Pokemon pokemon, boolean state) {
        if (pokemon.equals(playerActivePokemon)) {
            playerLastMoveFailed = state;

            // Keep track of whether a move failed this turn
            if (state) {
                playerCurrentMoveFailed = true;
            }
        }
        else {
            botLastMoveFailed = state;

            if (state) {
                botCurrentMoveFailed = true;
            }
        }
    }

    public void resetPokemonLastMovesFailed() {
        playerLastMoveFailed = false;
        botLastMoveFailed = false;
    }

    // By default (at beginning of turn), assume moves are successful unless failure is otherwise indicated by certain moves and conditions
    public void resetCurrentMoveFailed() {
        playerCurrentMoveFailed = false;
        botCurrentMoveFailed = false;
    }

    private void updateLastMove(Move move, Pokemon user) {
        if (user.equals(playerActivePokemon)) {
            playerLastMove = move;

            // Update last move failed depending on current move failure status
            if (playerLastMoveFailed && playerCurrentMoveFailed) {
                playerLastMoveFailed = true;
            }
            else if (playerLastMoveFailed && !playerCurrentMoveFailed) {
                playerLastMoveFailed = false;
            }
        }
        else if (user.equals(botActivePokemon)) {
            botLastMove = move;

            if (botLastMoveFailed && botCurrentMoveFailed) {
                botLastMoveFailed = true;
            }
            else if (botLastMoveFailed && !botCurrentMoveFailed) {
                botLastMoveFailed = false;
            }
        }
    }

    // Provides last action information for ChatGPT: if move was successful or something unknown to the bot happened (Ex: a switch occurred or a Pokemon fainted)
    public void appendLastAction(Pokemon currentPokemon, Pokemon newPokemon, String switchReason) {
        String updateText = "";
        Pokemon user = newPokemon;

        // Standard switching
        if (switchReason.equals("None")) {
            updateText = "Switched to " + newPokemon.getName();
            updateLastMove(new NullMove(), user);
        }
        // Switching after a Pokemon has fainted
        else if (switchReason.equals("Fainted")) {
            updateText = "Fainted";
            updateLastMove(new NullMove(), user);
        }
        // Switching from a move
        else if (switchReason.equals("U-Turn") || switchReason.equals("Flip Turn") || switchReason.equals("Volt Switch") ||
                 switchReason.equals("Baton Pass") || switchReason.equals("Parting Shot") || switchReason.equals("Teleport")) {

            updateText = switchReason + " to " + newPokemon.getName();
        }
        // Regular move use (Ex: Thunderbolt)
        else {
            updateText = switchReason;
        }

        // Information sent to be printed
        // if (currentPokemon.equals(playerActivePokemon)) {
        //     TurnEventMessageBuilder.getInstance().setPlayerLastMove(updateText);
        // }
        // else {
        //     TurnEventMessageBuilder.getInstance().setBotLastMove(updateText);
        // }

        if (currentPokemon.equals(playerActivePokemon)) {
            BattleContextHolder.get().getTurnMessageHandler().setPlayerLastMove(updateText);
        }
        else {
            BattleContextHolder.get().getTurnMessageHandler().setBotLastMove(updateText);
        }
    }

    public boolean getSimulationActive() {
        return simulationActive;
    }

    /*----------Pre-Move Use----------*/

    // Which side/Pokemon should a move operate on
    public Pokemon getMoveTarget(Pokemon user, String code) {
        Pokemon opposing = BattleManager.getInstance().getOpposing(user);
        if (code.equals("NearOther")) {
            return opposing;
        }
        else if (code.equals("User") || code.equals("UserSide") || code.equals("UserAndAllies")) {
            return user;
        }

        return opposing;
    }

    // Check if move chosen is in Pokemon's current moveset
    private boolean checkValidMove(String moveName, String[] userMoves) {
        for (String userMove : userMoves) {
            if (userMove.equals(moveName)) {
                return true;
            }
        }

        return false;
    }

    // Scenario where a Pokemon is forced into hurting itself from confusion or struggling due to not being able to use any of its moves
    private boolean forcedMove(Pokemon target, Pokemon user, Move move) throws InvalidIdentifierException {
        // if (move.getName().equals("Confusion Damage")) {
        //     TurnEventMessageBuilder.getInstance().appendEvent("It hurt itself in its confusion!");
        // }
        // else if (move.getName().equals("Struggle")) {
        //     TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " used " + move.getName() + "!");
        // }
        // else {
        //     return false; // Exit: use chosen move
        // }

        if (move.getName().equals("Confusion Damage")) {
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("It hurt itself in its confusion!");
        }
        else if (move.getName().equals("Struggle")) {
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(user.getName() + " used " + move.getName() + "!");
        }
        else {
            return false; // Exit: use chosen move
        }

        checksBeforeMoveUse(move, user, target);

        updateLastMove(move, user);
        appendLastAction(user, user, move.getName());

        return true;
    }

    public void useMove(Pokemon user, String moveName) throws InvalidIdentifierException {
        String[] userMoves = user.getMoves();

        //System.out.println(moveName + "             kijhsafoihfgoasihgsadoighsadoighsadgoishagoisahgsadogihsadopighsadogihsadgoishadgoisdhgoih");
        Move move = MoveFactory.generateMove(moveName);

        Pokemon target = getMoveTarget(user, move.getTarget());

        if (forcedMove(target, user, move)) {
            return;
        }

        if (!checkValidMove(moveName, userMoves)) {
            return;
        }

        BattleManager.getInstance().wait(500);

        // TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " used " + move.getName() + "!");
        BattleContextHolder.get().getTurnMessageHandler().appendEvent(user.getName() + " used " + move.getName() + "!");

        if (BattleManager.getInstance().passAccuracyCheck(move, user, target)) {
            checksBeforeMoveUse(move, user, target);
        }
        else {
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " avoided the attack!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + " avoided the attack!");
            user.interruptMultiTurnMove(); // If attack that missed was from a multi-turn move (Ex: Outrage), interrupt it
        }

        updateLastMove(move, user);
        appendLastAction(user, user, move.getName());
    }

    public void simulatedMove(Pokemon user, Pokemon target, Move move) {
        simulationActive = true;
        // BattleManager.getInstance().wait(500);

        try {
            move.execute(target); // Move effect
        }
        catch (InvalidIdentifierException e) {
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("The move failed because of an invalid target!");
        }

        simulationActive = false;
    }

    /*----------Move Checks----------*/

    // Modify or nullify a move depending on abilities, and other effects from other moves. Standard type immunity is handled elsewhere
    private void checksBeforeMoveUse(Move move, Pokemon user, Pokemon target) {
        ModifyMoveAbilities.execute(user, move); // Apply any final modifiers to a move (Ex: Serene Grace)

        // Plasma Fists' effect: Normal moves used during that turn become Electric moves
        if (BattleManager.getInstance().plasmaEffectActive() && move.getType().equals("NORMAL")) {
            move.setType("ELECTRIC");
        }

        // Invulnerability comes from using moves like Fly, Bounce, Dig. Most moves miss regardless of accuracy
        if (checkInvulnerability(target, user, move)) {
            user.interruptMultiTurnMove();
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " is out of reach of the move!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + " is out of reach of the move!");
            return;
        }

        // Protection from using Protect, King's Shield, etc. Depending on protection category, most moves will not affect a target under protection.
        if (checkProtection(target, user, move)) {
            user.interruptMultiTurnMove();
            return;
        }

        // Certain abilities give new type immunities such as Levitate and Volt Absorb. An ability nullification system is not yet implemented
        if (TypeImmunityAbilities.execute(target, move)) {
            user.interruptMultiTurnMove();
            return;
        }

        // Certain typings have hidden immunities/effects. Grass types are immune to powder moves
        if (checkPowderImmunity(target, user, move)) {
            return;
        }

        checkSubstitute(target, user, move); // If the target has a substitute active. Actual damage redirection happens in Pokemon class

        try {
            move.execute(target); // Move effect
        }
        catch (InvalidIdentifierException e) {
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("The move failed because of an invalid target!");
        }
    }

    private boolean checkInvulnerability(Pokemon target, Pokemon user, Move move) {
        if (target.getInvulnerable().isEmpty()) {
            return false;
        }

        // Immunity from being high up in the air. Certain moves like Gust can still affect the target
        if (!checkSkyInvulnerability(move, user, target)) {
            // TurnEventMessageBuilder.getInstance().appendEvent(move.getName() + " reached " + target.getName() + "!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(move.getName() + " reached " + target.getName() + "!");
            return false;
        }

        return true;
    }
    
    private boolean checkSkyInvulnerability(Move move, Pokemon user, Pokemon target) {
        if (!target.getInvulnerable().equals("Sky")) {
            return false;
        }

        // Moves that can hit targets in the sky are denoted as such
        if (move.getFunctionCode().contains("TargetInSky")) {
            return false;
        }

        // Other ways to hit past sky invulnerability such as No Guard and Lock On (not yet implemented)

        return true;
    }

    private boolean checkProtection(Pokemon target, Pokemon user, Move move) {
        // If a non-protection move was used, refresh protection turns (if a protect move was used on a previous turn)
        if (!move.containsFlag("CanProtect")) {
            target.refreshProtectionTurns();
            return false;
        }

        // Protection moves should fail if the opposing Pokemon uses a two-turn attack. Due to implementation of two-turn moves, refresh protection since it should fail
        if (move.getFunctionCode().contains("TwoTurnAttack") && !user.lockedIntoMove()) {
            target.refreshProtectionTurns();
            return false;
        }

        // Moves that protect from damaging and status moves
        if (target.getProtection().equals("Protect")) {
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " was protected!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + " was protected!");
            return true;
        }
        else if (target.getProtection().equals("Spiky Shield")) {
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " was protected!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + " was protected!");

            if (!protectionSideEffectActivated && move.containsFlag("Contact")) {
                // TurnEventMessageBuilder.getInstance().appendEvent("Spiky Shield activated!");
                BattleContextHolder.get().getTurnMessageHandler().appendEvent("Spiky Shield activated!");
                ProtectUserFromTargetingMovesSpikyShieldFunctionCode spikyShield = new ProtectUserFromTargetingMovesSpikyShieldFunctionCode();
                spikyShield.damageAttacker(user, protectionSideEffectActivated);

                protectionSideEffectActivated = true;
            }

            return true;
        }
        else if (target.getProtection().equals("Baneful Bunker")) {
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " was protected!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + " was protected!");

            if (!protectionSideEffectActivated && move.containsFlag("Contact")) {
                // TurnEventMessageBuilder.getInstance().appendEvent("Baneful Bunker activated!");
                BattleContextHolder.get().getTurnMessageHandler().appendEvent("Baneful Bunker activated!");
                ProtectUserBanefulBunkerFunctionCode banefulBunker = new ProtectUserBanefulBunkerFunctionCode();
                banefulBunker.poisonTarget(user, protectionSideEffectActivated);

                protectionSideEffectActivated = true;
            }

            return true;
        }

        // Cutoff rest of protection moves
        if (move.getCategory().equals("Status")) {
            target.refreshProtectionTurns();
            return false;
        }

        // Moves that only protect from damaging moves
        if (target.getProtection().equals("King's Shield")) {
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " was protected!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + " was protected!");

            if (!protectionSideEffectActivated && move.containsFlag("Contact")) {
                // TurnEventMessageBuilder.getInstance().appendEvent("King's Shield activated!");
                BattleContextHolder.get().getTurnMessageHandler().appendEvent("King's Shield activated!");
                ProtectUserFromDamagingMovesKingsShieldFunctionCode kingsShield = new ProtectUserFromDamagingMovesKingsShieldFunctionCode();
                kingsShield.lowerAttack1(user, protectionSideEffectActivated);

                protectionSideEffectActivated = true;
            }
            
            return true;
        }

        target.refreshProtectionTurns();
        return false;
    }

    public void setProtectionSideEffectActivation(boolean state) {
        protectionSideEffectActivated = state;
    }

    private boolean checkPowderImmunity(Pokemon target, Pokemon user, Move move) {
        if (!move.containsFlag("Powder")) {
            return false;
        }

        if (target.getType1().equals("Grass") || target.getType2().equals("Grass")) {
            setPokemonLastMoveFailed(user, true); // Indicate move failed for Stomping Tantrum
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " is immune to powder moves!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + " is immune to powder moves!");
            
            return true;
        }

        return false;
    }

    // return health: 0 = no substitute
    public int getPokemonSubstitute(Pokemon pokemon) {
        if (pokemon.equals(playerActivePokemon)) {
            return playerActivePokemonSubstitute;
        }
        else {
            return botActivePokemonSubstitute;
        }
    }

    // Handles creation of substitutes and damaging of existing substitutes
    public void setPokemonSubstitute(Pokemon user, boolean newSub, int hp) {
        // New substitutes start with a fourth of the max HP of the user
        if (newSub) {
            hp = (int) Math.ceil((double) user.getMaxHp() / 4);
        }

        // Apply health or set to new health after taking damage
        if (user.equals(playerActivePokemon)) {
            playerActivePokemonSubstitute = hp;
        }
        else if (user.equals(botActivePokemon)){
            botActivePokemonSubstitute = hp;
        }

        if (playerActivePokemonSubstitute < 0) {
            // TurnEventMessageBuilder.getInstance().appendEvent(playerActivePokemon.getName() + "'s substitute was destroyed!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(playerActivePokemon.getName() + "'s substitute was destroyed!");
            playerActivePokemonSubstitute = 0;
        }
        if (botActivePokemonSubstitute < 0) {
            // TurnEventMessageBuilder.getInstance().appendEvent(botActivePokemon.getName() + "'s substitute was destroyed!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(botActivePokemon.getName() + "'s substitute was destroyed!");
            botActivePokemonSubstitute = 0;
        }
    }

    public boolean getPokemonSubstituteBypassed(Pokemon pokemon) {
        if (pokemon.equals(playerActivePokemon)) {
            return playerSubstituteBypassedThisTurn;
        }
        else {
            return botSubstituteBypassedThisTurn;
        }
    }

    // At end of turn, no moves should be able to work through substitute until a new move (that bypasses) is chosen next turn
    public void resetPokemonSubstitutesEndOfTurn() {
        playerSubstituteBypassedThisTurn = false;
        botSubstituteBypassedThisTurn = false;
    }

    public void checkSubstitute(Pokemon target, Pokemon user, Move move) {
        if (move.getName().equals("Substitute")) {
            return;
        }

        if (target.equals(playerActivePokemon)) {
            if (bypassSubstitute(target, user, move)) {
                playerSubstituteBypassedThisTurn = true;
            }
        }
        else {
            if (bypassSubstitute(target, user, move)) {
                botSubstituteBypassedThisTurn = true;
            }
        }
    }

    private boolean bypassSubstitute(Pokemon target, Pokemon user, Move move) {
        if (getPokemonSubstitute(target) == 0) {
            return true;
        }

        // Other ways to bypass substitute, such as Infiltrator ability (not yet implemented)

        // Sound moves by default bypass substitute. Other moves are hard coded to bypass
        if (move.containsFlag("Sound") || move.containsFlag("BypassSubstitute")) {
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + "'s substitute was bypassed!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + "'s substitute was bypassed!");
            return true;
        }

        return false;
    }
}
