package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StartSunWeatherFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getWeather().equals("Harsh Sunlight")) {
            TurnEventMessageBuilder.getInstance().appendEvent("The sun is already harsh!");
            return;
        }

        BattleManager.getInstance().setWeather("Harsh Sunlight", 5);
    }
}
