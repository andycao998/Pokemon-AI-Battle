package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;

public class NoFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        BattleManager.getInstance().dealDamage(moveTarget, this);
        //getAttributes(moveName);
        //TurnEventMessageBuilder.getInstance().appendEvent();
    }
    
    // private void getAttributes(String moveName) throws InvalidIdentifierException {
    //     MoveMapper data = new MoveMapper();
    //     MoveDto attributes = data.getMoveAttributes(moveName);

    //     super.setName(attributes.displayName);
    //     super.setType(attributes.type);
    //     super.setCategory(attributes.category);
    //     super.setPower(Integer.valueOf(attributes.power));
    //     super.setAccuracy(Integer.valueOf(attributes.accuracy));
    //     super.setPriority(Integer.valueOf(attributes.priority));
    //     super.setFunctionCode(attributes.functionCode);
    //     super.setFlags(attributes.flags);
    //     super.setEffectChance(Integer.valueOf(attributes.effectChance));
    // }
}
