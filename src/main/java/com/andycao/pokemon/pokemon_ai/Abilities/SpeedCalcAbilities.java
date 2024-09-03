package com.andycao.pokemon.pokemon_ai.Abilities;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class SpeedCalcAbilities {
    public static double execute(Pokemon user, Move move) {
        String ability = user.getCurrentAbility();

        double multiplier = 1.0;

        multiplier = swiftSwim(ability, user);
        
        return multiplier;
    }

    private static double swiftSwim(String ability, Pokemon user) {
        String activeWeather = BattleManager.getInstance().getWeather();
        if (!activeWeather.equals("Rain") && !activeWeather.equals("Heavy Rain")) {
            return 1.0;
        }

        if (ability.equals("Swift Swim")) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " is faster thanks to Swift Swim!");
            return 2.0;
        }

        return 1.0;
    }
}
