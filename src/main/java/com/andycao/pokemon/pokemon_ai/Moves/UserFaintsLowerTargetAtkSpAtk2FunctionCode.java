package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UserFaintsLowerTargetAtkSpAtk2FunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        if (BattleManager.getInstance().getPokemonSubstitute(moveTarget) > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is protected from Memento with its substitute!");
            return;
        }

        if (moveTarget.getProtection().equals("ProtectUser")) {
            TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " is protected from Memento!");
            return;
        }

        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        user.setCurrentHp(0);
        user.setFainted();
        moveTarget.updateAttackStage(-2, user, false);
        moveTarget.updateSpAttackStage(-2, user, false);

        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " fainted to drop the stats of " + moveTarget.getName() + "!");
    }
}
