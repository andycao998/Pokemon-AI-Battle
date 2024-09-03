package com.andycao.pokemon.pokemon_ai.AI;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidStatException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.Pokemon.PokemonAttributesDto;
import com.andycao.pokemon.pokemon_ai.Pokemon.PokemonIdService;
import com.andycao.pokemon.pokemon_ai.Pokemon.PokemonUpdateDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {
    @GetMapping("/{id}")
    public Pokemon getPokemon(@PathVariable int id) {
        return PokemonIdService.getPokemon(id);
    }

    @PostMapping("/api/pokemon")
    public ResponseEntity<Pokemon> createPokemon(@RequestBody int id) throws InvalidStatException, InvalidIdentifierException {
        Pokemon createdPokemon = new Pokemon(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPokemon);
    }
    
    @PatchMapping("/api/pokemon/{id}")
    public ResponseEntity<Pokemon> updatePokemon(@PathVariable int id, @RequestBody PokemonUpdateDto updateDto) {
        
        // Fetch the Pokemon by ID
        Pokemon pokemon = PokemonIdService.getPokemon(id);
        
        // Check if pokemon exists
        if (pokemon == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (!updateDto.warnings.isEmpty()) {
            return ResponseEntity.ok(pokemon);
        }

        try {
            pokemon.setNature(updateDto.getNature());

            pokemon.setIvs(updateDto.ivHp, updateDto.ivAttack, updateDto.ivDefense, updateDto.ivSpAttack, updateDto.ivSpDefense, updateDto.ivSpeed);

            pokemon.setEvs(updateDto.evHp, updateDto.evAttack, updateDto.evDefense, updateDto.evSpAttack, updateDto.evSpDefense, updateDto.evSpeed);

            pokemon.setMoves(updateDto.getCurrentMoves());

            pokemon.setCurrentAbility(updateDto.getCurrentAbility());
        }
        catch (InvalidIdentifierException | InvalidStatException e) {

        }

        // Return the updated Pokemon
        return ResponseEntity.ok(pokemon);
    }
}
