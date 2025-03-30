package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class StartSandstormWeatherFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getWeather().equals("Sandstorm")) {
            TurnEventMessageBuilder.getInstance().appendEvent("A sandstorm is already active!");
            return;
        }

        BattleManager.getInstance().setWeather("Sandstorm", 5);
    }
}
