package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.Pokemon.PokemonStateDto;

public class PartyStateDto {
    public String pokemon1Name;
    public int pokemon1CurrentHp;
    public int pokemon1MaxHp;
    public String pokemon1Status;
    public String pokemon1Move1;
    public String pokemon1Move2;
    public String pokemon1Move3;
    public String pokemon1Move4;
    public String pokemon1Type;
    public int pokemon1Attack;
    public int pokemon1Defense;
    public int pokemon1SpAttack;
    public int pokemon1SpDefense;
    public int pokemon1Speed;

    //ability and item

    public String pokemon2Name;
    public int pokemon2CurrentHp;
    public int pokemon2MaxHp;
    public String pokemon2Status;
    public String pokemon2Move1;
    public String pokemon2Move2;
    public String pokemon2Move3;
    public String pokemon2Move4;
    public String pokemon2Type;
    public int pokemon2Attack;
    public int pokemon2Defense;
    public int pokemon2SpAttack;
    public int pokemon2SpDefense;
    public int pokemon2Speed;

    public String pokemon3Name;
    public int pokemon3CurrentHp;
    public int pokemon3MaxHp;
    public String pokemon3Status;
    public String pokemon3Move1;
    public String pokemon3Move2;
    public String pokemon3Move3;
    public String pokemon3Move4;
    public String pokemon3Type;
    public int pokemon3Attack;
    public int pokemon3Defense;
    public int pokemon3SpAttack;
    public int pokemon3SpDefense;
    public int pokemon3Speed;

    public String pokemon4Name;
    public int pokemon4CurrentHp;
    public int pokemon4MaxHp;
    public String pokemon4Status;
    public String pokemon4Move1;
    public String pokemon4Move2;
    public String pokemon4Move3;
    public String pokemon4Move4;
    public String pokemon4Type;
    public int pokemon4Attack;
    public int pokemon4Defense;
    public int pokemon4SpAttack;
    public int pokemon4SpDefense;
    public int pokemon4Speed;

    public String pokemon5Name;
    public int pokemon5CurrentHp;
    public int pokemon5MaxHp;
    public String pokemon5Status;
    public String pokemon5Move1;
    public String pokemon5Move2;
    public String pokemon5Move3;
    public String pokemon5Move4;
    public String pokemon5Type;
    public int pokemon5Attack;
    public int pokemon5Defense;
    public int pokemon5SpAttack;
    public int pokemon5SpDefense;
    public int pokemon5Speed;

    public String pokemon6Name;
    public int pokemon6CurrentHp;
    public int pokemon6MaxHp;
    public String pokemon6Status;
    public String pokemon6Move1;
    public String pokemon6Move2;
    public String pokemon6Move3;
    public String pokemon6Move4;
    public String pokemon6Type;
    public int pokemon6Attack;
    public int pokemon6Defense;
    public int pokemon6SpAttack;
    public int pokemon6SpDefense;
    public int pokemon6Speed;

