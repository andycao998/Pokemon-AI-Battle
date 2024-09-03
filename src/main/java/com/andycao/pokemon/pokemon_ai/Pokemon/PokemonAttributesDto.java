package com.andycao.pokemon.pokemon_ai.Pokemon;

public class PokemonAttributesDto {
    public String name;

    public String type1;
    public String type2;

    public double weightInKg;

    public int hp;
    public int attack;
    public int defense;
    public int spAttack;
    public int spDefense;
    public int speed;

    public double percentageMale;

    public double damageMultiplierFromNormal;
    public double damageMultiplierFromFire;
    public double damageMultiplierFromWater;
    public double damageMultiplierFromElectric;
    public double damageMultiplierFromGrass;
    public double damageMultiplierFromIce;
    public double damageMultiplierFromFighting;
    public double damageMultiplierFromPoison;
    public double damageMultiplierFromGround;
    public double damageMultiplierFromFlying;
    public double damageMultiplierFromPsychic;
    public double damageMultiplierFromBug;
    public double damageMultiplierFromRock;
    public double damageMultiplierFromGhost;
    public double damageMultiplierFromDragon;
    public double damageMultiplierFromDark;
    public double damageMultiplierFromSteel;
    public double damageMultiplierFromFairy;

    public String moves;

    public String ability1;
    public String ability2;
    public String hiddenAbility;

    public PokemonAttributesDto(  String name, String type1, String type2, double weightInKg, int hp, int attack, int defense, int spAttack, int spDefense, int speed, 
                        double percentageMale, double damageMultiplierFromNormal, double damageMultiplierFromFire, double damageMultiplierFromWater, 
                        double damageMultiplierFromElectric, double damageMultiplierFromGrass, double damageMultiplierFromIce, double damageMultiplierFromFighting,
                        double damageMultiplierFromPoison, double damageMultiplierFromGround, double damageMultiplierFromFlying, double damageMultiplierFromPsychic,
                        double damageMultiplierFromBug, double damageMultiplierFromRock, double damageMultiplierFromGhost, double damageMultiplierFromDragon,
                        double damageMultiplierFromDark, double damageMultiplierFromSteel, double damageMultiplierFromFairy, String moves, String ability1, String ability2,
                        String hiddenAbility) {

        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.weightInKg = weightInKg;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.spAttack = spAttack;
        this.spDefense = spDefense;
        this.speed = speed;
        this.percentageMale = percentageMale;
        this.damageMultiplierFromNormal = damageMultiplierFromNormal;
        this.damageMultiplierFromFire = damageMultiplierFromFire;
        this.damageMultiplierFromWater = damageMultiplierFromWater;
        this.damageMultiplierFromElectric = damageMultiplierFromElectric;
        this.damageMultiplierFromGrass = damageMultiplierFromGrass;
        this.damageMultiplierFromIce = damageMultiplierFromIce;
        this.damageMultiplierFromFighting = damageMultiplierFromFighting;
        this.damageMultiplierFromPoison = damageMultiplierFromPoison;
        this.damageMultiplierFromGround = damageMultiplierFromGround;
        this.damageMultiplierFromFlying = damageMultiplierFromFlying;
        this.damageMultiplierFromPsychic = damageMultiplierFromPsychic;
        this.damageMultiplierFromBug = damageMultiplierFromBug;
        this.damageMultiplierFromRock = damageMultiplierFromRock;
        this.damageMultiplierFromGhost = damageMultiplierFromGhost;
        this.damageMultiplierFromDragon = damageMultiplierFromDragon;
        this.damageMultiplierFromDark = damageMultiplierFromDark;
        this.damageMultiplierFromSteel = damageMultiplierFromSteel;
        this.damageMultiplierFromFairy = damageMultiplierFromFairy;
        this.moves = moves;
        this.ability1 = ability1;
        this.ability2 = ability2;
        this.hiddenAbility = hiddenAbility;
    }
}
