package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StartRainWeatherFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getWeather().equals("Rain")) {
            TurnEventMessageBuilder.getInstance().appendEvent("It is already raining!");
            return;
        }

        BattleManager.getInstance().setWeather("Rain", 5);
    }
}
