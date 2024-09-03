package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UserStealTargetPositiveStatStagesFunctionCode extends Move {
    private void stealStatBoosts(Pokemon target, Pokemon user) {
        int attackStage = target.getAttackStage();
        int defenseStage = target.getDefenseStage();
        int spAttackStage = target.getSpAttackStage();
        int spDefenseStage = target.getSpDefenseStage();
        int speedStage = target.getSpeedStage();
        int accuracyStage = target.getAccuracyStage();
        int evasionStage = target.getEvasionStage();

        boolean statsStolen = false;

        if (attackStage > 0) {
            target.updateAttackStage(attackStage * -1, user, false);
            user.updateAttackStage(attackStage, user, false);
            statsStolen = true;
        }
        if (defenseStage > 0) {
            target.updateDefenseStage(defenseStage * -1, user, false);
            user.updateDefenseStage(defenseStage, user, false);
            statsStolen = true;
        }
        if (spAttackStage > 0) {
            target.updateSpAttackStage(spAttackStage * -1, user, false);
            user.updateSpAttackStage(spAttackStage, user, false);
            statsStolen = true;
        }
        if (spDefenseStage > 0) {
            target.updateSpDefenseStage(spDefenseStage * -1, user, false);
            user.updateSpDefenseStage(spDefenseStage, user, false);
            statsStolen = true;
        }
        if (speedStage > 0) {
            target.updateSpeedStage(speedStage * -1, user, false);
            user.updateSpeedStage(speedStage, user, false);
            statsStolen = true;
        }
        if (accuracyStage > 0) {
            target.updateAccuracyStage(accuracyStage * -1, user, false);
            user.updateAccuracyStage(accuracyStage, user, false);
            statsStolen = true;
        }
        if (evasionStage > 0) {
            target.updateEvasionStage(evasionStage * -1, user, false);
            user.updateEvasionStage(evasionStage, user, false);
            statsStolen = true;
        }

        if (statsStolen) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " stole " + target.getName() + "'s stat boosts!");
        }
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        stealStatBoosts(moveTarget, user);

        BattleManager.getInstance().dealDamage(moveTarget, this);
    }
}
