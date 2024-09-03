package com.andycao.pokemon.pokemon_ai.Pokemon;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidStatException;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Moves.MoveFactory;
import com.andycao.pokemon.pokemon_ai.Moves.NullMove;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.EffectChanceRandomizer;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;
import com.andycao.pokemon.pokemon_ai.Abilities.SpeedCalcAbilities;
import com.andycao.pokemon.pokemon_ai.Abilities.StatLossImmunityAbilities;
import com.andycao.pokemon.pokemon_ai.Abilities.StatReflectingAbilities;

import java.util.Random;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

public class Pokemon {
    private static final int MIN_HP_STAT = 1;
    private static final int MIN_OTHER_STAT = 4;

    private String name;
    private final int speciesId;
    private final long id;

    private String type1;
    private String type2;

    private double weightInKg;

    private int baseHp;
    private int baseAttack;
    private int baseDefense;
    private int baseSpAttack;
    private int baseSpDefense;
    private int baseSpeed;

    private char sex;

    private double damageMultiplierFromNormal;
    private double damageMultiplierFromFire;
    private double damageMultiplierFromWater;
    private double damageMultiplierFromElectric;
    private double damageMultiplierFromGrass;
    private double damageMultiplierFromIce;
    private double damageMultiplierFromFighting;
    private double damageMultiplierFromPoison;
    private double damageMultiplierFromGround;
    private double damageMultiplierFromFlying;
    private double damageMultiplierFromPsychic;
    private double damageMultiplierFromBug;
    private double damageMultiplierFromRock;
    private double damageMultiplierFromGhost;
    private double damageMultiplierFromDragon;
    private double damageMultiplierFromDark;
    private double damageMultiplierFromSteel;
    private double damageMultiplierFromFairy;

    private int maxHp;
    private int unmodifiedAttack;
    private int unmodifiedDefense;
    private int unmodifiedSpAttack;
    private int unmodifiedSpDefense;
    private int unmodifiedSpeed;

    private int currentHp;
    private int currentAttack;
    private int currentDefense;
    private int currentSpAttack;
    private int currentSpDefense;
    private int currentSpeed;

    private String nature;
    private double attackMultiplier;
    private double defenseMultiplier;
    private double spAttackMultiplier;
    private double spDefenseMultiplier;
    private double speedMultiplier;

    private int ivHp;
    private int ivAttack;
    private int ivDefense;
    private int ivSpAttack;
    private int ivSpDefense;
    private int ivSpeed;

    private int evHp;
    private int evAttack;
    private int evDefense;
    private int evSpAttack;
    private int evSpDefense;
    private int evSpeed;

    private int attackStage;
    private int defenseStage;
    private int spAttackStage;
    private int spDefenseStage;
    private int speedStage;

    private int accuracyStage;
    private int evasionStage;

    private int criticalStage;
    private int criticalChance;

    private String moveList;
    private String[] currentMoves;
    private int[] currentMovePPs;

    private String[] abilityList;
    private String currentAbility;

    private String status;
    private boolean flinch;
    private boolean confuse;
    private boolean leech;
    private boolean curse;
    private int confusionTurns;
    private int badPoisonTurns;
    private int sleepTurns;
    private int drowsyTurns;

    private boolean firstTurnSleep;
    private boolean restSleep;

    private boolean canSwitch;

    private boolean roosted;

    private Triple<String, Integer, Integer> lockedMove;

    private boolean grounded;

    private int turnsOut;

    private Pair<Pokemon, Integer> bound;

    private int soundBlocked;

    private int taunted;

    private boolean statsLoweredThisTurn;
    private boolean statsRaisedThisTurn;
    private int lostHpThisTurn;

    private Pair<String, Integer> protection;

    private boolean destinyBond;

    private String invulnerable;

    private Pair<String, Integer> encored;

    public Pokemon(int speciesId) throws InvalidStatException, InvalidIdentifierException {
        PokemonAttributesDto data = PokemonIdService.createPokemonFromDexNumber(speciesId);

        setName(data.name, speciesId);
        this.speciesId = speciesId;
        id = System.currentTimeMillis();

        PokemonIdService.recordPokemon(this);

        setType1(data.type1);
        setType2(data.type2);

        setBaseHp(data.hp);
        setBaseAttack(data.attack);
        setBaseDefense(data.defense);
        setBaseSpAttack(data.spAttack);
        setBaseSpDefense(data.spDefense);
        setBaseSpeed(data.speed);

        determineSex(data.percentageMale);

        setWeight(data.weightInKg);

        damageMultiplierFromNormal = data.damageMultiplierFromNormal;
        damageMultiplierFromFire = data.damageMultiplierFromFire;
        damageMultiplierFromWater = data.damageMultiplierFromWater;
        damageMultiplierFromElectric = data.damageMultiplierFromElectric;
        damageMultiplierFromGrass = data.damageMultiplierFromGrass;
        damageMultiplierFromIce = data.damageMultiplierFromIce;
        damageMultiplierFromFighting = data.damageMultiplierFromFighting;
        damageMultiplierFromPoison = data.damageMultiplierFromPoison;
        damageMultiplierFromGround = data.damageMultiplierFromGround;
        damageMultiplierFromFlying = data.damageMultiplierFromFlying;
        damageMultiplierFromPsychic = data.damageMultiplierFromPsychic;
        damageMultiplierFromBug = data.damageMultiplierFromBug;
        damageMultiplierFromRock = data.damageMultiplierFromRock;
        damageMultiplierFromGhost = data.damageMultiplierFromGhost;
        damageMultiplierFromDragon = data.damageMultiplierFromDragon;
        damageMultiplierFromDark = data.damageMultiplierFromDark;
        damageMultiplierFromSteel = data.damageMultiplierFromSteel;
        damageMultiplierFromFairy = data.damageMultiplierFromFairy;

        moveList = data.moves;

        setAbilityList(data.ability1, data.ability2, data.hiddenAbility);
        setCurrentAbility(abilityList[0]);

        status = "";

        setIvs(31, 31, 31, 31, 31, 31);
        generateRandomNature();

        calculateStats();

        currentMoves = new String[4];
        currentMovePPs = new int[4];

        currentHp = maxHp;
        currentAttack = unmodifiedAttack;
        currentDefense = unmodifiedDefense;
        currentSpAttack = unmodifiedSpAttack;
        currentSpDefense = unmodifiedSpDefense;
        currentSpeed = unmodifiedSpeed;

        updateCriticalStage(0, true);

        bound = new ImmutablePair<Pokemon, Integer>(this, 0);

        setLockedMove("", 0, 0, 0);

        canSwitch = true;

        grounded = true;
        if (currentAbility.equals("Levitate") || type1.equals("Flying") || type2.equals("Flying")) {
            grounded = false;
        }

        soundBlocked = 0;

        setProtection("None", 0);

        invulnerable = "";

        encored = new ImmutablePair<String, Integer>("", 0);
    }

