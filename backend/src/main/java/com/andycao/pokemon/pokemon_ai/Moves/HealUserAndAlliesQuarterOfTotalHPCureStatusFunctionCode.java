package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleContextHolder;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BotPartyManager;
import com.andycao.pokemon.pokemon_ai.PlayerPartyManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HealUserAndAlliesQuarterOfTotalHPCureStatusFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon[] party;

        if (moveTarget.equals(BattleManager.getInstance().getPlayerPokemon())) {
            // party = PlayerPartyManager.getInstance().getParty();
            party = BattleContextHolder.get().getPlayerPartyHandler().getParty();
        }
        else {
            // party = BotPartyManager.getInstance().getParty();
            party = BattleContextHolder.get().getBotPartyHandler().getParty();
        }

        for (Pokemon pokemon : party) {
            pokemon.receiveHealing((int) Math.floor((double) pokemon.getMaxHp() / 4));
        }

        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + "'s party was slightly healed!");

        CureUserPartyStatusFunctionCode curePartyStatusFunction = new CureUserPartyStatusFunctionCode();
        curePartyStatusFunction.curePartyStatus(party, moveTarget);
    }
}
