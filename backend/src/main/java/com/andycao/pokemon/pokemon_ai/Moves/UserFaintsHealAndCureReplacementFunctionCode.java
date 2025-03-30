package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class UserFaintsHealAndCureReplacementFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        moveTarget.setCurrentHp(0);
        moveTarget.setFainted();
        BattleManager.getInstance().setHealingWish(moveTarget, true);
        
        TurnEventMessageBuilder.getInstance().appendEvent(moveTarget.getName() + " fainted to cast a healing wish!");
    }
}