    public Pokemon(Pokemon another) {
        this.name = another.name;
        this.speciesId = another.speciesId;
        this.id = another.id;
        this.type1 = another.type1;
        this.type2 = another.type2;
        this.weightInKg = another.weightInKg;
        this.baseHp = another.baseHp;
        this.baseAttack = another.baseAttack;
        this.baseDefense = another.baseDefense;
        this.baseSpAttack = another.baseSpAttack;
        this.baseSpDefense = another.baseSpDefense;
        this.baseSpeed = another.baseSpeed;
        this.sex = another.sex;
        this.damageMultiplierFromNormal = another.damageMultiplierFromNormal;
        this.damageMultiplierFromFire = another.damageMultiplierFromFire;
        this.damageMultiplierFromWater = another.damageMultiplierFromWater;
        this.damageMultiplierFromElectric = another.damageMultiplierFromElectric;
        this.damageMultiplierFromGrass = another.damageMultiplierFromGrass;
        this.damageMultiplierFromIce = another.damageMultiplierFromIce;
        this.damageMultiplierFromFighting = another.damageMultiplierFromFighting;
        this.damageMultiplierFromPoison = another.damageMultiplierFromPoison;
        this.damageMultiplierFromGround = another.damageMultiplierFromGround;
        this.damageMultiplierFromFlying = another.damageMultiplierFromFlying;
        this.damageMultiplierFromPsychic = another.damageMultiplierFromPsychic;
        this.damageMultiplierFromBug = another.damageMultiplierFromBug;
        this.damageMultiplierFromRock = another.damageMultiplierFromRock;
        this.damageMultiplierFromGhost = another.damageMultiplierFromGhost;
        this.damageMultiplierFromDragon = another.damageMultiplierFromDragon;
        this.damageMultiplierFromDark = another.damageMultiplierFromDark;
        this.damageMultiplierFromSteel = another.damageMultiplierFromSteel;
        this.damageMultiplierFromFairy = another.damageMultiplierFromFairy;
        this.maxHp = another.maxHp;
        this.unmodifiedAttack = another.unmodifiedAttack;
        this.unmodifiedDefense = another.unmodifiedDefense;
        this.unmodifiedSpAttack = another.unmodifiedSpAttack;
        this.unmodifiedSpDefense = another.unmodifiedSpDefense;
        this.unmodifiedSpeed = another.unmodifiedSpeed;
        this.currentHp = another.currentHp;
        this.currentAttack = another.currentAttack;
        this.currentDefense = another.currentDefense;
        this.currentSpAttack = another.currentSpAttack;
        this.currentSpDefense = another.currentSpDefense;
        this.currentSpeed = another.currentSpeed;
        this.nature = another.nature;
        this.attackMultiplier = another.attackMultiplier;
        this.defenseMultiplier = another.defenseMultiplier;
        this.spAttackMultiplier = another.spAttackMultiplier;
        this.spDefenseMultiplier = another.spDefenseMultiplier;
        this.ivHp = another.ivHp;
        this.ivAttack = another.ivAttack;
        this.ivDefense = another.ivDefense;
        this.ivSpAttack = another.ivSpAttack;
        this.ivSpDefense = another.ivSpDefense;
        this.ivSpeed = another.ivSpeed;
        this.attackStage = another.attackStage;
        this.defenseStage = another.defenseStage;
        this.spAttackStage = another.spAttackStage;
        this.spDefenseStage = another.spDefenseStage;
        this.accuracyStage = another.accuracyStage;
        this.evasionStage = another.evasionStage;
        this.criticalStage = another.criticalStage;
        this.criticalChance = another.criticalChance;
        this.moveList = another.moveList;
        this.currentMoves = another.currentMoves;
        this.currentMovePPs = another.currentMovePPs;
        this.abilityList = another.abilityList;
        this.currentAbility = another.currentAbility;
        this.status = another.status;
        this.flinch = another.flinch;
        this.confuse = another.confuse;
        this.leech = another.leech;
        this.curse = another.curse;
        this.confusionTurns = another.confusionTurns;
        this.badPoisonTurns = another.badPoisonTurns;
        this.sleepTurns = another.sleepTurns;
        this.drowsyTurns = another.drowsyTurns;
        this.firstTurnSleep = another.firstTurnSleep;
        this.restSleep = another.restSleep;
        this.canSwitch = another.canSwitch;
        this.roosted = another.roosted;
        this.lockedMove = new ImmutableTriple<String,Integer,Integer>(another.lockedMove.getLeft(), another.lockedMove.getMiddle(), another.lockedMove.getRight());
        this.grounded = another.grounded;
        this.turnsOut = another.turnsOut;
        this.bound = new ImmutablePair<Pokemon,Integer>(another.bound.getKey(), another.bound.getValue());
        this.soundBlocked = another.soundBlocked;
        this.taunted = another.taunted;
        this.statsLoweredThisTurn = another.statsLoweredThisTurn;
        this.statsRaisedThisTurn = another.statsRaisedThisTurn;
        this.lostHpThisTurn = another.lostHpThisTurn;
        this.protection = new ImmutablePair<String,Integer>(another.protection.getKey(), another.protection.getValue());
        this.destinyBond = another.destinyBond;
        this.invulnerable = another.invulnerable;
        this.encored = new ImmutablePair<String,Integer>(another.encored.getKey(), another.encored.getValue());
    }