    public PartyStateDto() throws InvalidIdentifierException {
        Pokemon[] party = PlayerPartyManager.getInstance().getParty();

        PokemonStateDto dto1 = party[0].generateStateDto();
        PokemonStateDto dto2 = party[1].generateStateDto();
        PokemonStateDto dto3 = party[2].generateStateDto();
        PokemonStateDto dto4 = party[3].generateStateDto();
        PokemonStateDto dto5 = party[4].generateStateDto();
        PokemonStateDto dto6 = party[5].generateStateDto();

        this.pokemon1Name = dto1.name;
        this.pokemon1CurrentHp = dto1.currentHp;
        this.pokemon1MaxHp = dto1.maxHp;
        this.pokemon1Status = dto1.status;
        this.pokemon1Move1 = dto1.move1;
        this.pokemon1Move2 = dto1.move2;
        this.pokemon1Move3 = dto1.move3;
        this.pokemon1Move4 = dto1.move4;
        this.pokemon1Type = dto1.type;
        this.pokemon1Attack = dto1.attack;
        this.pokemon1Defense = dto1.defense;
        this.pokemon1SpAttack = dto1.spAttack;
        this.pokemon1SpDefense = dto1.spDefense;
        this.pokemon1Speed = dto1.speed;

        this.pokemon2Name = dto2.name;
        this.pokemon2CurrentHp = dto2.currentHp;
        this.pokemon2MaxHp = dto2.maxHp;
        this.pokemon2Status = dto2.status;
        this.pokemon2Move1 = dto2.move1;
        this.pokemon2Move2 = dto2.move2;
        this.pokemon2Move3 = dto2.move3;
        this.pokemon2Move4 = dto2.move4;
        this.pokemon2Type = dto2.type;
        this.pokemon2Attack = dto2.attack;
        this.pokemon2Defense = dto2.defense;
        this.pokemon2SpAttack = dto2.spAttack;
        this.pokemon2SpDefense = dto2.spDefense;
        this.pokemon2Speed = dto2.speed;

        this.pokemon3Name = dto3.name;
        this.pokemon3CurrentHp = dto3.currentHp;
        this.pokemon3MaxHp = dto3.maxHp;
        this.pokemon3Status = dto3.status;
        this.pokemon3Move1 = dto3.move1;
        this.pokemon3Move2 = dto3.move2;
        this.pokemon3Move3 = dto3.move3;
        this.pokemon3Move4 = dto3.move4;
        this.pokemon3Type = dto3.type;
        this.pokemon3Attack = dto3.attack;
        this.pokemon3Defense = dto3.defense;
        this.pokemon3SpAttack = dto3.spAttack;
        this.pokemon3SpDefense = dto3.spDefense;
        this.pokemon3Speed = dto3.speed;

        this.pokemon4Name = dto4.name;
        this.pokemon4CurrentHp = dto4.currentHp;
        this.pokemon4MaxHp = dto4.maxHp;
        this.pokemon4Status = dto4.status;
        this.pokemon4Move1 = dto4.move1;
        this.pokemon4Move2 = dto4.move2;
        this.pokemon4Move3 = dto4.move3;
        this.pokemon4Move4 = dto4.move4;
        this.pokemon4Type = dto4.type;
        this.pokemon4Attack = dto4.attack;
        this.pokemon4Defense = dto4.defense;
        this.pokemon4SpAttack = dto4.spAttack;
        this.pokemon4SpDefense = dto4.spDefense;
        this.pokemon4Speed = dto4.speed;

        this.pokemon5Name = dto5.name;
        this.pokemon5CurrentHp = dto5.currentHp;
        this.pokemon5MaxHp = dto5.maxHp;
        this.pokemon5Status = dto5.status;
        this.pokemon5Move1 = dto5.move1;
        this.pokemon5Move2 = dto5.move2;
        this.pokemon5Move3 = dto5.move3;
        this.pokemon5Move4 = dto5.move4;
        this.pokemon5Type = dto5.type;
        this.pokemon5Attack = dto5.attack;
        this.pokemon5Defense = dto5.defense;
        this.pokemon5SpAttack = dto5.spAttack;
        this.pokemon5SpDefense = dto5.spDefense;
        this.pokemon5Speed = dto5.speed;

        this.pokemon6Name = dto6.name;
        this.pokemon6CurrentHp = dto6.currentHp;
        this.pokemon6MaxHp = dto6.maxHp;
        this.pokemon6Status = dto6.status;
        this.pokemon6Move1 = dto6.move1;
        this.pokemon6Move2 = dto6.move2;
        this.pokemon6Move3 = dto6.move3;
        this.pokemon6Move4 = dto6.move4;
        this.pokemon6Type = dto6.type;
        this.pokemon6Attack = dto6.attack;
        this.pokemon6Defense = dto6.defense;
        this.pokemon6SpAttack = dto6.spAttack;
        this.pokemon6SpDefense = dto6.spDefense;
        this.pokemon6Speed = dto6.speed;
    }

    public String getPokemon1Name() {
        return pokemon1Name;
    }
    
    public void setPokemon1Name(String pokemon1Name) {
        this.pokemon1Name = pokemon1Name;
    }

    public int getPokemon1CurrentHp() {
        return pokemon1CurrentHp;
    }

    public void setPokemon1CurrentHp(int pokemon1CurrentHp) {
        this.pokemon1CurrentHp = pokemon1CurrentHp;
    }

    public int getPokemon1MaxHp() {
        return pokemon1MaxHp;
    }

