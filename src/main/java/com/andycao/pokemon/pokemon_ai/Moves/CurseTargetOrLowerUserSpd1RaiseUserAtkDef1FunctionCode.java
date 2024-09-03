package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class CurseTargetOrLowerUserSpd1RaiseUserAtkDef1FunctionCode extends Move {
    private void raiseStats(Pokemon moveTarget) {
        moveTarget.updateSpeedStage(-1, moveTarget, false);
        moveTarget.updateAttackStage(1, moveTarget, false);
        moveTarget.updateDefenseStage(1, moveTarget, false);
    }

    private void curseTarget(Pokemon user, Pokemon opposing) {
        if (opposing.getCursed()) {
            TurnEventMessageBuilder.getInstance().appendEvent(opposing.getName() + " is already cursed!");
            return;
        }

        user.receiveDamage((int) Math.floor((double) user.getMaxHp() / 2), user);
        opposing.setCursed(true);

        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " used its HP to lay a curse on " + opposing.getName() + "!");
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon opposing = BattleManager.getInstance().getOpposing(moveTarget);

        if (opposing.getStatus().equals("Fainted")) {
            TurnEventMessageBuilder.getInstance().appendEvent("Curse failed without a target!");
            return;
        }

        if (moveTarget.getType1().equals("Ghost") || moveTarget.getType2().equals("Ghost")) {
            curseTarget(moveTarget, opposing);
        }
        else {
            raiseStats(moveTarget);
        }
    }
}