    public String getName() {
        return name;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public long getId() {
        return id;
    }

    public void setName(String name, int speciesId) throws InvalidIdentifierException {
        if (speciesId < 1 || speciesId > 898) {
            throw new InvalidIdentifierException("Invalid species ID provided (valid range: 1-898)");
        }

        this.name = name;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public boolean containsType(String type) {
        if (type1.equals(type) || type2.equals(type)) {
            return true;
        }

        return false;
    }

    private void validateType(String type) throws InvalidIdentifierException {
        if (type == null) {
            throw new InvalidIdentifierException("Type provided is null");
        }

        for (String validType : TypeConstants.TYPES) {
            if (type.equals(validType)) {
                return;
            }
        }

        throw new InvalidIdentifierException("Invalid type provided");
    }

    public void setType1(String type) throws InvalidIdentifierException {
        validateType(type);
        type1 = type;
    }

    public void setType2(String type) throws InvalidIdentifierException {
        if (!type.isEmpty()) {
            validateType(type);
        }

        type2 = type;
    }

    public double getWeight() {
        return weightInKg;
    }

    public void setWeight(double weight) throws InvalidStatException {
        if (weight <= 0) {
            throw new InvalidStatException("Weight cannot be less than or equal to zero");
        }

        weightInKg = weight;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseSpAttack() {
        return baseSpAttack;
    }

    public int getBaseSpDefense() {
        return baseSpDefense;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseHp(int hp) throws InvalidStatException {
        if (hp < 1 || hp > 255) {
            throw new InvalidStatException("Base HP outside valid range (1-255)");
        }

        baseHp = hp;
    }

    public void setBaseAttack(int attack) throws InvalidStatException {
        if (attack < 1 || attack > 255) {
            throw new InvalidStatException("Base Attack outside valid range (1-255)");
        }

        baseAttack = attack;
    }

    public void setBaseDefense(int defense) throws InvalidStatException {
        if (defense < 1 || defense > 255) {
            throw new InvalidStatException("Base Defense outside valid range (1-255)");
        }

        baseDefense = defense;
    }

    public void setBaseSpAttack(int spAttack) throws InvalidStatException {
        if (spAttack < 1 || spAttack > 255) {
            throw new InvalidStatException("Base Sp. Attack outside valid range (1-255)");
        }

        baseSpAttack = spAttack;
    }

    public void setBaseSpDefense(int spDefense) throws InvalidStatException {
        if (spDefense < 1 || spDefense > 255) {
            throw new InvalidStatException("Base Sp. Defense outside valid range (1-255)");
        }

        baseSpDefense = spDefense;
    }

    public void setBaseSpeed(int speed) throws InvalidStatException {
        if (speed < 1 || speed > 255) {
            throw new InvalidStatException("Base Speed outside valid range (1-255)");
        }

        baseSpeed = speed;
    }

    public char getSex() {
        return sex;
    }

    private void determineSex(double percentageMale) {
        if (percentageMale == -1.0) {
            sex = 'U';
        }
        else if (percentageMale == 0.0) {
            sex = 'F';
        }
        else if (percentageMale == 100.0) {
            sex = 'M';
        }
        else {
            if (EffectChanceRandomizer.evaluate((int) percentageMale * 10, 1000)) {
                sex = 'M';
            }
            else {
                sex = 'F';
            }
        }
    }

    public double getDamageMultiplierFromNormal() {
        return damageMultiplierFromNormal;
    }

    public double getDamageMultiplierFromFire() {
        return damageMultiplierFromFire;
    }

    public double getDamageMultiplierFromWater() {
        return damageMultiplierFromWater;
    }

    public double getDamageMultiplierFromElectric() {
        return damageMultiplierFromElectric;
    }

    public double getDamageMultiplierFromGrass() {
        return damageMultiplierFromGrass;
    }

    public double getDamageMultiplierFromIce() {
        return damageMultiplierFromIce;
    }

    public double getDamageMultiplierFromFighting() {
        return damageMultiplierFromFighting;
    }

    public double getDamageMultiplierFromPoison() {
        return damageMultiplierFromPoison;
    }

    public double getDamageMultiplierFromGround() {
        return damageMultiplierFromGround;
    }

    public double getDamageMultiplierFromFlying() {
        return damageMultiplierFromFlying;
    }

    public double getDamageMultiplierFromPsychic() {
        return damageMultiplierFromPsychic;
    }

    public double getDamageMultiplierFromBug() {
        return damageMultiplierFromBug;
    }

    public double getDamageMultiplierFromRock() {
        return damageMultiplierFromRock;
    }

    public double getDamageMultiplierFromGhost() {
        return damageMultiplierFromGhost;
    }

    public double getDamageMultiplierFromDragon() {
        return damageMultiplierFromDragon;
    }

    public double getDamageMultiplierFromDark() {
        return damageMultiplierFromDark;
    }

    public double getDamageMultiplierFromSteel() {
        return damageMultiplierFromSteel;
    }

    public double getDamageMultiplierFromFairy() {
        return damageMultiplierFromFairy;
    }

    public String getMoveList() {
        return moveList;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int currentMaxHp) {
        this.maxHp = currentMaxHp;
    }

    public int getUnmodifiedAttack() {
        return unmodifiedAttack;
    }

    // public void setUnmodifiedAttack(int currentAttack) {
    //     this.unmodifiedAttack = currentAttack;
    // }

    public int getUnmodifiedDefense() {
        return unmodifiedDefense;
    }

    // public void setUnmodifiedDefense(int currentDefense) {
    //     this.unmodifiedDefense = currentDefense;
    // }

    public int getUnmodifiedSpAttack() {
        return unmodifiedSpAttack;
    }

    // public void setUnmodifiedSpAttack(int currentSpAttack) {
    //     this.unmodifiedSpAttack = currentSpAttack;
    // }

    public int getUnmodifiedSpDefense() {
        return unmodifiedSpDefense;
    }

    // public void setUnmodifiedSpDefense(int currentSpDefense) {
    //     this.unmodifiedSpDefense = currentSpDefense;
    // }

    public int getUnmodifiedSpeed() {
        return unmodifiedSpeed;
    }

    // public void setUnmodifiedSpeed(int currentSpeed) {
    //     this.unmodifiedSpeed = currentSpeed;
    // }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int hp) {
        if (hp <= 0) {
            status = "Fainted";
            currentHp = 0;
        }
        else if (hp > maxHp) {
            currentHp = maxHp;
        }
        else {
            currentHp = hp;
        }
    }

    public void receiveDamage(int damage, Pokemon inflicter) {
        // Ability stuff on damage
        if (damage == 0) {
            return;
        }

        if (BattleManager.getInstance().getPokemonSubstitute(this) > 0 && !BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s substitute lost " + damage + " health!");
            BattleManager.getInstance().setPokemonSubstitute(this, false, BattleManager.getInstance().getPokemonSubstitute(this) - damage);
            return;
        }

        lostHpThisTurn = damage;

        damage = Math.max(damage, 1);

        TurnEventMessageBuilder.getInstance().appendEvent(name + " lost " + damage + " HP!");

        setCurrentHp(currentHp - damage);

        activateDestinyBond(damage, inflicter);
    }

    public void receiveHealing(int heal) {
        heal = Math.max(heal, 1);

        if (currentHp == maxHp) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s HP is already full!");
        }
        else {
            TurnEventMessageBuilder.getInstance().appendEvent(name + " regained " + heal + " HP!");
            setCurrentHp(currentHp + heal);
        }
    }   

    private void statMessage(String stat, int numStages) {
        String keyWord = "";

        if (Math.abs(numStages) == 1) {
            keyWord = "slightly";
        }
        else if (Math.abs(numStages) == 2) {
            keyWord = "sharply";
        }
        else if (Math.abs(numStages) == 3) {
            keyWord = "drastically";
        }
        else if (numStages >= 6) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + " maximized its " + stat + "!");
            // System.out.println(name + " maximized its " + stat + "!");
        }

        if (numStages < 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s " + stat + " fell " + keyWord + "!");
            // System.out.println(name + "'s " + stat + " fell " + keyWord + "!");
        }
        else if (numStages > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s " + stat + " rose " + keyWord + "!");
            // System.out.println(name + "'s " + stat + " rose " + keyWord + "!");
        }
    }

    public void calculateEffectiveStats() {
        calculateEffectiveAttack(getStageModifier(attackStage));
        calculateEffectiveDefense(getStageModifier(defenseStage));
        calculateEffectiveSpAttack(getStageModifier(spAttackStage));
        calculateEffectiveSpDefense(getStageModifier(spDefenseStage));
        calculateEffectiveSpeed(getStageModifier(speedStage));
    }