    public void setPokemon1MaxHp(int pokemon1MaxHp) {
        this.pokemon1MaxHp = pokemon1MaxHp;
    }

    public String getPokemon1Status() {
        return pokemon1Status;
    }

    public void setPokemon1Status(String pokemon1Status) {
        this.pokemon1Status = pokemon1Status;
    }

    public String getPokemon1Move1() {
        return pokemon1Move1;
    }

    public void setPokemon1Move1(String pokemon1Move1) {
        this.pokemon1Move1 = pokemon1Move1;
    }

    public String getPokemon1Move2() {
        return pokemon1Move2;
    }

    public void setPokemon1Move2(String pokemon1Move2) {
        this.pokemon1Move2 = pokemon1Move2;
    }

    public String getPokemon1Move3() {
        return pokemon1Move3;
    }

    public void setPokemon1Move3(String pokemon1Move3) {
        this.pokemon1Move3 = pokemon1Move3;
    }

    public String getPokemon1Move4() {
        return pokemon1Move4;
    }

    public void setPokemon1Move4(String pokemon1Move4) {
        this.pokemon1Move4 = pokemon1Move4;
    }

    public String getPokemon1Type() {
        return pokemon1Type;
    }

    public void setPokemon1Type(String pokemon1Type) {
        this.pokemon1Type = pokemon1Type;
    }

    public int getPokemon1Attack() {
        return pokemon1Attack;
    }

    public void setPokemon1Attack(int pokemon1Attack) {
        this.pokemon1Attack = pokemon1Attack;
    }

    public int getPokemon1Defense() {
        return pokemon1Defense;
    }

    public void setPokemon1Defense(int pokemon1Defense) {
        this.pokemon1Defense = pokemon1Defense;
    }

    public int getPokemon1SpAttack() {
        return pokemon1SpAttack;
    }

    public void setPokemon1SpAttack(int pokemon1SpAttack) {
        this.pokemon1SpAttack = pokemon1SpAttack;
    }

    public int getPokemon1SpDefense() {
        return pokemon1SpDefense;
    }

    public void setPokemon1SpDefense(int pokemon1SpDefense) {
        this.pokemon1SpDefense = pokemon1SpDefense;
    }

    public int getPokemon1Speed() {
        return pokemon1Speed;
    }

    public void setPokemon1Speed(int pokemon1Speed) {
        this.pokemon1Speed = pokemon1Speed;
    }

    public String getPokemon2Name() {
        return pokemon2Name;
    }

    public void setPokemon2Name(String pokemon2Name) {
        this.pokemon2Name = pokemon2Name;
    }

    public int getPokemon2CurrentHp() {
        return pokemon2CurrentHp;
    }

    public void setPokemon2CurrentHp(int pokemon2CurrentHp) {
        this.pokemon2CurrentHp = pokemon2CurrentHp;
    }

    public int getPokemon2MaxHp() {
        return pokemon2MaxHp;
    }

    public void setPokemon2MaxHp(int pokemon2MaxHp) {
        this.pokemon2MaxHp = pokemon2MaxHp;
    }

    public String getPokemon2Status() {
        return pokemon2Status;
    }

    public void setPokemon2Status(String pokemon2Status) {
        this.pokemon2Status = pokemon2Status;
    }

    public String getPokemon2Move1() {
        return pokemon2Move1;
    }

    public void setPokemon2Move1(String pokemon2Move1) {
        this.pokemon2Move1 = pokemon2Move1;
    }

    public String getPokemon2Move2() {
        return pokemon2Move2;
    }

    public void setPokemon2Move2(String pokemon2Move2) {
        this.pokemon2Move2 = pokemon2Move2;
    }

    public String getPokemon2Move3() {
        return pokemon2Move3;
    }

    public void setPokemon2Move3(String pokemon2Move3) {
        this.pokemon2Move3 = pokemon2Move3;
    }

    public String getPokemon2Move4() {
        return pokemon2Move4;
    }

    public void setPokemon2Move4(String pokemon2Move4) {
        this.pokemon2Move4 = pokemon2Move4;
    }

    public String getPokemon2Type() {
        return pokemon2Type;
    }

    public void setPokemon2Type(String pokemon2Type) {
        this.pokemon2Type = pokemon2Type;
    }

