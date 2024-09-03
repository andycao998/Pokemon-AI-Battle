package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class PowerHigherWithConsecutiveUseFunctionCode extends Move {
    private static int playerPower;
    private static int botPower;

    public void setPowerBasedOnConsecutiveUse(Pokemon user) {
        Move lastMove = BattleManager.getInstance().getPokemonLastMove(user);

        if (lastMove.equals(this) && getMovePower(user) < 160) {
            TurnEventMessageBuilder.getInstance().appendEvent(getName() + " doubled in power!");
            setMovePower(user, getMovePower(user) * 2);
        }
        else if (lastMove.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(getName() + " is at its max power!");
        }
        else {
            setMovePower(user, 40);
        }

        setPower(getMovePower(user));
    }

    private int getMovePower(Pokemon pokemon) {
        if (pokemon.equals(BattleManager.getInstance().getPlayerPokemon())) {
            return playerPower;
        }
        else {
            return botPower;
        }
    }

    private void setMovePower(Pokemon pokemon, int power) {
        if (pokemon.equals(BattleManager.getInstance().getPlayerPokemon())) {
            playerPower = power;
        }
        else {
            botPower = power;
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        setPowerBasedOnConsecutiveUse(user);

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
