package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class MaxUserAttackLoseHalfOfTotalHPFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        int halfHp = (int) Math.floor((double) moveTarget.getMaxHp() / 2);
        if (moveTarget.getCurrentHp() < halfHp) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " was too weak to use Belly Drum!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        if (moveTarget.getAttackStage() == 6) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " already maximized its attack!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        moveTarget.receiveDamage(halfHp, moveTarget);
        moveTarget.updateAttackStage(12, moveTarget, false);

        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " cut down on its HP to maximize its attack!");
    }
}
