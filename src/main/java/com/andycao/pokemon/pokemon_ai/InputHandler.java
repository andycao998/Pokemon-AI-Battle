package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public interface InputHandler {
    String getMoveChoice(Pokemon pokemon, boolean lastAlive, boolean canSwitch) throws InvalidIdentifierException;
    
    Pokemon getPokemonChoice(Pokemon[] availablePokemon) throws InvalidIdentifierException;

    String getBotActionChoice(Pokemon playerPokemon, Pokemon botPokemon, String playerMove, boolean botFainted) throws InvalidIdentifierException;

    String getPlayerActionChoice();

    void setPlayerActionChoice(String playerMove);
}
