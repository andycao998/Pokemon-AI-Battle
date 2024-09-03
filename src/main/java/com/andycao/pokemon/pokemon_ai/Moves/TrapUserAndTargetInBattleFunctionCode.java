package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class TrapUserAndTargetInBattleFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        TrapTargetInBattleFunctionCode trapTargetInBattle = new TrapTargetInBattleFunctionCode();
        trapTargetInBattle.trapTarget(moveTarget);

        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        trapTargetInBattle.trapTarget(user);
    }
}
