package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class CounterSpecialDamageFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (!BattleManager.getInstance().getPokemonLastMove(moveTarget).getCategory().equals("Special")) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " didn't take any special damage!");
            return;
        }

        int lostHpSpecial = user.getLostHpThisTurn();
        if (lostHpSpecial == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " didn't take any damage!");
            return;
        }

        BattleManager.getInstance().dealDirectDamage(moveTarget, this, lostHpSpecial * 2);
        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " mirrored the attack!");
    }
}
