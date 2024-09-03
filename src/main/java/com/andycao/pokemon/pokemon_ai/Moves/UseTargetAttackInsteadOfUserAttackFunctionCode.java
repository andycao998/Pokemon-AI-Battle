package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UseTargetAttackInsteadOfUserAttackFunctionCode extends Move {
    public int calculateTargetAttack(Pokemon moveTarget) {
        int unmodifiedAttack = moveTarget.getUnmodifiedAttack();
        int attackStage = moveTarget.getAttackStage();

        double modifier = 1.0;
        if (attackStage < 0) {
            modifier = (double) 2 / (2 + Math.abs(attackStage));
        }
        else {
            modifier = (double) (2 + Math.abs(attackStage)) / 2;
        }

        return (int) Math.floor(unmodifiedAttack * modifier);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        int userAttack = user.getCurrentAttack();
        // get item, ability

        int effectiveAttack = calculateTargetAttack(moveTarget); // * item * ability multipliers

        user.setCurrentAttack(effectiveAttack);
        BattleManager.getInstance().dealDamage(moveTarget, this);
        user.setCurrentAttack(userAttack);
    }
}
