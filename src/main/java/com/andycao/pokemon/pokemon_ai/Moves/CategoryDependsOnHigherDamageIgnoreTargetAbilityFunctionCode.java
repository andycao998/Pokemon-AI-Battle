package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class CategoryDependsOnHigherDamageIgnoreTargetAbilityFunctionCode extends Move {
    public int calculateUserAttack(Pokemon user) {
        int unmodifiedAttack = user.getUnmodifiedAttack();
        int attackStage = user.getAttackStage();

        double modifier = 1.0;
        if (attackStage < 0) {
            modifier = (double) 2 / (2 + Math.abs(attackStage));
        }
        else {
            modifier = (double) (2 + Math.abs(attackStage)) / 2;
        }

        return (int) Math.floor(unmodifiedAttack * modifier);
    }

    public int calculateUserSpAttack(Pokemon user) {
        int unmodifiedSpAttack = user.getUnmodifiedSpAttack();
        int spAttackStage = user.getSpAttackStage();

        double modifier = 1.0;
        if (spAttackStage < 0) {
            modifier = (double) 2 / (2 + Math.abs(spAttackStage));
        }
        else {
            modifier = (double) (2 + Math.abs(spAttackStage)) / 2;
        }

        return (int) Math.floor(unmodifiedSpAttack * modifier);
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        int attack = calculateUserAttack(user);
        int spAttack = calculateUserSpAttack(user);

        if (attack > spAttack) {
            setCategory("Physical");
            TurnEventMessageBuilder.getInstance().appendEvent("Photon Geyser became physical!");
        }
        else {
            setCategory("Special");
            TurnEventMessageBuilder.getInstance().appendEvent("Photon Geyser became special!");
        }

        String targetAbility = moveTarget.getCurrentAbility();

        moveTarget.setCurrentAbility("None");
        BattleManager.getInstance().dealDamage(moveTarget, this);
        moveTarget.setCurrentAbility(targetAbility);
    }
}
