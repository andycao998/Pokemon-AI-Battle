package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class CategoryDependsOnHigherDamagePoisonTargetFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        CategoryDependsOnHigherDamageIgnoreTargetAbilityFunctionCode categoryCalcsFunction = new CategoryDependsOnHigherDamageIgnoreTargetAbilityFunctionCode();

        int attack = categoryCalcsFunction.calculateUserAttack(user);
        int spAttack = categoryCalcsFunction.calculateUserSpAttack(user);

        if (attack > spAttack) {
            setCategory("Physical");
            TurnEventMessageBuilder.getInstance().appendEvent("Shell Side Arm became physical!");
        }
        else {
            setCategory("Special");
            TurnEventMessageBuilder.getInstance().appendEvent("Shell Side Arm became special!");
        }

        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);

        if (damage == 0) {
            return;
        }

        PoisonTargetFunctionCode poisonFunction = new PoisonTargetFunctionCode();
        poisonFunction.poison(moveTarget, getEffectChance(), false);
    }
}
