package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class RaiseUserAttack3IfTargetFaintsFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        if (moveTarget.getStatus().equals("Fainted")) {
            Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
            user.updateAttackStage(3, user, false);
        }
    }
}