    public double getStageModifier(int stage) {
        double modifier = 1.0;
        if (stage < 0) {
            modifier = (double) 2 / (2 + Math.abs(stage));
        }
        else {
            modifier = (double) (2 + Math.abs(stage)) / 2;
        }

        return modifier;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public void setCurrentAttack(int attack) {
        if (attack < MIN_OTHER_STAT) {
            currentAttack = MIN_OTHER_STAT;
        }
        else {
            currentAttack = attack;
        }
    }

    public int getAttackStage() {
        return attackStage;
    }

    public void updateAttackStage(int numStages, Pokemon inflicter, boolean suppressMessage) {
        if (StatReflectingAbilities.execute(this, new NullMove(), inflicter, "Attack", numStages)) {
            return;
        }

        if (numStages < 0 && StatLossImmunityAbilities.execute(this, new NullMove(), inflicter)) {
            return;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s stats were unaffected behind a substitute!");
            return;
        }

        recordStatChanges(numStages);

        int newStage = attackStage + numStages;

        if (newStage > 6) {
            attackStage = 6;
        }
        else if (newStage < -6) {
            attackStage = -6;
        }
        else {
            attackStage = newStage;
        }

        // double modifier = 1.0;
        // if (attackStage < 0) {
        //     modifier = (double) 2 / (2 + Math.abs(attackStage));
        // }
        // else {
        //     modifier = (double) (2 + Math.abs(attackStage)) / 2;
        // }

        // setCurrentAttack((int) Math.floor(currentAttack * modifier));

        if (Math.abs(numStages) > 0 && !suppressMessage) {
            statMessage("Attack", numStages);
        }

        calculateEffectiveAttack(getStageModifier(attackStage));
    }

    private void calculateEffectiveAttack(double stageModifier) {
        int attack = (int) Math.floor(unmodifiedAttack * stageModifier);

        // spAtk = (int) Math.floor(spAtk * SpeedCalcAbilities.execute(this, new NullMove()));
        // item multipliers

        setCurrentAttack(attack);
    }

    public int getCurrentDefense() {
        return currentDefense;
    }

    public void setCurrentDefense(int defense) {
        if (defense < MIN_OTHER_STAT) {
            currentDefense = MIN_OTHER_STAT;
        }
        else {
            currentDefense = defense;
        }
    }

    public int getDefenseStage() {
        return defenseStage;
    }

    public void updateDefenseStage(int numStages, Pokemon inflicter, boolean suppressMessage) {
        if (StatReflectingAbilities.execute(this, new NullMove(), inflicter, "Defense", numStages)) {
            return;
        }

        if (numStages < 0 && StatLossImmunityAbilities.execute(this, new NullMove(), inflicter)) {
            return;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s stats were unaffected behind a substitute!");
            return;
        }

        recordStatChanges(numStages);

        int newStage = defenseStage + numStages;

        if (newStage > 6) {
            defenseStage = 6;
        }
        else if (newStage < -6) {
            defenseStage = -6;
        }
        else {
            defenseStage = newStage;
        }

        // double modifier = 1.0;
        // if (defenseStage < 0) {
        //     modifier = (double) 2 / (2 + Math.abs(defenseStage));
        // }
        // else {
        //     modifier = (double) (2 + Math.abs(defenseStage)) / 2;
        // }

        // setCurrentDefense((int) Math.floor(currentDefense * modifier));

        if (Math.abs(numStages) > 0 && !suppressMessage) {
            statMessage("Defense", numStages);
        }

        calculateEffectiveDefense(getStageModifier(defenseStage));
    }

    private void calculateEffectiveDefense(double stageModifier) {
        int defense = (int) Math.floor(unmodifiedDefense * stageModifier);

        if ((type1.equals("Ice") || type2.equals("Ice")) && BattleManager.getInstance().getWeather().equals("Hail")) {
            defense = (int) Math.floor(defense * 1.5);
        }
        // spAtk = (int) Math.floor(spAtk * SpeedCalcAbilities.execute(this, new NullMove()));
        // item multipliers

        setCurrentDefense(defense);
    }

    public int getCurrentSpAttack() {
        return currentSpAttack;
    }

    public void setCurrentSpAttack(int spAttack) {
        if (spAttack < MIN_OTHER_STAT) {
            currentSpAttack = MIN_OTHER_STAT;
        }
        else {
            currentSpAttack = spAttack;
        }
    }

    public int getSpAttackStage() {
        return spAttackStage;
    }

    public void updateSpAttackStage(int numStages, Pokemon inflicter, boolean suppressMessage) {
        if (StatReflectingAbilities.execute(this, new NullMove(), inflicter, "SpAttack", numStages)) {
            return;
        }

        if (numStages < 0 && StatLossImmunityAbilities.execute(this, new NullMove(), inflicter)) {
            return;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s stats were unaffected behind a substitute!");
            return;
        }

        recordStatChanges(numStages);

        int newStage = spAttackStage + numStages;

        if (newStage > 6) {
            spAttackStage = 6;
        }
        else if (newStage < -6) {
            spAttackStage = -6;
        }
        else {
            spAttackStage = newStage;
        }

        // double modifier = 1.0;
        // if (spAttackStage < 0) {
        //     modifier = (double) 2 / (2 + Math.abs(spAttackStage));
        // }
        // else {
        //     modifier = (double) (2 + Math.abs(spAttackStage)) / 2;
        // }

        // setCurrentSpAttack((int) Math.floor(currentSpAttack * modifier));

        if (Math.abs(numStages) > 0 && !suppressMessage) {
            statMessage("Special Attack", numStages);
        }

        calculateEffectiveSpAttack(getStageModifier(spAttackStage));
    }

    private void calculateEffectiveSpAttack(double stageModifier) {
        int spAttack = (int) Math.floor(unmodifiedSpAttack * stageModifier);

        // spAtk = (int) Math.floor(spAtk * SpeedCalcAbilities.execute(this, new NullMove()));
        // item multipliers

        setCurrentSpAttack(spAttack);
    }

    public int getCurrentSpDefense() {
        return currentSpDefense;
    }

    public void setCurrentSpDefense(int spDefense) {
        if (spDefense < MIN_OTHER_STAT) {
            currentSpDefense = MIN_OTHER_STAT;
        }
        else {
            currentSpDefense = spDefense;
        }
    }

    public int getSpDefenseStage() {
        return spDefenseStage;
    }

    public void updateSpDefenseStage(int numStages, Pokemon inflicter, boolean suppressMessage) {
        if (StatReflectingAbilities.execute(this, new NullMove(), inflicter, "SpDefense", numStages)) {
            return;
        }

        if (numStages < 0 && StatLossImmunityAbilities.execute(this, new NullMove(), inflicter)) {
            return;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s stats were unaffected behind a substitute!");
            return;
        }

        recordStatChanges(numStages);

        int newStage = spDefenseStage + numStages;

        if (newStage > 6) {
            spDefenseStage = 6;
        }
        else if (newStage < -6) {
            spDefenseStage = -6;
        }
        else {
            spDefenseStage = newStage;
        }

        // double modifier = 1.0;
        // if (spDefenseStage < 0) {
        //     modifier = (double) 2 / (2 + Math.abs(spDefenseStage));
        // }
        // else {
        //     modifier = (double) (2 + Math.abs(spDefenseStage)) / 2;
        // }

        // setCurrentSpDefense((int) Math.floor(currentSpDefense * modifier));

        if (Math.abs(numStages) > 0 && !suppressMessage) {
            statMessage("Special Defense", numStages);
        }

        calculateEffectiveSpDefense(getStageModifier(spDefenseStage));
    }

    private void calculateEffectiveSpDefense(double stageModifier) {
        int spDefense = (int) Math.floor(unmodifiedSpDefense * stageModifier);

        if ((type1.equals("Rock") || type2.equals("Rock")) && BattleManager.getInstance().getWeather().equals("Sandstorm")) {
            spDefense = (int) Math.floor(spDefense * 1.5);
        }
        // spDef = (int) Math.floor(spDef * SpeedCalcAbilities.execute(this, new NullMove()));
        // item multipliers

        setCurrentSpDefense(spDefense);
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int speed) {
        if (speed < MIN_OTHER_STAT) {
            currentSpeed = MIN_OTHER_STAT;
        }
        else {
            currentSpeed = speed;
        }
    }

    public int getSpeedStage() {
        return speedStage;
    }

    public void updateSpeedStage(int numStages, Pokemon inflicter, boolean suppressMessage) {
        if (StatReflectingAbilities.execute(this, new NullMove(), inflicter, "Speed", numStages)) {
            return;
        }

        if (numStages < 0 && StatLossImmunityAbilities.execute(this, new NullMove(), inflicter)) {
            return;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s stats were unaffected behind a substitute!");
            return;
        }

        recordStatChanges(numStages);

        int newStage = speedStage + numStages;

        if (newStage > 6) {
            speedStage = 6;
        }
        else if (newStage < -6) {
            speedStage = -6;
        }
        else {
            speedStage = newStage;
        }

        // double modifier = 1.0;
        // if (speedStage < 0) {
        //     modifier = (double) 2 / (2 + Math.abs(speedStage));
        // }
        // else {
        //     modifier = (double) (2 + Math.abs(speedStage)) / 2;
        // }

        // setCurrentSpeed((int) Math.floor(currentSpeed * modifier));
        // calculateEffectiveSpeed(modifier);

        if (Math.abs(numStages) > 0 && !suppressMessage) {
            statMessage("Speed", numStages);
        }

        calculateEffectiveSpeed(getStageModifier(speedStage));
    }

    private void calculateEffectiveSpeed(double stageModifier) {
        int speed = (int) Math.floor(unmodifiedSpeed * stageModifier);

        speed = (int) Math.floor(speed * SpeedCalcAbilities.execute(this, new NullMove()));
        // item multipliers

        setCurrentSpeed(speed);
    }

    public int getAccuracyStage() {
        return accuracyStage;
    }

    public void updateAccuracyStage(int numStages, Pokemon inflicter, boolean suppressMessage) {
        if (StatReflectingAbilities.execute(this, new NullMove(), inflicter, "Accuracy", numStages)) {
            return;
        }

        if (numStages < 0 && StatLossImmunityAbilities.execute(this, new NullMove(), inflicter)) {
            return;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s stats were unaffected behind a substitute!");
            return;
        }

        recordStatChanges(numStages);

        int newStage = accuracyStage + numStages;

        if (newStage > 6) {
            accuracyStage = 6;
        }
        else if (newStage < -6) {
            accuracyStage = -6;
        }
        else {
            accuracyStage = newStage;
        }

        if (Math.abs(numStages) > 0 && !suppressMessage) {
            statMessage("Accuracy", numStages);
        }
    }

    public int getEvasionStage() {
        return evasionStage;
    }

    public void updateEvasionStage(int numStages, Pokemon inflicter, boolean suppressMessage) {
        if (StatReflectingAbilities.execute(this, new NullMove(), inflicter, "Evasion", numStages)) {
            return;
        }

        if (numStages < 0 && StatLossImmunityAbilities.execute(this, new NullMove(), inflicter)) {
            return;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + "'s stats were unaffected behind a substitute!");
            return;
        }

        recordStatChanges(numStages);

        int newStage = evasionStage + numStages;

        if (newStage > 6) {
            evasionStage = 6;
        }
        else if (newStage < -6) {
            evasionStage = -6;
        }
        else {
            evasionStage = newStage;
        }

        if (Math.abs(numStages) > 0 && !suppressMessage) {
            statMessage("Evasion", numStages);
        }
    }

