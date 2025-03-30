package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import org.apache.commons.lang3.tuple.Pair;

public class ProtectUserFunctionCode extends Move {
    private boolean calculateProtectionSuccess(int turnsSuccessful) {
        int range = (int) Math.pow(3, turnsSuccessful);

        if (EffectChanceRandomizer.evaluate(1, range)) {
            return true;
        }

        return false;
    }

    public void protect(Pokemon user, String type) {
        Pair<Move, Integer> targetMoveChoice = BattleManager.getInstance().getPokemonMoveChoice(user);
        int order = targetMoveChoice.getValue();

        if (order != 0) { // Going second
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " has nothing to defend against!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        int turnsSuccessful = user.getProtectionTurnsSuccessful();
        if (calculateProtectionSuccess(turnsSuccessful)) {
            user.setProtection(type, turnsSuccessful + 1);
        }
        else {
            user.setProtection("None", 0);
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " failed to protect itself!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
        }
    }   

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        protect(moveTarget, "Protect");
    }
}
