package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidPartyException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public interface TrainerPartyManager {
    Pokemon[] getParty();

    Pokemon[] getAvailableParty();

    Pokemon[] updateAvailableParty(Pokemon activePokemon);

    void setParty(Pokemon[] newParty) throws InvalidPartyException;

    Pokemon getLeadingPokemon();

    void cureStatus();
}