    public int getPokemon2Attack() {
        return pokemon2Attack;
    }

    public void setPokemon2Attack(int pokemon2Attack) {
        this.pokemon2Attack = pokemon2Attack;
    }

    public int getPokemon2Defense() {
        return pokemon2Defense;
    }

    public void setPokemon2Defense(int pokemon2Defense) {
        this.pokemon2Defense = pokemon2Defense;
    }

    public int getPokemon2SpAttack() {
        return pokemon2SpAttack;
    }

    public void setPokemon2SpAttack(int pokemon2SpAttack) {
        this.pokemon2SpAttack = pokemon2SpAttack;
    }

    public int getPokemon2SpDefense() {
        return pokemon2SpDefense;
    }

    public void setPokemon2SpDefense(int pokemon2SpDefense) {
        this.pokemon2SpDefense = pokemon2SpDefense;
    }

    public int getPokemon2Speed() {
        return pokemon2Speed;
    }

    public void setPokemon2Speed(int pokemon2Speed) {
        this.pokemon2Speed = pokemon2Speed;
    }

    public String getPokemon3Name() {
        return pokemon3Name;
    }

    public void setPokemon3Name(String pokemon3Name) {
        this.pokemon3Name = pokemon3Name;
    }

    public int getPokemon3CurrentHp() {
        return pokemon3CurrentHp;
    }

    public void setPokemon3CurrentHp(int pokemon3CurrentHp) {
        this.pokemon3CurrentHp = pokemon3CurrentHp;
    }

    public int getPokemon3MaxHp() {
        return pokemon3MaxHp;
    }

    public void setPokemon3MaxHp(int pokemon3MaxHp) {
        this.pokemon3MaxHp = pokemon3MaxHp;
    }

    public String getPokemon3Status() {
        return pokemon3Status;
    }
    
    public void setPokemon3Status(String pokemon3Status) {
        this.pokemon3Status = pokemon3Status;
    }
    
    public String getPokemon3Move1() {
        return pokemon3Move1;
    }

    public void setPokemon3Move1(String pokemon3Move1) {
        this.pokemon3Move1 = pokemon3Move1;
    }

    public String getPokemon3Move2() {
        return pokemon3Move2;
    }

    public void setPokemon3Move2(String pokemon3Move2) {
        this.pokemon3Move2 = pokemon3Move2;
    }

    public String getPokemon3Move3() {
        return pokemon3Move3;
    }

    public void setPokemon3Move3(String pokemon3Move3) {
        this.pokemon3Move3 = pokemon3Move3;
    }

    public String getPokemon3Move4() {
        return pokemon3Move4;
    }

    public void setPokemon3Move4(String pokemon3Move4) {
        this.pokemon3Move4 = pokemon3Move4;
    }

    public String getPokemon3Type() {
        return pokemon3Type;
    }

    public void setPokemon3Type(String pokemon3Type) {
        this.pokemon3Type = pokemon3Type;
    }

    public int getPokemon3Attack() {
        return pokemon3Attack;
    }

    public void setPokemon3Attack(int pokemon3Attack) {
        this.pokemon3Attack = pokemon3Attack;
    }

    public int getPokemon3Defense() {
        return pokemon3Defense;
    }

    public void setPokemon3Defense(int pokemon3Defense) {
        this.pokemon3Defense = pokemon3Defense;
    }

    public int getPokemon3SpAttack() {
        return pokemon3SpAttack;
    }

    public void setPokemon3SpAttack(int pokemon3SpAttack) {
        this.pokemon3SpAttack = pokemon3SpAttack;
    }

    public int getPokemon3SpDefense() {
        return pokemon3SpDefense;
    }

    public void setPokemon3SpDefense(int pokemon3SpDefense) {
        this.pokemon3SpDefense = pokemon3SpDefense;
    }

    public int getPokemon3Speed() {
        return pokemon3Speed;
    }

    public void setPokemon3Speed(int pokemon3Speed) {
        this.pokemon3Speed = pokemon3Speed;
    }

    public String getPokemon4Name() {
        return pokemon4Name;
    }

    public void setPokemon4Name(String pokemon4Name) {
        this.pokemon4Name = pokemon4Name;
    }

    public int getPokemon4CurrentHp() {
        return pokemon4CurrentHp;
    }

