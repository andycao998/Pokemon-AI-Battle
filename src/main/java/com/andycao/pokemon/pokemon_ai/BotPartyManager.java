package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidPartyException;

public class BotPartyManager implements TrainerPartyManager {
    private static BotPartyManager instance;

    private Pokemon[] party;
    private Pokemon[] availableParty;

    private BotPartyManager() {
        party = new Pokemon[6];
        updateAvailableParty(BattleManager.getInstance().getBotPokemon());
    }

    public static BotPartyManager getInstance() {
        if (instance == null) {
            instance = new BotPartyManager();
        }

        return instance;
    }

    public Pokemon[] getParty() {
        return party;
    }

    public Pokemon[] getAvailableParty() {
        return availableParty;
    }

    public Pokemon[] updateAvailableParty(Pokemon activePokemon) {
        List<Pokemon> availablePokemon = new ArrayList<Pokemon>();

        for (Pokemon pokemon : party) {
            if (pokemon == null || pokemon.equals(activePokemon)) {
                continue;
            }

            if (!pokemon.getStatus().equals("Fainted")) {
                availablePokemon.add(pokemon);
            }
        }

        availableParty = availablePokemon.toArray(new Pokemon[0]);
        return availableParty;
    }

    public void setParty(Pokemon[] newParty) throws InvalidPartyException {
        if (newParty == null) {
            throw new InvalidPartyException("Party is null");
        }

        int members = 0;
        int providedLength = newParty.length;
        for (int i = 0; i < providedLength; i++) {
            if (newParty[i] != null) {
                party[i] = newParty[i];
                members += 1;
            }
        }
        
        for (int j = members; j < 6; j++) {
            party[j] = null;
        }
    }

    public Pokemon getLeadingPokemon() {
        for (Pokemon pokemon : party) {
            if (pokemon == null) {
                continue;
            }

            if (!pokemon.getStatus().equals("Fainted")) {
                return pokemon;
            }
        }

        return null;
    }

    public void cureStatus() {
        for (Pokemon pokemon : party) {
            if (pokemon.getStatus().isEmpty() || pokemon.getStatus().equals("Fainted")) {
                continue;
            }

            pokemon.cureMajorStatus();
        }
    }
}
