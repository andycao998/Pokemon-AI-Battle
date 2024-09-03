package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class TwoTurnAttackInvulnerableInSkyFunctionCode extends Move {
    public void firstTurnSky(Pokemon target, Pokemon user, String moveId) {
        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " ascended into the sky!");
        
        user.setLockedMove(moveId, 1, 100, -1);
        user.setInvulnerable("Sky");
    }

    public void secondTurnSky(Pokemon target, Pokemon user, Move move) {
        int damage = BattleManager.getInstance().dealDamage(target, move);

        if (damage == 0) {
            user.interruptMultiTurnMove();
        }

        user.setInvulnerable("");
        user.setLockedMove("", user.getLockedMove().getMiddle(), 0, -1);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (!user.lockedIntoMove()) {
            firstTurnSky(moveTarget, user, getId());
        }
        else if (user.getLockedMove().getLeft().equals(getId())) {
            secondTurnSky(moveTarget, user, this);
        }
    }
}
