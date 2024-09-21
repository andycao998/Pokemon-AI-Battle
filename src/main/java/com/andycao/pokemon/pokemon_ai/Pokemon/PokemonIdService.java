package com.andycao.pokemon.pokemon_ai.Pokemon;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public abstract class PokemonIdService {
    private static Resource pokedexFilePath = new ClassPathResource("pokedex.json");

    private static Map<Integer, Pokemon> idToPokemon = new HashMap<Integer, Pokemon>();

    public static PokemonAttributesDto createPokemonFromDexNumber(int pokedexNumber) throws InvalidIdentifierException {
        String jsonContent = null;

        try {
            jsonContent = new String(Files.readAllBytes(Paths.get(pokedexFilePath.getURI())));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Configuration conf = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext context = JsonPath.using(conf).parse(jsonContent);

        List<Map<String, Object>> pokemonList = context.read("$[?(@.pokedex_number == " + pokedexNumber + ")]");

        if (pokemonList == null || pokemonList.isEmpty()) {
            throw new InvalidIdentifierException("Pokémon not found with Pokedex number: " + pokedexNumber);
        }

        Map<String, Object> pokemonData = pokemonList.get(0);

        String name = (String) pokemonData.get("name");
        String type1 = (String) pokemonData.get("type_1");
        String type2 = (String) pokemonData.get("type_2");
        double weightInKg = Double.valueOf(pokemonData.get("weight_kg").toString());
        int hp = (int) pokemonData.get("hp");
        int attack = (int) pokemonData.get("attack");
        int defense = (int) pokemonData.get("defense");
        int spAttack = (int) pokemonData.get("sp_attack");
        int spDefense = (int) pokemonData.get("sp_defense");
        int speed = (int) pokemonData.get("speed");
        double percentageMale;
        if (pokemonData.get("percentage_male").equals("")) {
            percentageMale = -1.0;
        } 
        else {
            percentageMale = Double.valueOf(pokemonData.get("percentage_male").toString());
        }
        double damageMultiplierFromNormal = Double.valueOf(pokemonData.get("normal_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromFire = Double.valueOf(pokemonData.get("fire_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromWater = Double.valueOf(pokemonData.get("water_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromElectric = Double.valueOf(pokemonData.get("electric_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromGrass = Double.valueOf(pokemonData.get("grass_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromIce = Double.valueOf(pokemonData.get("ice_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromFighting = Double.valueOf(pokemonData.get("fighting_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromPoison = Double.valueOf(pokemonData.get("poison_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromGround = Double.valueOf(pokemonData.get("ground_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromFlying = Double.valueOf(pokemonData.get("flying_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromPsychic = Double.valueOf(pokemonData.get("psychic_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromBug = Double.valueOf(pokemonData.get("bug_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromRock = Double.valueOf(pokemonData.get("rock_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromGhost = Double.valueOf(pokemonData.get("ghost_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromDragon = Double.valueOf(pokemonData.get("dragon_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromDark = Double.valueOf(pokemonData.get("dark_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromSteel = Double.valueOf(pokemonData.get("steel_damage_multiplier_against_pokemon").toString());
        double damageMultiplierFromFairy = Double.valueOf(pokemonData.get("fairy_damage_multiplier_against_pokemon").toString());
        String moves = (String) pokemonData.get("moves");
        String ability1 = (String) pokemonData.get("ability_1");
        String ability2 = (String) pokemonData.get("ability_2");
        String hiddenAbility = (String) pokemonData.get("ability_hidden");

        return new PokemonAttributesDto(name, type1, type2, weightInKg, hp, attack, defense, spAttack, spDefense, speed, percentageMale, damageMultiplierFromNormal, 
                                        damageMultiplierFromFire, damageMultiplierFromWater, damageMultiplierFromElectric, damageMultiplierFromGrass, damageMultiplierFromIce, 
                                        damageMultiplierFromFighting, damageMultiplierFromPoison, damageMultiplierFromGround, damageMultiplierFromFlying, 
                                        damageMultiplierFromPsychic, damageMultiplierFromBug, damageMultiplierFromRock, damageMultiplierFromGhost, damageMultiplierFromDragon, 
                                        damageMultiplierFromDark, damageMultiplierFromSteel, damageMultiplierFromFairy, moves, ability1, ability2, hiddenAbility);
    }

    public static void recordPokemon(Pokemon pokemon) {
        idToPokemon.put(pokemon.getSpeciesId(), pokemon);
    }

    public static Pokemon getPokemon(int dexNumber) {
        return idToPokemon.get(dexNumber);
    }
}
