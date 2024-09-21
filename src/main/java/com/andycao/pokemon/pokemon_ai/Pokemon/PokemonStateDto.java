package com.andycao.pokemon.pokemon_ai.Pokemon;

// Used in PartyStateDto creation
public class PokemonStateDto {
    public String name;

    public int level;

    public int currentHp;
    public int maxHp;

    public String status;

    public String move1;
    public String move2;
    public String move3;
    public String move4;

    public String spriteUrl;

    public String type;

    public int attack;
    public int defense;
    public int spAttack;
    public int spDefense;
    public int speed;

    public PokemonStateDto(String name, int currentHp, int maxHp, String status, String move1, String move2, String move3, String move4,
                           String type, int attack, int defense, int spAttack, int spDefense, int speed) {
        this.name = name;
        level = 50;

        this.currentHp = currentHp;
        this.maxHp = maxHp;

        this.status = status;

        this.move1 = move1;
        this.move2 = move2;
        this.move3 = move3;
        this.move4 = move4;

        spriteUrl = name.toUpperCase() + ".png";

        this.type = type;

        this.attack = attack;
        this.defense = defense;
        this.spAttack = spAttack;
        this.spDefense = spDefense;
        this.speed = speed;
    }
}
