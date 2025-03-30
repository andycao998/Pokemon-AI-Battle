package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class AccuracyHandler {
    private int calculateModifiedAccuracy(int moveAccuracy, int userAccuracyStages, int targetEvasionStages) {
        int adjustedStages = targetEvasionStages - userAccuracyStages; // Accuracy modifier from stages is difference between target evasion and user accuracy
        
        double accuracyMultiplier = 1.0;
        if (adjustedStages < 0) {
            accuracyMultiplier = 3 / (3 + Math.abs(adjustedStages)); // 3/9 min modifier
        }
        else {
            accuracyMultiplier = (3 + Math.abs(adjustedStages)) / 3; // 9/3 max modifier
        }


        double modifier = getModifiers();
        return (int) Math.floor(moveAccuracy * modifier * accuracyMultiplier);
    }

    // Account for ability, move, and field effects on accuracy like Hustle (currently none implemented)
    private double getModifiers() {
        return 1.0;
    }

    public boolean moveHit(int moveAccuracy, Pokemon moveUser, Pokemon moveTarget) {
        int accuracy = calculateModifiedAccuracy(moveAccuracy, moveUser.getAccuracyStage(), moveTarget.getEvasionStage());

        return EffectChanceRandomizer.evaluate(accuracy, 100); // Roll for final result on hit or miss
    }

    public boolean passAccuracyCheck(Move move, Pokemon user, Pokemon target) {
        // Moves denoted with 0 accuracy mean they will never miss unless the target is invulnerable (such as during Fly or Bounce)
        if (move.getAccuracy() == 0) {
            return true;
        }

        // The first turn of a two-turn attack still uses the move's overall accuracy. Override to be guaranteed to pass (second turn attack will still have accuracy)
        if (move.getFunctionCode().contains("TwoTurnAttack") && !user.lockedIntoMove()) {
            return true;
        }

        return moveHit(move.getAccuracy(), user, target);
    }
}
