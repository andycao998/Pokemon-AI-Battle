package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Abilities.CriticalDamageCalcAbilities;
import com.andycao.pokemon.pokemon_ai.Abilities.DamageCalcTargetAbilities;
import com.andycao.pokemon.pokemon_ai.Abilities.OnHitTargetAbilities;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Moves.NullMove;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DamageHandler {
    private String activeWeather; // Used to keep track of whethers that affect damage multipliers: Harsh Sunlight (and Extremely Harsh Sunlight) and Rain (and Heavy Rain)

    private boolean critsEnabled;

    public DamageHandler() {
        activeWeather = "";
        critsEnabled = true;
    }

    /*----------Damage Calculation----------*/

    // Pokemon damage formula using up to Gen 8 mechanics (accounts for all currently implemented move/ability modifiers but not all possible)
    // Randomness is currently excluded for testing purposes
    private int calculateDamage(int power, int userEffectiveAttack, int targetEffectiveDefense, double stab, double typeEffectiveness, double crit, double burn, double weather) {
        // 22 denotes the multiplier used for level 50 Pokemon (set standard). Power is move power. STAB is same type attack bonus.
        // Due to the way damage is implemented in the main games, the calculations are floored to truncate to integers
        double damage = Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(22 * power * ((double) userEffectiveAttack / targetEffectiveDefense)) / 50) + 2) * weather) * crit) * stab) * typeEffectiveness) * burn);
        BigDecimal roundedDamage = new BigDecimal(damage).setScale(2, RoundingMode.HALF_DOWN); // Cut off at two decimal places and round half down

        return roundedDamage.toBigInteger().intValue(); // Convert to int: damage is represented with integers only
    }

    public int dealDamage(Pokemon target, Move move) {
        Pokemon user = BattleManager.getInstance().getOpposing(target);

        int damage = 0;
        double stab = 1.0;
        double effectiveness = getTypeEffectiveness(target, move);
        double critMultiplier = getCriticalHitMultiplier(user, move);
        double weatherMultiplier = getWeatherMultiplier(move);

        // Strong Winds alters type chart for Flying-types
        if (activeWeather.equals("Strong Winds")) {
            effectiveness = recalculateStrongWindsEffectiveness(effectiveness, target, activeWeather);
        }

        // Keep track of stat stages during a successful critical hit (user's negative attacking stat stages are ignored and target's positive defensive stat stages are ignored)
        int userAttackStages = user.getAttackStage();
        int userSpAttackStages = user.getSpAttackStage();
        int targetDefenseStages = target.getDefenseStage();
        int targetSpDefenseStages = target.getSpDefenseStage();

        // STAB: Move used matches one of user's types and gets a 1.5x boost in damage. Adaptability modifies this to 2.0x (not yet implemented)
        if (user.getType1().toUpperCase().equals(move.getType()) || user.getType2().toUpperCase().equals(move.getType())) {
            stab = 1.5;
        }

        // If a critical hit happened ignore certain stat stages as mentioned above
        if (critMultiplier > 1.0) {
            if (userAttackStages < 0) {
                user.updateAttackStage(userAttackStages * -1, user, true);
            }
            if (userSpAttackStages < 0) {
                user.updateSpAttackStage(userSpAttackStages * -1, user, true);
            }
            if (targetDefenseStages > 0) {
                target.updateDefenseStage(targetDefenseStages * -1, target, true);
            }
            if (targetSpDefenseStages > 0) {
                target.updateSpDefenseStage(targetSpDefenseStages * -1, target, true);
            }
        }

        DamageCalcTargetAbilities.execute(target, move); // Account for abilities that modify damage before calculations like Thick Fat

        if (move.getCategory().equals("Physical")) {
            double burn = 1.0;
            if (user.getStatus().equals("Burn")) {
                burn = 0.5; // Burn is a flat half multiplier to Physical attacks under most circumstances
            }

            damage = calculateDamage(move.getPower(), user.getCurrentAttack(), target.getCurrentDefense(), stab, effectiveness, critMultiplier, burn, weatherMultiplier);

            // Reflect is physical multiplier that reduces damage by half if it is active for the target Pokemon's side. Aurora Veil functions the same
            if (BattleManager.getInstance().getReflect(target) || BattleManager.getInstance().getAuroraVeil(target)) {
                damage = (int) Math.floor((double) damage / 2);
            }
        }
        else {
            damage = calculateDamage(move.getPower(), user.getCurrentSpAttack(), target.getCurrentSpDefense(), stab, effectiveness, critMultiplier, 1.0, weatherMultiplier);

            // Reflect is physical multiplier that reduces damage by half if it is active for the target Pokemon's side. Aurora Veil functions the same
            if (BattleManager.getInstance().getLightScreen(target) || BattleManager.getInstance().getAuroraVeil(target)) {
                damage = (int) Math.floor((double) damage / 2);
            }
        }

        // Restore stat stages if a critical hit happened
        if (critMultiplier > 1.0) {
            user.updateAttackStage(userAttackStages, user, true);
            user.updateSpAttackStage(userSpAttackStages, user, true);
            target.updateDefenseStage(targetDefenseStages, target, true);
            target.updateSpDefenseStage(targetSpDefenseStages, target, true);
        }

        // If target has fainted before the move could be used, skip
        if (checkTargetFainted(user, target)) {
            return 0;
        }

        // Target receives damage and activates abilities that function when damage is taken like Flame Body
        target.receiveDamage(damage, user);
        OnHitTargetAbilities.execute(target, move);
        writeEffectiveness(effectiveness, weatherMultiplier, user);

        return damage; // Certain moves look at how much damage was done: return it
    }

    // A different category of damage that ignores most calculations and deals static damage (Ex: moves like Seismic Toss deal damage based on user level)
    public int dealDirectDamage(Pokemon target, Move move, int damage) {
        Pokemon user = BattleManager.getInstance().getOpposing(target);

        // A target that is immune to the type of the move still takes zero damage. Otherwise, resistances and weaknesses don't matter
        if (getTypeEffectiveness(target, move) == 0.0) {
            writeEffectiveness(0.0, 1.0, user);
            return 0;
        }
        
        if (checkTargetFainted(user, target)) {
            return 0;
        }

        target.receiveDamage(damage, user);
        OnHitTargetAbilities.execute(target, move);

        return damage;
    }

    // Category of damage reserved for unselectable moves/effects: Struggle (no usable moves due to PP or move blocking) and Confusion Damage (pokemon hit itself in confusion)
    public int dealTypelessDamage(Pokemon target, Move move) {
        Pokemon user = BattleManager.getInstance().getOpposing(target);

        int damage = 0;

        // These moves are typeless (neutral damage multiplier), can't critical hit, and don't receive STAB. Burn still applies to damage output
        if (move.getCategory().equals("Physical")) {
            double burn = calculateBurnMultiplier(user, move);

            if (move.getId().equals("STRUGGLE")) {
                damage = calculateDamage(move.getPower(), user.getCurrentAttack(), target.getCurrentDefense(), 1.0, 1.0, 1.0, burn, 1.0);
            }
            else if (move.getId().equals("CONFUSIONDAMAGE")) {
                damage = calculateDamage(move.getPower(), target.getCurrentAttack(), target.getCurrentDefense(), 1.0, 1.0, 1.0, burn, 1.0);
            }
        }
        // Currently unused implementation for Special moves in this category (Struggle and Confusion Damage are both Physical)
        else {
            damage = calculateDamage(move.getPower(), user.getCurrentSpAttack(), target.getCurrentSpDefense(), 1.0, 1.0, 1.0, 1.0, 1.0);
        }

        if (checkTargetFainted(user, target)) {
            return 0;
        }

        target.receiveDamage(damage, target);

        return damage;
    }

    private boolean checkTargetFainted(Pokemon user, Pokemon target) {
        if (target.getStatus().equals("Fainted")) {
            // TurnEventMessageBuilder.getInstance().appendEvent(target.getName() + " is already fainted!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent(target.getName() + " is already fainted!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return true;
        }

        return false;
    }

    /*----------Effectiveness Modifiers----------*/

    private double getTypeEffectiveness(Pokemon target, Move move) {
        double effectiveness = 1.0; // Standard neutral effectiveness

        // Depending on move type, get target Pokemon's specific defenses vs that type
        switch(move.getType()) {
            case "NORMAL":
                effectiveness = target.getDamageMultiplierFromNormal();
                break;
            case "FIRE":
                effectiveness = target.getDamageMultiplierFromFire();
                break;
            case "WATER":
                effectiveness = target.getDamageMultiplierFromWater();
                break;
            case "ELECTRIC":
                effectiveness = target.getDamageMultiplierFromElectric();
                break;
            case "GRASS":
                effectiveness = target.getDamageMultiplierFromGrass();
                break;
            case "ICE":
                effectiveness = target.getDamageMultiplierFromIce();
                break;
            case "FIGHTING":
                effectiveness = target.getDamageMultiplierFromFighting();
                break;
            case "POISON":
                effectiveness = target.getDamageMultiplierFromPoison();
                break;
            case "GROUND":
                effectiveness = target.getDamageMultiplierFromGround();
                break;
            case "FLYING":
                effectiveness = target.getDamageMultiplierFromFlying();
                break;
            case "PSYCHIC":
                effectiveness = target.getDamageMultiplierFromPsychic();
                break;
            case "BUG":
                effectiveness = target.getDamageMultiplierFromBug();
                break;
            case "ROCK":
                effectiveness = target.getDamageMultiplierFromRock();
                break;
            case "GHOST":
                effectiveness = target.getDamageMultiplierFromGhost();
                break;
            case "DRAGON":
                effectiveness = target.getDamageMultiplierFromDragon();
                break;
            case "DARK":
                effectiveness = target.getDamageMultiplierFromDark();
                break;
            case "STEEL":
                effectiveness = target.getDamageMultiplierFromSteel();
                break;
            case "FAIRY":
                effectiveness = target.getDamageMultiplierFromFairy();
                break;
            default:
                effectiveness = 1.0;
                break;
        }

        // Two scenarios where effectiveness is different depending on move/conditions
        effectiveness = freezeDryOnWaterEffectiveness(target, effectiveness, move);
        effectiveness = groundMovesOnGroundedFlyingEffectiveness(target, effectiveness, move);

        return effectiveness;
    }
    
    // Freeze-Dry deals super-effective damage to Water-types instead of being resisted
    private double freezeDryOnWaterEffectiveness(Pokemon target, double currentEffectiveness, Move move) {
        if (!move.containsFlag("HatesWater")) {
            return currentEffectiveness;
        }

        if (!target.getType1().equals("Water") && !target.getType2().equals("Water")) {
            return currentEffectiveness;
        }

        // A neutral hit becomes quad-effective (Ex: Water/Flying is originally neutral due to Water cancelling Flying's weakness to Ice, but now both types are weak to Ice)
        // A resisted hit becomes super-effective (Ex: Water/Fairy is originally resisted... Ice now hits Water super-effectively)
        // A quad-resisted hit becomes neutral (Ex: Water/Ice is originally quad-resisted... Water's weakness to Freeze-Dry now cancels out Ice's resistance to Ice)
        double newEffectiveness = 1.0;
        if (currentEffectiveness == 1.0) {
            newEffectiveness = 4.0;
        }
        else if (currentEffectiveness == 0.5) {
            newEffectiveness = 2.0;
        }
        else if (currentEffectiveness == 0.25) {
            newEffectiveness = 1.0;
        }

        return newEffectiveness;
    }

    // Flying Pokemon, if grounded, can be hit neutrally be Ground-types
    private double groundMovesOnGroundedFlyingEffectiveness(Pokemon target, double currentEffectiveness, Move move) {
        // Grounded effect usually doesn't apply to Flying-type Pokemon, Pokemon with Levitate, or Pokemon under the effects of Air Balloon or Magnet Rise
        // Certain moves can ground these Pokemon (without waiting for an effect like Magnet Rise to run out), such as Thousand Arrows or Gravity
        if (!target.getGrounded() && !move.getFunctionCode().contains("GroundsTarget")) {
            return currentEffectiveness;
        }

        if (!target.containsType("Flying")) {
            return currentEffectiveness;
        }

        // Types Ground is super-effective against
        boolean isFireType = target.containsType("Fire");
        boolean isElectricType = target.containsType("Electric");
        boolean isPoisonType = target.containsType("Poison");
        boolean isRockType = target.containsType("Rock");
        boolean isSteelType = target.containsType("Steel");

        // Types that resist Ground
        boolean isGrassType = target.containsType("Grass");
        boolean isBugType = target.containsType("Bug");

        double newEffectiveness = 1.0;
        // Target's non-Flying-type is weak to Ground -> super-effective damage
        if (isFireType || isElectricType || isPoisonType || isRockType || isSteelType) {
            newEffectiveness = 2.0;
        }
        // Target's non-Flying type resists Ground -> resisted damage
        else if (isGrassType || isBugType) {
            newEffectiveness = 0.5;
        }
        // Target's non-Flying type either doesn't exist or takes neutral damage from Ground -> neutral damage
        else {
            newEffectiveness = 1.0;
        }

        return newEffectiveness;
    }

    private double recalculateStrongWindsEffectiveness(double currentEffectiveness, Pokemon target, String moveType) {
        if (!target.getType1().equals("Flying") && !target.getType2().equals("Flying")) {
            return currentEffectiveness;
        }

        // Flying-type Pokemon lose their Flying weaknesses during Strong Winds
        if (!moveType.equals("ELECTRIC") && !moveType.equals("ICE") && !moveType.equals("ROCK")) {
            return currentEffectiveness;
        }

        BigDecimal effectiveness = new BigDecimal(currentEffectiveness);
        BigDecimal divisor = new BigDecimal(2.0);

        return effectiveness.divide(divisor).doubleValue();
    }

    // Display effectiveness text
    private void writeEffectiveness(double effectiveness, double weatherMultiplier, Pokemon user) {
        if (weatherMultiplier == 0.0) {
            if (activeWeather.equals("Extremely Harsh Sunlight")) {
                // TurnEventMessageBuilder.getInstance().appendEvent("The Water-type attack evaporated in the harsh sunlight!");
                BattleContextHolder.get().getTurnMessageHandler().appendEvent("The Water-type attack evaporated in the harsh sunlight!");
            }
            else {
                // TurnEventMessageBuilder.getInstance().appendEvent("The Fire-type attack fizzled out in the heavy rain!");
                BattleContextHolder.get().getTurnMessageHandler().appendEvent("The Fire-type attack fizzled out in the heavy rain!");
            }

            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);

            return;
        } 

        if (effectiveness >= 2.0) {
            // TurnEventMessageBuilder.getInstance().appendEvent("It's super effective!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("It's super effective!");
        } 
        else if (effectiveness == 0.0) {
            // TurnEventMessageBuilder.getInstance().appendEvent("It wasn't effective.");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("It wasn't effective.");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
        }
        else if (effectiveness <= 0.5) {
            // TurnEventMessageBuilder.getInstance().appendEvent("It's not very effective.");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("It's not very effective.");
        }
    }

    /*----------Other Modifiers----------*/
    public void setCriticalHitsEnabled(boolean state) {
        critsEnabled = state;
    }

    private double getCriticalHitMultiplier(Pokemon user, Move move) {
        if (!critsEnabled) {
            return 1.0;
        }

        // Moves can be denoted to have a higher crit chance (Ex: X-Scissor)
        if (move.containsFlag("HighCriticalHitRate")) {
            user.updateCriticalStage(1, true); // Update critical stages accordingly (+1 is 1/8 chance instead of 1/24)
        }
        // Or they can always be guaranteed to crit (Ex: Wicked Blow)
        else if (move.getFunctionCode().contains("AlwaysCriticalHit")) {
            user.updateCriticalStage(3, true);
        }

        int critChance = user.getCriticalChance();

        // Reset critical stages after move use as those effects only apply to that specific move
        if (move.containsFlag("HighCriticalHitRate")) {
            user.updateCriticalStage(-1, true);
        }
        else if (move.getFunctionCode().contains("AlwaysCriticalHit")) {
            user.updateCriticalStage(-3, true);
        }

        double critMultiplier = CriticalDamageCalcAbilities.execute(user, new NullMove()); // Abilities can modify the default crit damage boost of 1.5x

        // Roll crit success
        if (EffectChanceRandomizer.evaluate(1, critChance)) {
            // TurnEventMessageBuilder.getInstance().appendEvent("A critical hit!");
            BattleContextHolder.get().getTurnMessageHandler().appendEvent("A critical hit!");
            return critMultiplier;
        }
        
        return 1.0; // 1.0 denotes no critical hit
    }

    public void setWeather(String weather) {
        activeWeather = weather;
    }

    private double getWeatherMultiplier(Move move) {
        String type = move.getType();

        // Fire moves are boosted in Harsh Sunlight or Extremely Harsh Sunlight
        if (type.equals("FIRE")) {
            if (activeWeather.equals("Harsh Sunlight") || activeWeather.equals("Extremely Harsh Sunlight")) {
                return 1.5;
            }
            // Fire moves are decreased in Rain
            else if (activeWeather.equals("Rain")) {
                return 0.5;
            }
            // Fire moves are completely nullified in Heavy Rain (only damaging moves)
            else if (activeWeather.equals("Heavy Rain")) {
                return 0.0;
            }
            // No weather up that affects Fire-type moves
            else {
                return 1.0;
            }
        }

        // Water moves are boosted in Rain or Heavy Rain
        if (type.equals("WATER")) {
            if (activeWeather.equals("Rain") || activeWeather.equals("Heavy Rain")) {
                return 1.5;
            }
            // Water moves are decreased in Harsh Sunlight
            else if (activeWeather.equals("Harsh Sunlight")) {
                return 0.5;
            }
            // Water moves are completely nullified in Extremely Harsh Sunlight (only damaging moves)
            else if (activeWeather.equals("Extremely Harsh Sunlight")) {
                return 0.0;
            }
            //  No weather up that affects Water-type moves
            else {
                return 1.0;
            }
        }

        return 1.0;
    }

    private double calculateBurnMultiplier(Pokemon user, Move move) {
        // Certain moves like Facade or abilities (not yet implemented) can bypass the Burn debuff
        if (move.getFunctionCode().equals("DoublePowerIfUserPoisonedBurnedParalyzed")) {
            return 1.0;
        }

        if (user.getStatus().equals("Burn")) {
            return 0.5;
        }

        return 1.0;
    }
}
