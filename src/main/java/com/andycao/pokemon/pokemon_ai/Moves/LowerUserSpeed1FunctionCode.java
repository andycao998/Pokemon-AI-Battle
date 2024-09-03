package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class LowerUserSpeed1FunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        BattleManager.getInstance().dealDamage(moveTarget, this);

        user.updateSpeedStage(-1, user, false);
    }
}