    public void setPokemon4CurrentHp(int pokemon4CurrentHp) {
        this.pokemon4CurrentHp = pokemon4CurrentHp;
    }

    public int getPokemon4MaxHp() {
        return pokemon4MaxHp;
    }

    public void setPokemon4MaxHp(int pokemon4MaxHp) {
        this.pokemon4MaxHp = pokemon4MaxHp;
    }

    public String getPokemon4Status() {
        return pokemon4Status;
    }

    public void setPokemon4Status(String pokemon4Status) {
        this.pokemon4Status = pokemon4Status;
    }

    public String getPokemon4Move1() {
        return pokemon4Move1;
    }
    
    public void setPokemon4Move1(String pokemon4Move1) {
        this.pokemon4Move1 = pokemon4Move1;
    }

    public String getPokemon4Move2() {
        return pokemon4Move2;
    }

    public void setPokemon4Move2(String pokemon4Move2) {
        this.pokemon4Move2 = pokemon4Move2;
    }

    public String getPokemon4Move3() {
        return pokemon4Move3;
    }

    public void setPokemon4Move3(String pokemon4Move3) {
        this.pokemon4Move3 = pokemon4Move3;
    }

    public String getPokemon4Move4() {
        return pokemon4Move4;
    }

    public void setPokemon4Move4(String pokemon4Move4) {
        this.pokemon4Move4 = pokemon4Move4;
    }

    public String getPokemon4Type() {
        return pokemon4Type;
    }

    public void setPokemon4Type(String pokemon4Type) {
        this.pokemon4Type = pokemon4Type;
    }

    public int getPokemon4Attack() {
        return pokemon4Attack;
    }

    public void setPokemon4Attack(int pokemon4Attack) {
        this.pokemon4Attack = pokemon4Attack;
    }

    public int getPokemon4Defense() {
        return pokemon4Defense;
    }

    public void setPokemon4Defense(int pokemon4Defense) {
        this.pokemon4Defense = pokemon4Defense;
    }

    public int getPokemon4SpAttack() {
        return pokemon4SpAttack;
    }

    public void setPokemon4SpAttack(int pokemon4SpAttack) {
        this.pokemon4SpAttack = pokemon4SpAttack;
    }

    public int getPokemon4SpDefense() {
        return pokemon4SpDefense;
    }

    public void setPokemon4SpDefense(int pokemon4SpDefense) {
        this.pokemon4SpDefense = pokemon4SpDefense;
    }

    public int getPokemon4Speed() {
        return pokemon4Speed;
    }

    public void setPokemon4Speed(int pokemon4Speed) {
        this.pokemon4Speed = pokemon4Speed;
    }

    public String getPokemon5Name() {
        return pokemon5Name;
    }

    public void setPokemon5Name(String pokemon5Name) {
        this.pokemon5Name = pokemon5Name;
    }

    public int getPokemon5CurrentHp() {
        return pokemon5CurrentHp;
    }

    public void setPokemon5CurrentHp(int pokemon5CurrentHp) {
        this.pokemon5CurrentHp = pokemon5CurrentHp;
    }

    public int getPokemon5MaxHp() {
        return pokemon5MaxHp;
    }

    public void setPokemon5MaxHp(int pokemon5MaxHp) {
        this.pokemon5MaxHp = pokemon5MaxHp;
    }

    public String getPokemon5Status() {
        return pokemon5Status;
    }

    public void setPokemon5Status(String pokemon5Status) {
        this.pokemon5Status = pokemon5Status;
    }

    public String getPokemon5Move1() {
        return pokemon5Move1;
    }

    public void setPokemon5Move1(String pokemon5Move1) {
        this.pokemon5Move1 = pokemon5Move1;
    }

    public String getPokemon5Move2() {
        return pokemon5Move2;
    }

    public void setPokemon5Move2(String pokemon5Move2) {
        this.pokemon5Move2 = pokemon5Move2;
    }

    public String getPokemon5Move3() {
        return pokemon5Move3;
    }

    public void setPokemon5Move3(String pokemon5Move3) {
        this.pokemon5Move3 = pokemon5Move3;
    }

    public String getPokemon5Move4() {
        return pokemon5Move4;
    }

    public void setPokemon5Move4(String pokemon5Move4) {
        this.pokemon5Move4 = pokemon5Move4;
    }

