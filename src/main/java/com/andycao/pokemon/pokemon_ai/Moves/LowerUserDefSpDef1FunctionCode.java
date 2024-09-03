package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class LowerUserDefSpDef1FunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);

        if (damage > 0) {
            user.updateDefenseStage(-1, user, false);
            user.updateSpDefenseStage(-1, user, false);
        }
    }
}