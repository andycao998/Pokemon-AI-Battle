package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleContextHolder;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BotPartyManager;
import com.andycao.pokemon.pokemon_ai.PlayerPartyManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class SwitchOutUserStatusMoveFunctionCode extends Move {
    public boolean canSwitch(Pokemon switcher) {
        // if (switcher.equals(BattleManager.getInstance().getPlayerPokemon()) && PlayerPartyManager.getInstance().getAvailableParty().length == 0) {
        //     TurnEventMessageBuilder.getInstance().appendEvent(switcher.getName() + " has no unfainted party members!");
        //     BattleManager.getInstance().setPokemonLastMoveFailed(switcher, true);
        //     return false;
        // }
        // else if (switcher.equals(BattleManager.getInstance().getBotPokemon()) && BotPartyManager.getInstance().getAvailableParty().length == 0) {
        //     TurnEventMessageBuilder.getInstance().appendEvent(switcher.getName() + " has no unfainted party members!");
        //     BattleManager.getInstance().setPokemonLastMoveFailed(switcher, true);
        //     return false;
        // }

        if (switcher.equals(BattleManager.getInstance().getPlayerPokemon()) && BattleContextHolder.get().getPlayerPartyHandler().getAvailableParty().length == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(switcher.getName() + " has no unfainted party members!");
            BattleManager.getInstance().setPokemonLastMoveFailed(switcher, true);
            return false;
        }
        else if (switcher.equals(BattleManager.getInstance().getBotPokemon()) && BattleContextHolder.get().getBotPartyHandler().getAvailableParty().length == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(switcher.getName() + " has no unfainted party members!");
            BattleManager.getInstance().setPokemonLastMoveFailed(switcher, true);
            return false;
        }

        return true;
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (!canSwitch(moveTarget)) {
            return;
        }

        if (!BattleManager.getInstance().endBattle()) {
            // Bypass trapping effects
            BattleManager.getInstance().switchPokemon(moveTarget, getName());
        }
    }
}