    public int getCriticalStage() {
        return criticalStage;
    }

    public void updateCriticalStage(int numStages, boolean suppressMessage) {
        int newStage = criticalStage + numStages;

        if (newStage > 4) {
            criticalStage = 4;
        }
        else {
            criticalStage = newStage;
        }

        if (criticalStage == 0) {
            criticalChance = 24;
        }
        else if (criticalStage == 1) {
            criticalChance = 8;
        }
        else if (criticalStage == 2) {
            criticalChance = 2;
        }
        else {
            criticalChance = 1;
        }

        if (Math.abs(numStages) > 0 && !suppressMessage) {
            statMessage("Critical Hit Chance", numStages);
        }
    }

    public int getCriticalChance() {
        return criticalChance;
    }

    // public void applyPercentBoost(String stat, double percent) {
    //     if (stat.equals("Attack")) {
    //         setCurrentAttack((int) Math.floor(currentAttack * percent));
    //     }
    //     else if (stat.equals("Defense")) {
    //         setCurrentDefense((int) Math.floor(currentDefense * percent));
    //     }
    //     else if (stat.equals("Special Attack")) {
    //         setCurrentSpAttack((int) Math.floor(currentSpAttack * percent));
    //     }
    //     else if (stat.equals("Special Defense")) {
    //         setCurrentSpDefense((int) Math.floor(currentSpDefense * percent));
    //     }
    //     else if (stat.equals("Speed")) {
    //         setCurrentSpeed((int) Math.floor(currentSpeed * percent));
    //     }
    // }

    public void applyTempStatChange(String stat, double modifier) {
        if (stat.equals("Attack")) {
            setCurrentAttack((int) Math.floor(currentAttack * modifier));
        }
        else if (stat.equals("Defense")) {
            setCurrentDefense((int) Math.floor(currentDefense * modifier));
        }
        else if (stat.equals("SpAttack")) {
            setCurrentSpAttack((int) Math.floor(currentSpAttack * modifier));
        }
        else if (stat.equals("SpDefense")) {
            setCurrentSpDefense((int) Math.floor(currentSpDefense * modifier));
        }
        else if (stat.equals("Speed")) {
            setCurrentSpeed((int) Math.floor(currentSpeed * modifier));
        }
    }

    public void resetStatChanges() {
        updateAttackStage(attackStage * -1, this, true);
        updateDefenseStage(defenseStage * -1, this, true);
        updateSpAttackStage(spAttackStage * -1, this, true);
        updateSpDefenseStage(spDefenseStage * -1, this, true);
        updateSpeedStage(speedStage * -1, this, true);
        updateAccuracyStage(accuracyStage * -1, this, true);
        updateEvasionStage(evasionStage * -1, this, true);
        updateCriticalStage(criticalStage * -1, true);
    }

    private void recordStatChanges(int numStages) {
        if (numStages < 0) {
            statsLoweredThisTurn = true;
        }
        else if (numStages > 0) {
            statsRaisedThisTurn = true;
        }
    }

    public void setIvs(int hp, int attack, int defense, int spAttack, int spDefense, int speed) throws InvalidStatException {
        int[] params = {hp, attack, defense, spAttack, spDefense, speed};

        for (int param : params) {
            if (param > 31 || param < 0) {
                throw new InvalidStatException("An individual value is outside of the acceptable range (0-31)");
            }
        }

        ivHp = hp;
        ivAttack = attack;
        ivDefense = defense;
        ivSpAttack = spAttack;
        ivSpDefense = spDefense;
        ivSpeed = speed;
    }

