package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class CrashDamageIfFailsUnusableInGravityFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (moveTarget.getStatus().equals("Fainted")) {
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        int damage = BattleManager.getInstance().dealDamage(moveTarget, this);

        if (damage == 0) {
            user.receiveDamage((int) Math.floor((double) user.getMaxHp() / 2), user);
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " crash landed and took damage!");
        }
    }
}
