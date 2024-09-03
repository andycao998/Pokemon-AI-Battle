package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;

public interface ExecutableMove {
    void execute(Pokemon moveTarget) throws InvalidIdentifierException;
}