    public String getPokemon5Type() {
        return pokemon5Type;
    }

    public void setPokemon5Type(String pokemon5Type) {
        this.pokemon5Type = pokemon5Type;
    }

    public int getPokemon5Attack() {
        return pokemon5Attack;
    }

    public void setPokemon5Attack(int pokemon5Attack) {
        this.pokemon5Attack = pokemon5Attack;
    }

    public int getPokemon5Defense() {
        return pokemon5Defense;
    }

    public void setPokemon5Defense(int pokemon5Defense) {
        this.pokemon5Defense = pokemon5Defense;
    }

    public int getPokemon5SpAttack() {
        return pokemon5SpAttack;
    }

    public void setPokemon5SpAttack(int pokemon5SpAttack) {
        this.pokemon5SpAttack = pokemon5SpAttack;
    }

    public int getPokemon5SpDefense() {
        return pokemon5SpDefense;
    }

    public void setPokemon5SpDefense(int pokemon5SpDefense) {
        this.pokemon5SpDefense = pokemon5SpDefense;
    }

    public int getPokemon5Speed() {
        return pokemon5Speed;
    }

    public void setPokemon5Speed(int pokemon5Speed) {
        this.pokemon5Speed = pokemon5Speed;
    }

    public String getPokemon6Name() {
        return pokemon6Name;
    }

    public void setPokemon6Name(String pokemon6Name) {
        this.pokemon6Name = pokemon6Name;
    }

    public int getPokemon6CurrentHp() {
        return pokemon6CurrentHp;
    }
    
    public void setPokemon6CurrentHp(int pokemon6CurrentHp) {
        this.pokemon6CurrentHp = pokemon6CurrentHp;
    }

    public int getPokemon6MaxHp() {
        return pokemon6MaxHp;
    }

    public void setPokemon6MaxHp(int pokemon6MaxHp) {
        this.pokemon6MaxHp = pokemon6MaxHp;
    }

    public String getPokemon6Status() {
        return pokemon6Status;
    }

    public void setPokemon6Status(String pokemon6Status) {
        this.pokemon6Status = pokemon6Status;
    }

    public String getPokemon6Move1() {
        return pokemon6Move1;
    }

    public void setPokemon6Move1(String pokemon6Move1) {
        this.pokemon6Move1 = pokemon6Move1;
    }

    public String getPokemon6Move2() {
        return pokemon6Move2;
    }

    public void setPokemon6Move2(String pokemon6Move2) {
        this.pokemon6Move2 = pokemon6Move2;
    }

    public String getPokemon6Move3() {
        return pokemon6Move3;
    }

    public void setPokemon6Move3(String pokemon6Move3) {
        this.pokemon6Move3 = pokemon6Move3;
    }

    public String getPokemon6Move4() {
        return pokemon6Move4;
    }

    public void setPokemon6Move4(String pokemon6Move4) {
        this.pokemon6Move4 = pokemon6Move4;
    }

    public String getPokemon6Type() {
        return pokemon6Type;
    }

    public void setPokemon6Type(String pokemon6Type) {
        this.pokemon6Type = pokemon6Type;
    }

    public int getPokemon6Attack() {
        return pokemon6Attack;
    }

    public void setPokemon6Attack(int pokemon6Attack) {
        this.pokemon6Attack = pokemon6Attack;
    }

    public int getPokemon6Defense() {
        return pokemon6Defense;
    }

    public void setPokemon6Defense(int pokemon6Defense) {
        this.pokemon6Defense = pokemon6Defense;
    }

    public int getPokemon6SpAttack() {
        return pokemon6SpAttack;
    }

    public void setPokemon6SpAttack(int pokemon6SpAttack) {
        this.pokemon6SpAttack = pokemon6SpAttack;
    }

    public int getPokemon6SpDefense() {
        return pokemon6SpDefense;
    }

    public void setPokemon6SpDefense(int pokemon6SpDefense) {
        this.pokemon6SpDefense = pokemon6SpDefense;
    }

    public int getPokemon6Speed() {
        return pokemon6Speed;
    }

    public void setPokemon6Speed(int pokemon6Speed) {
        this.pokemon6Speed = pokemon6Speed;
    }
}