    public void setEvs(int hp, int attack, int defense, int spAttack, int spDefense, int speed) throws InvalidStatException {
        if (hp + attack + defense + spAttack + spDefense + speed > 510) {
            throw new InvalidStatException("Total effort values exceed maximum allotted (510)");
        }

        int[] params = {hp, attack, defense, spAttack, spDefense, speed};

        for (int param : params) {
            if (param > 255 || param < 0) {
                throw new InvalidStatException("An effort value is outside of the acceptable range (0-255)");
            }
        }

        evHp = hp;
        evAttack = attack;
        evDefense = defense;
        evSpAttack = spAttack;
        evSpDefense = spDefense;
        evSpeed = speed;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    private void generateRandomNature() {
        if (this.nature == null) {
            // String[] natures = {"Hardy", "Lonely", "Adamant", "Naughty", "Brave", "Bold", "Docile", "Impish", "Lax", "Relaxed", "Modest", "Mild", "Bashful", "Rash", "Quiet",
            //                     "Calm", "Gentle", "Careful", "Quirky", "Sassy", "Timid", "Hasty", "Jolly", "Naive", "Serious"};

            // Random generator = new Random();
            // String randomNature = natures[generator.nextInt(natures.length)];

            // nature = randomNature;
            nature = "Hardy";
        }
    }

    private void calculateNatureMultipliers() {
        double[] multipliers = NatureConstants.natures.get(nature);
        attackMultiplier = multipliers[0];
        defenseMultiplier = multipliers[1];
        spAttackMultiplier = multipliers[2];
        spDefenseMultiplier = multipliers[3];
        speedMultiplier = multipliers[4];
    }

    private double calculateStatsHelper(int baseValue, int ivValue, int evValue) {
        return Math.floor(((2 * baseValue + ivValue + Math.floor(evValue / 4.0)) * 50) / 100.0);
    }

    private void calculateStats() {
        calculateNatureMultipliers();

        maxHp = (int) calculateStatsHelper(baseHp, ivHp, evHp) + 50 + 10;
        unmodifiedAttack = (int) Math.floor((calculateStatsHelper(baseAttack, ivAttack, evAttack) + 5) * attackMultiplier);
        unmodifiedDefense = (int) Math.floor((calculateStatsHelper(baseDefense, ivDefense, evDefense) + 5) * defenseMultiplier);
        unmodifiedSpAttack = (int) Math.floor((calculateStatsHelper(baseSpAttack, ivSpAttack, evSpAttack) + 5) * spAttackMultiplier);
        unmodifiedSpDefense = (int) Math.floor((calculateStatsHelper(baseSpDefense, ivSpDefense, evSpDefense) + 5) * spDefenseMultiplier);
        unmodifiedSpeed = (int) Math.floor((calculateStatsHelper(baseSpeed, ivSpeed, evSpeed) + 5) * speedMultiplier);
    }

    public String[] getMoves() {
        return currentMoves;
    }

    public void setMoves(String[] moves) throws InvalidIdentifierException {
        if (moves == null) {
            throw new InvalidIdentifierException("No moves provided");
        }

        int movesFilled = 0;
        int providedLength = moves.length;
        for (int i = 0; i < providedLength; i++) {
            if (moves[i] != null) {
                currentMoves[i] = moves[i];
                currentMovePPs[i] = MoveFactory.generateMove(moves[i]).getTotalPP();
                movesFilled += 1;
            }
        }
        
        for (int j = movesFilled; j < 4; j++) {
            currentMoves[j] = "";
            currentMovePPs[j] = 0;
        }
    }

    private int getIndexOfMove(String moveId) {
        for (int i = 0; i < 4; i++) {
            if (currentMoves[i].equals(moveId)) {
                return i;
            }
        }

        return -1;
    }

    public int getMovePPs(String moveId) {
        int indexOfMove = getIndexOfMove(moveId);

        if (indexOfMove > -1) {
            return currentMovePPs[indexOfMove];
        }
        
        return 0;
    }

    public void decrementMovePP(String moveId) {
        int indexOfMove = getIndexOfMove(moveId);

        if (indexOfMove == -1) {
            return;
        }

        currentMovePPs[indexOfMove] -= 1;
    }

    public String[] getAbilityList() {
        return abilityList;
    }

    public void setAbilityList(String ability1, String ability2, String hiddenAbility) {
        String[] list = {ability1, ability2, hiddenAbility};
        abilityList = list;
    }

    public String getCurrentAbility() {
        return currentAbility;
    }

    public void setCurrentAbility(String ability) throws InvalidIdentifierException {
        for (String validAbility : abilityList) {
            if (ability.equals(validAbility) || ability.equals("None")) {
                currentAbility = ability;
                return;
            }
        }

        throw new InvalidIdentifierException(ability + " is not an available ability");
    }

    public String getStatus() {
        return status;
    }

    public boolean canFlinch() {
        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this)) {
            return false;
        }

        return true;

