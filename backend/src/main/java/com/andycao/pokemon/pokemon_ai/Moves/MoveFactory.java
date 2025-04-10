package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;

import java.util.Map;

public abstract class MoveFactory {
    private static Map<String, Move> moveFunctionCodes = Map.ofEntries(
        Map.entry("AddSpikesToFoeSide", new AddSpikesToFoeSideFunctionCode()),
        Map.entry("AddStealthRocksToFoeSide", new AddStealthRocksToFoeSideFunctionCode()),
        Map.entry("AddStickyWebToFoeSide", new AddStickyWebToFoeSideFunctionCode()),
        Map.entry("AddToxicSpikesToFoeSide", new AddToxicSpikesToFoeSideFunctionCode()),
        Map.entry("AlwaysCriticalHit", new AlwaysCriticalHitFunctionCode()),
        Map.entry("AttackerFaintsIfUserFaints", new AttackerFaintsIfUserFaintsFunctionCode()),
        Map.entry("BadPoisonTarget", new BadPoisonTargetFunctionCode()),
        Map.entry("BindTarget", new BindTargetFunctionCode()),
        Map.entry("BurnFlinchTarget", new BurnFlinchTargetFunctionCode()),
        Map.entry("BurnTarget", new BurnTargetFunctionCode()),
        Map.entry("CategoryDependsOnHigherDamageIgnoreTargetAbility", new CategoryDependsOnHigherDamageIgnoreTargetAbilityFunctionCode()),
        Map.entry("CategoryDependsOnHigherDamagePoisonTarget", new CategoryDependsOnHigherDamagePoisonTargetFunctionCode()),
        Map.entry("ConfuseTarget", new ConfuseTargetFunctionCode()),
        Map.entry("ConfuseTargetAlwaysHitsInRainHitsTargetInSky", new ConfuseTargetAlwaysHitsInRainHitsTargetInSkyFunctionCode()),
        Map.entry("CounterPhysicalDamage", new CounterPhysicalDamageFunctionCode()),
        Map.entry("CounterSpecialDamage", new CounterSpecialDamageFunctionCode()),
        Map.entry("CrashDamageIfFailsUnusableInGravity", new CrashDamageIfFailsUnusableInGravityFunctionCode()),
        Map.entry("CureTargetBurn", new CureTargetBurnFunctionCode()),
        Map.entry("CureUserPartyStatus", new CureUserPartyStatusFunctionCode()),
        Map.entry("CurseTargetOrLowerUserSpd1RaiseUserAtkDef1", new CurseTargetOrLowerUserSpd1RaiseUserAtkDef1FunctionCode()),
        Map.entry("DisableTargetSoundMoves", new DisableTargetSoundMovesFunctionCode()),
        Map.entry("DisableTargetStatusMoves", new DisableTargetStatusMovesFunctionCode()),
        Map.entry("DisableTargetUsingDifferentMove", new DisableTargetUsingDifferentMoveFunctionCode()),
        Map.entry("DoublePowerAfterFusionBolt", new DoublePowerAfterFusionBoltFunctionCode()),
        Map.entry("DoublePowerAfterFusionFlare", new DoublePowerAfterFusionFlareFunctionCode()),
        Map.entry("DoublePowerIfTargetActed", new DoublePowerIfTargetActedFunctionCode()),
        Map.entry("DoublePowerIfTargetLostHPThisTurn", new DoublePowerIfTargetLostHPThisTurnFunctionCode()),
        Map.entry("DoublePowerIfTargetNotActed", new DoublePowerIfTargetNotActedFunctionCode()),
        Map.entry("DoublePowerIfTargetStatus", new DoublePowerIfTargetStatusProblemFunctionCode()),
        Map.entry("DoublePowerIfTargetUnderground", new DoublePowerIfTargetUndergroundFunctionCode()),
        Map.entry("DoublePowerIfTargetUnderwater", new DoublePowerIfTargetUnderwaterFunctionCode()),
        Map.entry("DoublePowerIfUserHasNoItem", new DoublePowerIfUserHasNoItemFunctionCode()),
        Map.entry("DoublePowerIfUserLastMoveFailed", new DoublePowerIfUserLastMoveFailedFunctionCode()),
        Map.entry("DoublePowerIfUserLostHPThisTurn", new DoublePowerIfUserLostHPThisTurnFunctionCode()),
        Map.entry("DoublePowerIfUserPoisonedBurnedParalyzed", new DoublePowerIfUserPoisonedBurnedParalyzedFunctionCode()),
        Map.entry("DoublePowerIfUserStatsLoweredThisTurn", new DoublePowerIfUserStatsLoweredThisTurnFunctionCode()),
        Map.entry("FailsIfNotUserFirstTurn", new FailsIfNotUserFirstTurnFunctionCode()),
        Map.entry("FailsIfTargetActed", new FailsIfTargetActedFunctionCode()),
        Map.entry("FixedDamageHalfTargetHP", new FixedDamageHalfTargetHPFunctionCode()),
        Map.entry("FixedDamageUserLevel", new FixedDamageUserLevelFunctionCode()),
        Map.entry("FlinchTarget", new FlinchTargetFunctionCode()),
        Map.entry("FlinchTargetFailsIfNotUserFirstTurn", new FlinchTargetFailsIfNotUserFirstTurnFunctionCode()),
        Map.entry("FreezeFlinchTarget", new FreezeFlinchTargetFunctionCode()),
        Map.entry("FreezeTarget", new FreezeTargetFunctionCode()),
        Map.entry("FreezeTargetAlwaysHitsInHail", new FreezeTargetAlwaysHitsInHailFunctionCode()),
        Map.entry("FreezeTargetSuperEffectiveAgainstWater", new FreezeTargetSuperEffectiveAgainstWaterFunctionCode()),
        Map.entry("GrassPledge", new GrassPledgeFunctionCode()),
        Map.entry("HealAllyOrDamageFoe", new HealAllyOrDamageFoeFunctionCode()),
        Map.entry("HealUserAndAlliesQuarterOfTotalHPCureStatus", new HealUserAndAlliesQuarterOfTotalHPCureStatusFunctionCode()),
        Map.entry("HealUserByThreeQuartersOfDamageDone", new HealUserByThreeQuartersOfDamageDoneFunctionCode()),
        Map.entry("HealUserByHalfOfDamageDone", new HealUserByHalfOfDamageDoneFunctionCode()),
        Map.entry("HealUserByTargetAttackLowerTargetAttack1", new HealUserByTargetAttackLowerTargetAttack1FunctionCode()),
        Map.entry("HealUserDependingOnSandstorm", new HealUserDependingOnSandstormFunctionCode()),
        Map.entry("HealUserDependingOnWeather", new HealUserDependingOnWeatherFunctionCode()),
        Map.entry("HealUserFullyAndFallAsleep", new HealUserFullyAndFallAsleepFunctionCode()),
        Map.entry("HealUserHalfOfTotalHP", new HealUserHalfOfTotalHPFunctionCode()),
        Map.entry("HealUserHalfOfTotalHPLoseFlyingTypeThisTurn", new HealUserHalfOfTotalHPLoseFlyingTypeThisTurnFunctionCode()),
        Map.entry("HealUserPositionNextTurn", new HealUserPositionNextTurnFunctionCode()),
        Map.entry("HitsTargetInSkyGrounds", new HitsTargetInSkyGroundsTargetFunctionCode()),
        Map.entry("HitThreeTimesAlwaysCriticalHit", new HitThreeTimesAlwaysCriticalHitFunctionCode()),
        Map.entry("HitThreeTimesPowersUpWithEachHit", new HitThreeTimesPowersUpWithEachHitFunctionCode()),
        Map.entry("HitTwoTimes", new HitTwoTimesFunctionCode()),
        Map.entry("HitTwoTimesFlinchTarget", new HitTwoTimesFlinchTargetFunctionCode()),
        Map.entry("HitTwoToFiveTimes", new HitTwoToFiveTimesFunctionCode()),
        Map.entry("IgnoreTargetAbility", new IgnoreTargetAbilityFunctionCode()),
        Map.entry("IgnoreTargetDefSpDefEvaStatStages", new IgnoreTargetDefSpDefEvaStatStagesFunctionCode()),
        Map.entry("LowerTargetAccuracy1", new LowerTargetAccuracy1FunctionCode()),
        Map.entry("LowerTargetAtkSpAtk1SwitchOutUser", new LowerTargetAtkSpAtk1SwitchOutUserFunctionCode()),
        Map.entry("LowerTargetAttack1", new LowerTargetAttack1FunctionCode()),
        Map.entry("LowerTargetAttack2", new LowerTargetAttack2FunctionCode()),
        Map.entry("LowerTargetDefense1", new LowerTargetDefense1FunctionCode()),
        Map.entry("LowerTargetDefense1PowersUpInGravity", new LowerTargetDefense1PowersUpInGravityFunctionCode()),
        Map.entry("LowerTargetEvasion1RemoveSideEffects", new LowerTargetEvasion1RemoveSideEffectsFunctionCode()),
        Map.entry("LowerTargetSpAtk1", new LowerTargetSpAtk1FunctionCode()),
        Map.entry("LowerTargetSpDef1", new LowerTargetSpDef1FunctionCode()),
        Map.entry("LowerTargetSpeed1", new LowerTargetSpeed1FunctionCode()),
        Map.entry("LowerTargetSpeed2", new LowerTargetSpeed2FunctionCode()),
        Map.entry("LowerUserAtkDef1", new LowerUserAtkDef1FunctionCode()),
        Map.entry("LowerUserDefense1", new LowerUserDefense1FunctionCode()),
        Map.entry("LowerUserDefSpDef1", new LowerUserDefSpDef1FunctionCode()),
        Map.entry("LowerUserDefSpDef1RaiseUserAtkSpAtkSpd2", new LowerUserDefSpDef1RaiseUserAtkSpAtkSpd2FunctionCode()),
        Map.entry("LowerUserDefSpDefSpd1", new LowerUserDefSpDefSpd1FunctionCode()),
        Map.entry("LowerUserSpAtk2", new LowerUserSpAtk2FunctionCode()),
        Map.entry("LowerUserSpeed1", new LowerUserSpeed1FunctionCode()),
        Map.entry("MaxUserAttackLoseHalfOfTotalHP", new MaxUserAttackLoseHalfOfTotalHPFunctionCode()),
        Map.entry("MultiTurnAttackConfuseUserAtEnd", new MultiTurnAttackConfuseUserAtEndFunctionCode()),
        Map.entry("None", new NoFunctionCode()),
        Map.entry("ParalyzeBurnOrFreezeTarget", new ParalyzeBurnOrFreezeTargetFunctionCode()),
        Map.entry("ParalyzeFlinchTarget", new ParalyzeFlinchTargetFunctionCode()),
        Map.entry("ParalyzeTarget", new ParalyzeTargetFunctionCode()),
        Map.entry("ParalyzeTargetAlwaysHitsInRainHitsTargetInSky", new ParalyzeTargetAlwaysHitsInRainHitsTargetInSkyFunctionCode()),
        Map.entry("PoisonTarget", new PoisonTargetFunctionCode()),
        Map.entry("PowerHigherWithConsecutiveUse", new PowerHigherWithConsecutiveUseFunctionCode()),
        Map.entry("PowerHigherWithTargetWeight", new PowerHigherWithTargetWeightFunctionCode()),
        Map.entry("PowerHigherWithTargetFasterThanUser", new PowerHigherWithTargetFasterThanUserFunctionCode()),
        Map.entry("PowerHigherWithUserHeavierThanTarget", new PowerHigherWithUserHeavierThanTargetFunctionCode()),
        Map.entry("PowerHigherWithUserHP", new PowerHigherWithUserHPFunctionCode()),
        Map.entry("PowerHigherWithUserPositiveStatStages", new PowerHigherWithUserPositiveStatStagesFunctionCode()),
        Map.entry("ProtectUser", new ProtectUserFunctionCode()),
        Map.entry("ProtectUserBanefulBunker", new ProtectUserBanefulBunkerFunctionCode()),
        Map.entry("ProtectUserFromDamagingMovesKingsShield", new ProtectUserFromDamagingMovesKingsShieldFunctionCode()),
        Map.entry("ProtectUserFromTargetingMovesSpikyShield", new ProtectUserFromTargetingMovesSpikyShieldFunctionCode()),
        Map.entry("RaiseUserAtk1Spd2", new RaiseUserAtk1Spd2FunctionCode()),
        Map.entry("RaiseUserAtkAcc1", new RaiseUserAtkAcc1FunctionCode()),
        Map.entry("RaiseUserAtkDef1", new RaiseUserAtkDef1FunctionCode()),
        Map.entry("RaiseUserAtkSpd1", new RaiseUserAtkSpd1FunctionCode()),
        Map.entry("RaiseUserAtkDefAcc1", new RaiseUserAtkDefAcc1FunctionCode()),
        Map.entry("RaiseUserAttack1", new RaiseUserAttack1FunctionCode()),
        Map.entry("RaiseUserAttack2", new RaiseUserAttack2FunctionCode()),
        Map.entry("RaiseUserAttack3IfTargetFaints", new RaiseUserAttack3IfTargetFaintsFunctionCode()),
        Map.entry("RaiseUserDefense2", new RaiseUserDefense2FunctionCode()),
        Map.entry("RaiseUserDefense3", new RaiseUserDefense3FunctionCode()),
        Map.entry("RaiseUserDefSpDef1", new RaiseUserDefSpDef1FunctionCode()),
        Map.entry("RaiseUserMainStats1", new RaiseUserMainStats1FunctionCode()),
        Map.entry("RaiseUserMainStats1LoseThirdOfTotalHP", new RaiseUserMainStats1LoseThirdOfTotalHPFunctionCode()),
        Map.entry("RaiseUserMainStats1TrapUserInBattle", new RaiseUserMainStats1TrapUserInBattleFunctionCode()),
        Map.entry("RaiseUserSpAtk2", new RaiseUserSpAtk2FunctionCode()),
        Map.entry("RaiseUserSpAtk3", new RaiseUserSpAtk3FunctionCode()),
        Map.entry("RaiseUserSpAtkSpDef1", new RaiseUserSpAtkSpDef1FunctionCode()),
        Map.entry("RaiseUserSpAtkSpDefSpd1", new RaiseUserSpAtkSpDefSpd1FunctionCode()),
        Map.entry("RaiseUserSpeed1", new RaiseUserSpeed1FunctionCode()),
        Map.entry("RaiseUserSpeed2", new RaiseUserSpeed2FunctionCode()),
        Map.entry("RecoilHalfOfDamageDealt", new RecoilHalfOfDamageDealtFunctionCode()),
        Map.entry("RecoilQuarterOfDamageDealt", new RecoilQuarterOfDamageDealtFunctionCode()),
        Map.entry("RecoilThirdOfDamageDealt", new RecoilThirdOfDamageDealtFunctionCode()),
        Map.entry("RecoilThirdOfDamageDealtBurnTarget", new RecoilThirdOfDamageDealtBurnTargetFunctionCode()),
        Map.entry("RecoilThirdOfDamageDealtParalyzeTarget", new RecoilThirdOfDamageDealtParalyzeTargetFunctionCode()),
        Map.entry("RemoveScreens", new RemoveScreensFunctionCode()),
        Map.entry("RemoveTargetItem", new RemoveTargetItemFunctionCode()),
        Map.entry("RemoveUserBindingAndEntryHazards", new RemoveUserBindingAndEntryHazardsFunctionCode()),
        Map.entry("ResetAllBattlersStatStages", new ResetAllBattlersStatStagesFunctionCode()),
        Map.entry("ResetTargetStatStages", new ResetTargetStatStagesFunctionCode()),
        Map.entry("SleepTarget", new SleepTargetFunctionCode()),
        Map.entry("SleepTargetNextTurn", new SleepTargetNextTurnFunctionCode()),
        Map.entry("StartHailWeather", new StartHailWeatherFunctionCode()),
        Map.entry("StartLeechSeedTarget", new StartLeechSeedTargetFunctionCode()),
        Map.entry("StartRainWeather", new StartRainWeatherFunctionCode()),
        Map.entry("StartSandstormWeather", new StartSandstormWeatherFunctionCode()),
        Map.entry("StartSlowerBattlersActFirst", new StartSlowerBattlersActFirstFunctionCode()),
        Map.entry("StartSunWeather", new StartSunWeatherFunctionCode()),
        Map.entry("StartWeakenDamageAgainstUserSideIfHail", new StartWeakenDamageAgainstUserSideIfHailFunctionCode()),
        Map.entry("StartWeakenPhysicalDamageAgainstUserSide", new StartWeakenPhysicalDamageAgainstUserSideFunctionCode()),
        Map.entry("StartWeakenSpecialDamageAgainstUserSide", new StartWeakenSpecialDamageAgainstUserSideFunctionCode()),
        Map.entry("SwapSideEffects", new SwapSideEffectsFunctionCode()),
        Map.entry("SwitchOutTargetDamagingMove", new SwitchOutTargetDamagingMoveFunctionCode()),
        Map.entry("SwitchOutTargetStatusMove", new SwitchOutTargetStatusMoveFunctionCode()),
        Map.entry("SwitchOutUserDamagingMove", new SwitchOutUserDamagingMoveFunctionCode()),
        Map.entry("SwitchOutUserStatusMove", new SwitchOutUserStatusMoveFunctionCode()),
        Map.entry("TrapTargetInBattle", new TrapTargetInBattleFunctionCode()),
        Map.entry("TrapUserAndTargetInBattle", new TrapUserAndTargetInBattleFunctionCode()),
        Map.entry("TwoTurnAttackInvulnerableInSky", new TwoTurnAttackInvulnerableInSkyFunctionCode()),
        Map.entry("TwoTurnAttackInvulnerableInSkyParalyzeTarget", new TwoTurnAttackInvulnerableInSkyParalyzeTargetFunctionCode()),
        Map.entry("TwoTurnAttackOneTurnInSun", new TwoTurnAttackOneTurnInSunFunctionCode()),
        Map.entry("TwoTurnAttackRaiseUserSpAtkSpDefSpd2", new TwoTurnAttackRaiseUserSpAtkSpDefSpd2FunctionCode()),
        Map.entry("TypeDependsOnUserDrive", new TypeDependsOnUserDriveFunctionCode()),
        Map.entry("Typeless", new TypelessFunctionCode()),
        Map.entry("UseRandomUserMoveIfAsleep", new UseRandomUserMoveIfAsleepFunctionCode()),
        Map.entry("UserFaintsExplosive", new UserFaintsExplosiveFunctionCode()),
        Map.entry("UserFaintsHealAndCureReplacement", new UserFaintsHealAndCureReplacementFunctionCode()),
        Map.entry("UserFaintsLowerTargetAtkSpAtk2", new UserFaintsLowerTargetAtkSpAtk2FunctionCode()),
        Map.entry("UserMakeSubstitute", new UserMakeSubstituteFunctionCode()),
        Map.entry("UserStealTargetPositiveStatStages", new UserStealTargetPositiveStatStagesFunctionCode()),
        Map.entry("UserTargetAverageHP", new UserTargetAverageHPFunctionCode()),
        Map.entry("UseTargetAttackInsteadOfUserAttack", new UseTargetAttackInsteadOfUserAttackFunctionCode()),
        Map.entry("UseTargetDefenseInsteadOfTargetSpDef", new UseTargetDefenseInsteadOfTargetSpDefFunctionCode()),
        Map.entry("UseUserDefenseInsteadOfUserAttack", new UseUserDefenseInsteadOfUserAttackFunctionCode())
    );

