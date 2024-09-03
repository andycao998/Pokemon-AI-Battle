package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class HealUserByTargetAttackLowerTargetAttack1FunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (moveTarget.getAttackStage() == -6) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + "'s attack cannot be lowered further!");
            return;
        }

        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        UseTargetAttackInsteadOfUserAttackFunctionCode targetAttack = new UseTargetAttackInsteadOfUserAttackFunctionCode();
        int heal = targetAttack.calculateTargetAttack(moveTarget);
        moveTarget.updateAttackStage(-1, user, false);

        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " healed from " + moveTarget.getName() + "'s attack stat!");
        user.receiveHealing(heal);
    }
}
