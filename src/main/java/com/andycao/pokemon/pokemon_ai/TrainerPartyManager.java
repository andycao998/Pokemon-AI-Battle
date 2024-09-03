package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public interface TrainerPartyManager {
    Pokemon[] updateAvailableParty(Pokemon activePokemon);
}