        // Ability Inner Focus and Shield Dust and Shields Down (Shield form) prevents flinching
    }

    public boolean getFlinched() {
        return flinch;
    }

    public void setFlinched(boolean state) {
        flinch = state;
    }

    public void setFainted() {
        status = "Fainted";
    }

    public boolean canParalyze(String moveType, Pokemon inflicter) {
        if (type1.equals(TypeConstants.ELECTRIC) || type2.equals(TypeConstants.ELECTRIC)) {
            return false;
        }

        if ((type1.equals(TypeConstants.GROUND) || type2.equals(TypeConstants.GROUND)) && moveType.equals("ELECTRIC")) {
            return false;
        }

        if (!status.isEmpty()) {
            return false;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            return false;
        }

        return true;

        // Abilities Limber, Comatose, and Purifying Salt are immune to paralysis. If Sun is up, Leaf Guard Pokemon are immune as well.
        // Ability Magic Guard prevents full paralysis, but still loses speed.
        // Ability Shields Down with Minior's health > 50% is also immune

        // Safeguard and Misty Terrain (for grounded Pokemon) give immunity while their effects are up.

        // A Pokemon behind Substitute cannot be paralyzed except from Synchronize.
    }

    public void setParalyzed() {
        status = "Paralysis";
        
        setCurrentSpeed((int) Math.floor((double) currentSpeed / 2));
    }

    public void cureParalysis() {
        status = "";
    }

    public boolean canBurn(Pokemon inflicter) {
        if (type1.equals(TypeConstants.FIRE) || type2.equals(TypeConstants.FIRE)) {
            return false;
        }

        if (!status.isEmpty()) {
            return false;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            return false;
        }

        return true;

        // Abilities Water Veil, Water Bubble, Comatose, and Purifying Salt are immune to burn. If Sun is up, Leaf Guard Pokemon are immune as well.
        // Ability Magic Guard prevents HP loss, but still loses attack while Guts is the opposite (HP loss but no attack loss).
        // Ability Heatproof halves HP loss and Ability Shields Down with Minior's health > 50% is also immune

        // Safeguard and Misty Terrain (for grounded Pokemon) give immunity while their effects are up.

        // A Pokemon behind Substitute cannot be paralyzed except from Synchronize or Flame Orb.
    }

    public void setBurned() {
        status = "Burn";
    }

    public void cureBurn() {
        status = "";
    }

    public boolean canPoison(Pokemon inflicter) {
        if (type1.equals(TypeConstants.POISON) || type2.equals(TypeConstants.POISON)) {
            return false;
        }

        if (type1.equals(TypeConstants.STEEL) || type2.equals(TypeConstants.STEEL)) {
            return false;
        }

        if (!status.isEmpty()) {
            return false;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            return false;
        }

        return true;

        // Abilities Immunity, Comatose, Purifying Salt, and Pastel Veil are immune to poison. If sun is up, Leaf Guard Pokemon are immune as well.
        // Ability Shields Down with Minior's health > 50% is also immune, and Magic Guard also prevents poison damage (essentially immune to poison).
        
        // Safeguard and Misty Terrain (for grounded Pokemon) give immunity while their effects are up.

        // A Pokemon behind Substitute cannot be paralyzed except from Synchronize or Toxic Orb.
    }

    public void setPoisoned(boolean badPoison) {
        if (badPoison) {
            badPoisonTurns = 1;
            status = "BadPoison";
        }
        else {
            status = "Poison";
        }
    }

    public void incrementBadlyPoisonedTurns() {
        badPoisonTurns += 1;
    }

    public int getTurnsBadlyPoisoned() {
        return badPoisonTurns;
    }

    public void resetBadlyPoisonedTurns() {
        if (status.equals("BadPoison")) {
            badPoisonTurns = 1;
        }
    }

    public void curePoison() {
        status = "";
    }

    public boolean canSleep(Pokemon inflicter) {
        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            return false;
        }

        return true;

        // Abilities Insomnia, Vital Spirit, Comatose, Sweet Veil, and Purifying Salt are immune to sleep. If sun is up, Leaf Guard Pokemon are immune as well.
        // Ability Shields Down with Minior's health > 50% is also immune.

        // Ability Early Bird will be asleep for half the amount of turns (which may cause them to instantly wake up).
        
        // Electric Terrain and Misty Terrain (for grounded Pokemon) give immunity while their effects are up.

        // Pokemon cannot fall asleep if Uproar is used (except for Comatose sleep).

        // Safeguard prevents a Pokemon from being made drowsy from Yawn, but will not prevent sleep if the Pokemon is already drowsy. Self-inflicted sleep is also not blocked.
    }
    
    public int getSleepTurns() {
        return sleepTurns;
    }

    public void setAsleep() {
        status = "Sleep";
        firstTurnSleep = true;
        Random random = new Random();
        int numTurns = random.nextInt(3) + 1;

        sleepTurns = numTurns;
    }

    public void setAsleep(int turns) {
        status = "Sleep";
        restSleep = true;

        sleepTurns = turns;
    }

    // public void setSleepTurns(int turns) {
    //     sleepTurns = turns;
    // }

    public void decrementSleepTurns() {
        if (sleepTurns > 0) {
            sleepTurns -= 1;
        }
    }

    public void cureSleep() {
        status = "";
        sleepTurns = 0;
    }

    public int getDrowsyTurns() {
        return drowsyTurns;
    }

    public boolean getFirstTurnOfSleep() {
        return firstTurnSleep;
    }

    public void setFirstTurnOfSleep(boolean state) {
        firstTurnSleep = state;
    }

    public boolean getRestSleep() {
        return restSleep;
    }

    public void setRestSleep(boolean state) {
        restSleep = state;
    }

    public void setDrowsy(int turns, boolean reset) {
        // same conditions as sleep?

        if (reset) {
            drowsyTurns = 0;
            return;
        }

        if (drowsyTurns > 0 && turns == 0) {
            setAsleep();
            TurnEventMessageBuilder.getInstance().appendEvent(name + " fell asleep!");
        }

        drowsyTurns = turns;
    } 

    public boolean canFreeze(Pokemon inflicter) {
        if (type1.equals(TypeConstants.ICE) || type2.equals(TypeConstants.ICE)) {
            return false;
        }

        if (!status.isEmpty()) {
            return false;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            return false;
        }

        return true;

        // Abilities Magma Armor, Comatose, and Purifying Salt are immune to sleep.
        // Ability Shields Down with Minior's health > 50% is also immune.

        // Ability Shield Dust prevents freeze due to it being a move-additional-effect-exclusive status

        // Safeguard and Misty Terrain (for grounded Pokemon) give immunity while their effects are up.

        // A Pokemon behind Substitute cannot be frozen except from Synchronize.
    }

    public void setFrozen() {
        status = "Freeze";
    }

    public void cureFreeze() {
        status = "";
    }

    public boolean getConfused() {
        return confuse;
    }

    public void setConfused(boolean state) {
        confuse = state;

        if (state) {
            setConfusionTurns();
        }
        else {
            confusionTurns = 0;
        }
    }

    public int getConfusionTurns() {
        return confusionTurns;
    }

    public void setConfusionTurns() {
        Random random = new Random();
        int turns = random.nextInt(3) + 2;
        confusionTurns = turns;
    }

    public void decrementConfusionTurns() {
        confusionTurns -= 1;
    }

    public boolean canConfuse(Pokemon inflicter) {
        if (getConfused()) {
            return false;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this) && !inflicter.equals(this)) {
            return false;
        }

        return true;

        // Ability Own Tempo prevents confusion.

        // Safeguard and Misty Terrain (for grounded Pokemon) give immunity while their effects are up.

        // A Pokemon behind Substitute cannot be confused except from berries or their own damaging moves' side effects.
    }

    public boolean getLeeched() {
        return leech;
    }

    public void setLeeched(boolean state) {
        leech = state;
    }

    public boolean canLeech() {
        if (type1.equals("Grass") || type2.equals("Grass")) {
            return false;
        }

        if (getLeeched()) {
            return false;
        }

        if (!BattleManager.getInstance().getPokemonSubstituteBypassed(this)) {
            return false;
        }

        return true;
    }

    public boolean getCursed() {
        return curse;
    }

    public void setCursed(boolean state) {
        curse = state;
    }

    public void cureMajorStatus() {
        cureParalysis();
        cureBurn();
        curePoison();
        cureFreeze();
        cureSleep();
    }

    public boolean getCanSwitch() {
        return canSwitch;
    }

    public void setCanSwitch(boolean canSwitch) {
        this.canSwitch = canSwitch;
    }

    public boolean partFlying() {
        if (type1.equals(TypeConstants.FLYING) || type2.equals(TypeConstants.FLYING)) {
            return true;
        }

        return false;
    }

    public boolean getRoosted() {
        return roosted;
    }

    public void setRoosted(boolean state) {
        roosted = state;
    }

    public void loseFlying() throws InvalidIdentifierException {
        if (type1.equals(TypeConstants.FLYING) && type2.isEmpty()) {
            setType1(TypeConstants.NORMAL);
        }
        else if (type1.equals(TypeConstants.FLYING)) {
            setType1(type2);
            setType2("");
        }
        else if (type2.equals(TypeConstants.FLYING)) {
            setType2("");
        }

        roosted = true;
        TurnEventMessageBuilder.getInstance().appendEvent(name + " temporarily lost its Flying type!");
    }

    public void restoreTyping() throws InvalidIdentifierException {
        PokemonAttributesDto data = PokemonIdService.createPokemonFromDexNumber(speciesId);

        setType1(data.type1);
        setType2(data.type2);
        
        roosted = false;
        TurnEventMessageBuilder.getInstance().appendEvent(name + " regained its Flying type!");
    }

    public boolean lockedIntoMove() {
        if (lockedMove.getRight() > 0) {
            return true;
        }

        return false;
    }

    public Triple<String, Integer, Integer> getLockedMove() {
        return lockedMove;
    }

    public void setLockedMove(String moveName, int turnsLocked, int turnCounter, int turnInterrupted) {
        Triple<String, Integer, Integer> move = new ImmutableTriple<String,Integer,Integer>(moveName, turnsLocked, turnCounter);
        lockedMove = move;

        if (turnCounter > 0) {
            //canSwitch = false;
        }

        if (turnCounter == 0 && turnsLocked != 0) {
            if (canConfuse(this) && turnInterrupted == 0) {
                setConfused(true);
                TurnEventMessageBuilder.getInstance().appendEvent(name + " is confused!");
            }

            lockedMove = new ImmutableTriple<String,Integer,Integer>("", 0, 0);
            //canSwitch = true;
        }

        // BattleManager.getInstance().canSwitch(this);
    }

    public void interruptMultiTurnMove() {
        int turnsLocked = lockedMove.getMiddle();

        if (turnsLocked > 0) {    
            setLockedMove("", turnsLocked, 0, lockedMove.getRight() - 1);
        }

        if (invulnerable.isEmpty()) {
            setInvulnerable("");
        }
    }

    public boolean getGrounded() {
        return grounded;
    }

    public void setGrounded(boolean state) {
        grounded = state;
    }

    public int getTurnsOut() {
        return turnsOut;
    }

    public void resetTurnsOut() {
        turnsOut = 0;
    }

    public void incrementTurnsOut() {
        turnsOut += 1;
    }

    public Pokemon getBinder() {
        return bound.getKey();
    }

    public int getBoundTurns() {
        return bound.getValue();
    }

    public void setBound(Pokemon binder, int turns) {
        if (!binder.equals(this) && bound.getValue() > 0 && turns == 0) {
            // canSwitch = true;
            // BattleManager.getInstance().canSwitch(this);
            TurnEventMessageBuilder.getInstance().appendEvent(name + " is no longer bound!");
        }

        if (turns > 0 && !BattleManager.getInstance().getPokemonSubstituteBypassed(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + " cannot be bound behind a substitute!");
            return;
        }

        bound = new ImmutablePair<Pokemon,Integer>(binder, turns);
    }

    public int getSoundBlockedTurns() {
        return soundBlocked;
    }

    public void setSoundBlocked(int turns) {
        if (soundBlocked > 0 && turns == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + " recovered from Throat Chop!");
            soundBlocked = 0;
            return;
        }

        if (turns > 0 && !BattleManager.getInstance().getPokemonSubstituteBypassed(this)) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + " cannot be silenced behind a substitute!");
            return;
        }

        soundBlocked = turns;
    }

    public int getTauntedTurns() {
        return taunted;
    }

    public void setTaunted(int turns) {
        if (taunted > 0 && turns == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + " recoved from Taunt!");
            taunted = 0;
            return;
        }

        taunted = turns;
    }

    public boolean getStatsLoweredThisTurn() {
        return statsLoweredThisTurn;
    }

    public void setStatsLoweredThisTurn(boolean state) {
        statsLoweredThisTurn = state;
    }

    public boolean getStatsRaisedThisTurn() {
        return statsRaisedThisTurn;
    }

    public void setStatsRaisedThisTurn(boolean state) {
        statsRaisedThisTurn = state;
    }

    public int getLostHpThisTurn() {
        return lostHpThisTurn;
    }

    public void setLostHpThisTurn(int hp) {
        lostHpThisTurn = hp;
    }

    public String getProtection() {
        return protection.getKey();
    }

    public int getProtectionTurnsSuccessful() {
        return protection.getValue();
    }

    public void setProtection(String type, int turnsSuccessful) {
        protection = new ImmutablePair<String,Integer>(type, turnsSuccessful);
    }

    public void refreshProtectionTurns() {
        if (protection.getValue() == 0) {
            return;
        }

        Move lastMove = BattleManager.getInstance().getPokemonLastMove(this);

        if (lastMove.getFunctionCode().contains("ProtectUser")) {
            return;
        }
        
        setProtection("None", 0);
    }

    public boolean getDestinyBond() {
        return destinyBond;
    }

    public void setDestinyBond(boolean state) {
        destinyBond = state;
    }

    private void activateDestinyBond(int damage, Pokemon inflicter) {
        if (destinyBond && !inflicter.equals(this) && damage >= currentHp) {
            inflicter.setCurrentHp(0);
            TurnEventMessageBuilder.getInstance().appendEvent(name + " took " + inflicter.getName() + " with it!");
        }
    }

    public String getInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(String type) {
        invulnerable = type;
    }

    public int getEncoredTurns() {
        return encored.getValue();
    }

    public String getEncoredMove() {
        return encored.getKey();
    }

    public void setEncored(String moveId, int turns) {
        if (getEncoredTurns() > 0 && turns == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(name + " is no longer encored!");
            encored = new ImmutablePair<String,Integer>("", 0);
            setLockedMove("", lockedMove.getMiddle(), 0, -1);
            return;
        }

        encored = new ImmutablePair<String,Integer>(moveId, turns);
        setLockedMove(moveId, turns, 100, -1);
        //canSwitch = true;
        // BattleManager.getInstance().canSwitch(this);
    }

    public PokemonStateDto generateStateDto() throws InvalidIdentifierException {
        Move move1 = MoveFactory.generateMove(currentMoves[0]);
        String move1Info = move1.getName() + ", " + move1.getType() + ", " + currentMovePPs[0] + ", " + move1.getTotalPP() + ", " + move1.getCategory();
        Move move2 = MoveFactory.generateMove(currentMoves[1]);
        String move2Info = move2.getName() + ", " + move2.getType() + ", " + currentMovePPs[1] + ", " + move2.getTotalPP() + ", " + move2.getCategory();
        Move move3 = MoveFactory.generateMove(currentMoves[2]);
        String move3Info = move3.getName() + ", " + move3.getType() + ", " + currentMovePPs[2] + ", " + move3.getTotalPP() + ", " + move3.getCategory();
        Move move4 = MoveFactory.generateMove(currentMoves[3]);
        String move4Info = move4.getName() + ", " + move4.getType() + ", " + currentMovePPs[3] + ", " + move4.getTotalPP() + ", " + move4.getCategory(); 

        String fullType = "";
        if (type2.isEmpty()) {
            fullType = type1;
        }
        else {
            fullType = type1 + "/" + type2;
        }

        return new PokemonStateDto(name, currentHp, maxHp, status, move1Info, move2Info, move3Info, move4Info, 
                                   fullType, currentAttack, currentDefense, currentSpAttack, currentSpDefense, currentSpeed);
    }

    public boolean equals(Pokemon other) {
        if (id == other.getId()) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();

        if (type2.isEmpty()) {
            content.append(name + " is a " + type1 + " type\n");
        }
        else {
            content.append(name + " is a " + type1 + "/" + type2 + " type\n");
        }
        
        content.append(name + " has a " + nature + " nature\n");
        
        content.append(name + " currently has " + currentHp + " HP out of " + maxHp + " max HP\n");
        if (status.isEmpty()) {
            content.append(name + " is currently not Burned, Paralyzed, Poisoned, Frozen, or Asleep\n");
        }
        else {
            content.append(name + " currently has the " + status + " status effect\n");
        }
        
        content.append(name + " currently has " + currentAttack + " attack and has " + attackStage + " attack Stages" + " \n");
        content.append(name + " currently has " + currentDefense + " defense and has " + defenseStage + " defense Stages" + " \n");
        content.append(name + " currently has " + currentSpAttack + " special attack and has " + spAttackStage + " special attack Stages" + " \n");
        content.append(name + " currently has " + currentSpDefense + " special defense and has " + spDefenseStage + " special defense Stages" + " \n");
        content.append(name + " currently has " + currentSpeed + " speed and has " + speedStage + " speed Stages" + " \n");

        content.append(name + " knows the moves: " + currentMoves[0] + " (" + currentMovePPs[0] + " PP), " + currentMoves[1] + " (" + currentMovePPs[1] + " PP), " + 
                                   currentMoves[2] + " (" + currentMovePPs[2] + " PP), " + currentMoves[3] + " (" + currentMovePPs[3] + " PP)");

        content.append("Is " + name + " currently confused: " + confuse + "\n");
        content.append("Is " + name + " currently drowsy: " + (drowsyTurns > 0) + "\n");
        content.append("Is " + name + " currently leeched: " + leech + "\n");
        content.append("Is " + name + " currently cursed: " + curse + "\n");
        content.append("Is " + name + " currently taunted: " + (taunted > 0) + "\n");
        content.append("Is " + name + " currently prevented from using sound moves: " + (soundBlocked > 0) + "\n");
        content.append("Is " + name + " currently encored: " + (encored.getValue() > 0) + "\n");
        content.append("Is " + name + " currently locked into a move: " + lockedIntoMove() + "\n");
        content.append("Is " + name + " currently able to switch: " + canSwitch + "\n");
        content.append("Is " + name + " currently bound by its opponent: " + (getBoundTurns() > 0) + "\n");
        content.append("Is " + name + " currently grounded: " + grounded + "\n");
        content.append("Is destiny bond currently active on " + name + ": " + destinyBond + " \n");
        content.append("Does " + name + " already have an active substitute: " + (BattleManager.getInstance().getPokemonSubstitute(this) > 0) + "\n");

        return content.toString();
    }
}
