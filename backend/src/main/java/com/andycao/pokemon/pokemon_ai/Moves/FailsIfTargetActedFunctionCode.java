package com.andycao.pokemon.pokemon_ai.Moves;

import org.apache.commons.lang3.tuple.Pair;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class FailsIfTargetActedFunctionCode extends Move {
    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        Pokemon user = BattleManager.getInstance().getOpposing(moveTarget);
        Pair<Move, Integer> targetMoveChoice = BattleManager.getInstance().getPokemonMoveChoice(moveTarget);
        Move targetMove = targetMoveChoice.getKey();
        int order = targetMoveChoice.getValue();

        if (order != 1 || targetMove.getCategory().equals("Status")) { // Going second
            TurnEventMessageBuilder.getInstance().appendEvent("But the attack failed!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        BattleManager.getInstance().dealDamage(moveTarget, this);

        // if (moveTarget.equals(BattleManager.getInstance().getPlayerPokemon())) {
        //     Pair<Move, Integer> playerMoveChoice = BattleManager.getInstance().getPlayerMoveChoice();
        //     Move playerMove = playerMoveChoice.getKey();
        //     int order = playerMoveChoice.getValue();

        //     if (order != 1) { // Going second
        //         TurnEventMessageBuilder.getInstance().appendEvent("But it failed!");
        //         return;
        //     }

        //     if (playerMove.getCategory().equals("Physical") || playerMove.getCategory().equals("Special")) {
        //         BattleManager.getInstance().dealDamage(moveTarget, this);
        //     }
        //     else {
        //         TurnEventMessageBuilder.getInstance().appendEvent("But it failed!");
        //     }
        // }
        // else if (moveTarget.equals(BattleManager.getInstance().getBotPokemon())) {
        //     Pair<Move, Integer> botMoveChoice = BattleManager.getInstance().getBotMoveChoice();
        //     Move botMove = botMoveChoice.getKey();
        //     int order = botMoveChoice.getValue();

        //     if (order != 1) {
        //         TurnEventMessageBuilder.getInstance().appendEvent("But it failed!");
        //         return;
        //     }

        //     if (botMove.getCategory().equals("Physical") || botMove.getCategory().equals("Special")) {
        //         BattleManager.getInstance().dealDamage(moveTarget, this);
        //     }
        //     else {
        //         TurnEventMessageBuilder.getInstance().appendEvent("But it failed!");
        //     }
        // }
    }
}
