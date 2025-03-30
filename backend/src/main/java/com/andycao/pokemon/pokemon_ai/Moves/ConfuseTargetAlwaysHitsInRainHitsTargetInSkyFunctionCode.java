package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class ConfuseTargetAlwaysHitsInRainHitsTargetInSkyFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        String weather = BattleManager.getInstance().getWeather();
        if (weather.equals("Rain") || weather.equals("Heavy Rain")) {
            setAccuracy(0);
        }
        else if (weather.equals("Harsh Sunlight") || weather.equals("Extremely Harsh Sunlight")) {
            setAccuracy(50);
        }

        // If target in sky, still hit

        BattleManager.getInstance().dealDamage(moveTarget, this);

        ConfuseTargetFunctionCode confuseFunction = new ConfuseTargetFunctionCode();
        confuseFunction.confuse(moveTarget, getEffectChance(), false);
    }
}
