package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Abilities.OnSwitchInAbilities;
import com.andycao.pokemon.pokemon_ai.Abilities.OnSwitchOutAbilities;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Moves.NullMove;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class SwitchHandler {
    private Pokemon playerActivePokemon;
    private Pokemon botActivePokemon;

    private InputHandler inputHandler;

    public SwitchHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void setActivePokemon(Pokemon playerPokemon, Pokemon botPokemon) {
        playerActivePokemon = playerPokemon;
        botActivePokemon = botPokemon;
    }

    public void updateActivePokemon() {
        BattleManager.getInstance().updatePlayerActivePokemon(playerActivePokemon);
        BattleManager.getInstance().updateBotActivePokemon(botActivePokemon);
    }

    /*----------Switching----------*/

    // Update whether Pokemon can switch or not. Called from Turn Handler at the end of a turn
    public void canSwitch(Pokemon pokemon) {
        // A Pokemon cannot switch if locked into a move (Ex: Solar Beam)
        // Because Encore implementation uses this locking status, exclude it (Pokemon under Encore can still switch under normal circumstances)
        if (pokemon.lockedIntoMove() && pokemon.getEncoredTurns() == 0) {
            pokemon.setCanSwitch(false);
            return;
        }

        // Ghost types have hidden property of being immune to being prevented from switching (excluding being locked into using a move like a two-turn attack)
        if (pokemon.getType1().equals("Ghost") || pokemon.getType2().equals("Ghost")) {
            pokemon.setCanSwitch(true);
            return;
        }

        // Similar to Ghost types, Pokemon with Run Away as their ability can always switch out (same exclusion as above)
        if (pokemon.getCurrentAbility().equals("Run Away")) {
            pokemon.setCanSwitch(true);
            return;
        }

        // If a Pokemon is bound from a move like Wrap, or Whirlpool, it cannot switch unless it has the above properties
        // This implementation also covers trapping moves like Mean Look (which lasts indefinitely until a Pokemon has fainted)
        if (pokemon.getBoundTurns() > 0) {
            pokemon.setCanSwitch(false);
            return;
        }

        pokemon.setCanSwitch(true); // If none of the previous conditions are met, switching is by default allowed (Ex: Encored Pokemon that is not bound can switch)
    }

    // Handles normal switching and switching from a move like Volt Switch
    public void switchPokemon(Pokemon currentActivePokemon, String switchingMove, String botSwitchIn) throws InvalidIdentifierException {
        if (currentActivePokemon.equals(playerActivePokemon)) {
            // TurnEventMessageBuilder.getInstance().appendEvent(currentActivePokemon.getName() + " went back to Player!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(currentActivePokemon.getName() + " went back to Player!");

            onSwitchOut(currentActivePokemon); // Apply switch out effects for that Pokemon (Ex: reset temporary statuses like Leech Seed or Taunt)

            //playerActivePokemon = inputHandler.getPokemonChoice(PlayerPartyManager.getInstance().updateAvailableParty(playerActivePokemon)); // Get new party member choice
            playerActivePokemon = inputHandler.getPlayerSwitch();

            while (playerActivePokemon == null) {
                BattleManager.getInstance().wait(1000);
                playerActivePokemon = inputHandler.getPlayerSwitch();
            }
    
            inputHandler.setPlayerSwitch(null);

            //BattleManager.getInstance().wait(1000);

            BattleManager.getInstance().appendLastAction(currentActivePokemon, playerActivePokemon, switchingMove);

            BattleManager.getInstance().setPokemonSubstitute(currentActivePokemon, false, 0); // Switching removes substitute for that Pokemon (Except for not yet implement Baton Pass)

            // PlayerPartyManager.getInstance().updateAvailableParty(playerActivePokemon); // Used for display of all non-fainted party members not including current Pokemon
            BattleContextHolder.get().getPlayerPartyHandler().updateAvailableParty(playerActivePokemon); // Used for display of all non-fainted party members not including current Pokemon
            // Include segment afterwards indicating [currentHP/maxHP]: for frontend tracking 
            // TurnEventMessageBuilder.getInstance().appendEvent("Player sent out " + playerActivePokemon.getName() + "! " + playerActivePokemon.getCurrentHp() + "/" + playerActivePokemon.getMaxHp());

            // Include segment afterwards indicating [currentHP/maxHP]: for frontend tracking 
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("Player sent out " + playerActivePokemon.getName() + "! " + playerActivePokemon.getCurrentHp() + "/" + playerActivePokemon.getMaxHp());

            BattleManager.getInstance().updateBoundStatus(playerActivePokemon, botActivePokemon); // Switching out (either Pokemon) causes the bind and trap effect to end
            
            updateActivePokemon(); // Inform BattleManager mediator of change in Pokemon

            onSwitchIn(playerActivePokemon); // Apply switch in effects for that Pokemon (Ex: take entry hazard damage or activate switch in abilities like Intimidate)
        }
        else {
            // TurnEventMessageBuilder.getInstance().appendEvent(currentActivePokemon.getName() + " went back to ChatGPT!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(currentActivePokemon.getName() + " went back to ChatGPT!");

            onSwitchOut(currentActivePokemon);

            // BotPartyManager.getInstance().updateAvailableParty(botActivePokemon);

            // for (Pokemon pokemon : BotPartyManager.getInstance().getAvailableParty()) {
            //     if (pokemon.getName().equals(botSwitchIn)) {
            //         botActivePokemon = pokemon;
            //         break;
            //     }
            // }

            BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(botActivePokemon);

            for (Pokemon pokemon : BattleContextHolder.get().getBotPartyHandler().getAvailableParty()) {
                if (pokemon.getName().equals(botSwitchIn)) {
                    botActivePokemon = pokemon;
                    break;
                }
            }

            // botActivePokemon = inputHandler.getPokemonChoice(BotPartyManager.getInstance().updateAvailableParty(botActivePokemon));
            //BattleManager.getInstance().wait(1000);

            BattleManager.getInstance().appendLastAction(currentActivePokemon, botActivePokemon, switchingMove);

            BattleManager.getInstance().setPokemonSubstitute(currentActivePokemon, false, 0);

            // BotPartyManager.getInstance().updateAvailableParty(botActivePokemon);
            BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(botActivePokemon);
            // TurnEventMessageBuilder.getInstance().appendEvent("ChatGPT sent out " + botActivePokemon.getName() + "! " + botActivePokemon.getCurrentHp() + "/" + botActivePokemon.getMaxHp());
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("ChatGPT sent out " + botActivePokemon.getName() + "! " + botActivePokemon.getCurrentHp() + "/" + botActivePokemon.getMaxHp());

            BattleManager.getInstance().updateBoundStatus(playerActivePokemon, botActivePokemon);

            updateActivePokemon();

            onSwitchIn(botActivePokemon);
        }
    }

    // Handles switching after a Pokemon has fainted
    public void updateSides() throws InvalidIdentifierException {
        if (BattleManager.getInstance().endBattle()) {
            return;
        }

        boolean playerSideFainted = playerActivePokemon.getStatus().equals("Fainted");
        boolean botSideFainted = botActivePokemon.getStatus().equals("Fainted");

        if (playerSideFainted) {
            // TurnEventMessageBuilder.getInstance().appendEvent(playerActivePokemon.getName() + " fainted!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(playerActivePokemon.getName() + " fainted!");
            //playerActivePokemon = inputHandler.getPokemonChoice(PlayerPartyManager.getInstance().updateAvailableParty(playerActivePokemon));

            playerActivePokemon = inputHandler.getPlayerSwitch();

            while (playerActivePokemon == null) {
                BattleManager.getInstance().wait(1000);
                playerActivePokemon = inputHandler.getPlayerSwitch();
            }
    
            inputHandler.setPlayerSwitch(null);

            BattleManager.getInstance().appendLastAction(playerActivePokemon, playerActivePokemon, "Fainted");

            // PlayerPartyManager.getInstance().updateAvailableParty(playerActivePokemon);
            BattleContextHolder.get().getPlayerPartyHandler().updateAvailableParty(playerActivePokemon);
            // TurnEventMessageBuilder.getInstance().appendEvent("Player sent out " + playerActivePokemon.getName() + "! " + playerActivePokemon.getCurrentHp() + "/" + playerActivePokemon.getMaxHp());
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("Player sent out " + playerActivePokemon.getName() + "! " + playerActivePokemon.getCurrentHp() + "/" + playerActivePokemon.getMaxHp());
            
            updateActivePokemon();

            onSwitchIn(playerActivePokemon);
        }
        
        if (botSideFainted) {
            // TurnEventMessageBuilder.getInstance().appendEvent(botActivePokemon.getName() + " fainted!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(botActivePokemon.getName() + " fainted!");

            String choice = inputHandler.getBotMove().split(" ")[1]; // getBotActionChoice(playerActivePokemon, botActivePokemon, "", true).split(" ")[1];
            // for (Pokemon pokemon : BotPartyManager.getInstance().updateAvailableParty(botActivePokemon)) {
            //     if (pokemon.getName().equals(choice)) {
            //         botActivePokemon = pokemon;
            //         break;
            //     }
            // }
            for (Pokemon pokemon : BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(botActivePokemon)) {
                if (pokemon.getName().equals(choice)) {
                    botActivePokemon = pokemon;
                    break;
                }
            }

            //botActivePokemon = inputHandler.getPokemonChoice(BotPartyManager.getInstance().updateAvailableParty(botActivePokemon));

            BattleManager.getInstance().appendLastAction(botActivePokemon, botActivePokemon, "Fainted");

            // BotPartyManager.getInstance().updateAvailableParty(botActivePokemon);
            BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(botActivePokemon);
            // TurnEventMessageBuilder.getInstance().appendEvent("ChatGPT sent out " + botActivePokemon.getName() + "! " + botActivePokemon.getCurrentHp() + "/" + botActivePokemon.getMaxHp());
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("ChatGPT sent out " + botActivePokemon.getName() + "! " + botActivePokemon.getCurrentHp() + "/" + botActivePokemon.getMaxHp());

            updateActivePokemon();

            onSwitchIn(botActivePokemon);
        }
    }

    /*----------Switching Effects----------*/

    public void onSwitchIn(Pokemon pokemon) throws InvalidIdentifierException {
        BattleManager.getInstance().activateEntryHazards(pokemon);

        OnSwitchInAbilities.execute(pokemon, new NullMove()); 

        pokemon.incrementTurnsOut(); // Start turn counter

        BattleManager.getInstance().activateHealingWish(pokemon); // If healing wish is active on a trainer's side, heal incoming Pokemon
    }
    
    public void onSwitchOut(Pokemon pokemon) throws InvalidIdentifierException {
        // Temporary status effect -> removed when switching
        if (pokemon.getConfused()) {
            pokemon.setConfused(false);
        }

        // If a bound Pokemon swaps through moves like Volt Switch, being whirlwinded out, or having an immunity to trapping, remove bound effect
        if (pokemon.getBoundTurns() > 0) {
            pokemon.setBound(pokemon, 0);
        } 

        OnSwitchOutAbilities.execute(pokemon, new NullMove());

        pokemon.resetBadlyPoisonedTurns(); // Reset bad poison counter (1/16 max HP per turn)

        // All of the following are temporary effects removed upon switching
        pokemon.setSoundBlocked(0);

        pokemon.setTaunted(0);

        pokemon.setEncored("", 0);

        pokemon.setDrowsy(0, true);

        pokemon.setCursed(false);

        pokemon.setLeeched(false);

        pokemon.setDestinyBond(false);

        // Refresh protection turns so it functions normally if the Pokemon swaps back in and attempts to Protect
        pokemon.refreshProtectionTurns();

        // Review grounded state and reset to default (Flying and levitating Pokemon are not grounded, every other Pokemon is)
        if (pokemon.containsType("Flying") || pokemon.getCurrentAbility().equals("Levitate")) {
            pokemon.setGrounded(false);
        }
        else {
            pokemon.setGrounded(true);
        }

        // Restore any Flying type lost through Roost (Ex: Pokemon uses Roost and is switched out from Whirlwind on the same turn before regaining its type)
        if (pokemon.getRoosted()) {
            pokemon.restoreTyping();
        }

        // Reset stat boosts and flags related to Pokemon stats
        pokemon.resetStatChanges();
        pokemon.setStatsLoweredThisTurn(false);
        pokemon.setStatsRaisedThisTurn(false);
        pokemon.setLostHpThisTurn(0);

        pokemon.resetTurnsOut();
    }
}
