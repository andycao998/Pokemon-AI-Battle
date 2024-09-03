package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class BindTargetFunctionCode extends Move {
    public void bindTarget(Pokemon moveTarget) {
        // if (moveTarget.getType1().equals("Ghost") || moveTarget.getType2().equals("Ghost")) {
        //     return;
        // }

        // if (moveTarget.getCurrentAbility().equals("Run Away")) {
        //     return;
        // }

        if (moveTarget.getBoundTurns() > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is already bound!");
            BattleManager.getInstance().setPokemonLastMoveFailed(BattleManager.getInstance().getOpposing(moveTarget), true);
            return;
        }

        int boundTurns = 0;
        if (EffectChanceRandomizer.evaluate(1, 2)) {
            boundTurns = 4;
        }
        else {
            boundTurns = 5;
        }

        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        moveTarget.setBound(user, boundTurns);
        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " was bound by " + getName() + "!");
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);

        bindTarget(moveTarget);
    }
}
