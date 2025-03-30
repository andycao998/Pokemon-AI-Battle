package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class TwoTurnAttackRaiseUserSpAtkSpDefSpd2FunctionCode extends Move {
    public void firstTurnCharge(Pokemon target, Pokemon user, String moveId) {
        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " is charging a move using energy around it!");

        user.setLockedMove(moveId, 1, 100, -1);
    }

    public void secondTurnBuff(Pokemon target, Pokemon user, Move move) {
        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " absorbed the energy, boosting its stats!");

        user.updateSpAttackStage(2, user, false);
        user.updateSpDefenseStage(2, user, false);
        user.updateSpeedStage(2, user, false);

        user.setLockedMove("", user.getLockedMove().getMiddle(), 0, -1);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (!user.lockedIntoMove()) {
            firstTurnCharge(moveTarget, user, getId());
        }
        else if (user.getLockedMove().getLeft().equals(getId())) {
            secondTurnBuff(moveTarget, user, this);
        }
    }
}