    private static MoveDto getAttributes(String moveName) throws InvalidIdentifierException {
        MoveMapper data = new MoveMapper();
        return data.getMoveAttributes(moveName);
    }

    public static Move generateMove(String moveName) {
        if (moveName.equals("SWITCH")) {
            return new NullMove();
        }

        if (moveName.equals("")) {
            return new NullMove();
        }

        MoveDto attributes;
        try {
            attributes = getAttributes(moveName);
        }
        catch (InvalidIdentifierException e) {
            return new NullMove();
        }

        Move move = moveFunctionCodes.get(attributes.functionCode);
        // System.out.println("Move: " + moveName);

        // For moves that aren't yet implemented
        if (move == null) {
            move = new NullMove();
        }

        move.setId(attributes.moveName);
        move.setName(attributes.displayName);
        move.setType(attributes.type);
        move.setCategory(attributes.category);
        move.setPower(Integer.valueOf(attributes.power));
        move.setAccuracy(Integer.valueOf(attributes.accuracy));
        move.setTotalPP(Integer.valueOf(attributes.totalPP));
        move.setTarget(attributes.target);
        move.setPriority(Integer.valueOf(attributes.priority));
        move.setFunctionCode(attributes.functionCode);
        move.setFlags(attributes.flags);
        move.setEffectChance(Integer.valueOf(attributes.effectChance));
        return move;
    }
}
