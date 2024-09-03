package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BotPartyManager;
import com.andycao.pokemon.pokemon_ai.PlayerPartyManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class CureUserPartyStatusFunctionCode extends Move {
    public void curePartyStatus(Pokemon[] party, Pokemon user) {
        for (Pokemon pokemon : party) {
            if (pokemon.getStatus().isEmpty() || pokemon.getStatus().equals("Fainted")) {
                continue;
            }

            pokemon.cureMajorStatus();
        }

        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + "'s party was cured of status!");
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon[] party;

        if (moveTarget.equals(BattleManager.getInstance().getPlayerPokemon())) {
            party = PlayerPartyManager.getInstance().getParty();
        }
        else {
            party = BotPartyManager.getInstance().getParty();
        }

        curePartyStatus(party, moveTarget);
    }
}
