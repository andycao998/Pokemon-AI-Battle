package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HealUserDependingOnWeatherFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        String activeWeather = BattleManager.getInstance().getWeather();

        if (activeWeather.isEmpty() || activeWeather.equals("Strong Winds")) {
            moveTarget.receiveHealing((int) Math.floor((double) moveTarget.getMaxHp() / 2));
        }
        else if (activeWeather.equals("Harsh Sunlight") || activeWeather.equals("Extremely Harsh Sunlight")) {
            moveTarget.receiveHealing((int) Math.floor((double) (2 * moveTarget.getMaxHp()) / 3));
        }
        else {
            moveTarget.receiveHealing((int) Math.floor((double) moveTarget.getMaxHp() / 4));
        }
    }
}
