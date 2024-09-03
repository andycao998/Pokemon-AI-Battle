package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class CounterPhysicalDamageFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);

        if (!BattleManager.getInstance().getPokemonLastMove(moveTarget).getCategory().equals("Physical")) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " didn't take any physical damage!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        int lostHpPhysical = user.getLostHpThisTurn();
        if (lostHpPhysical == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " didn't take any damage!");
            BattleManager.getInstance().setPokemonLastMoveFailed(moveTarget, true);
            return;
        }

        BattleManager.getInstance().dealDirectDamage(moveTarget, this, lostHpPhysical * 2);
        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " countered the attack!");
    }
}
