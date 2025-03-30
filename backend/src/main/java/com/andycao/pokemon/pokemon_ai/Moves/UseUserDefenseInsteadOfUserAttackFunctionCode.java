package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;

public class UseUserDefenseInsteadOfUserAttackFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        int userAttackStages = user.getAttackStage();
        int userAttack = user.getCurrentAttack();

        user.updateAttackStage(user.getDefenseStage(), user, true);
        user.setCurrentAttack(user.getCurrentDefense());
        BattleManager.getInstance().dealDamage(moveTarget, this);
        user.setCurrentAttack(userAttack);
        user.updateAttackStage(userAttackStages, user, true);
    }
}
