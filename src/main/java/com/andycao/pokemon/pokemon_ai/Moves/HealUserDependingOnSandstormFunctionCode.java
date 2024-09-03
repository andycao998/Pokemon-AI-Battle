package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HealUserDependingOnSandstormFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        String activeWeather = BattleManager.getInstance().getWeather();

        if (activeWeather.equals("Sandstorm")) {
            moveTarget.receiveHealing((int) Math.floor((double) (moveTarget.getMaxHp() * 2) / 3));
        }
        else {
            moveTarget.receiveHealing((int) Math.floor((double) moveTarget.getMaxHp() / 2));
        }
    }
}
