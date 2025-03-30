package com.andycao.pokemon.pokemon_ai.Pokemon;

import java.util.ArrayList;
import java.util.List;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Moves.MoveFactory;

public class PokemonUpdateDto {
    public List<String> warnings = new ArrayList<String>();

    //public char sex;

    public String nature;

    public int ivHp;
    public int ivAttack;
    public int ivDefense;
    public int ivSpAttack;
    public int ivSpDefense;
    public int ivSpeed;

    public int evHp;
    public int evAttack;
    public int evDefense;
    public int evSpAttack;
    public int evSpDefense;
    public int evSpeed;

    public String[] currentMoves;

    public String currentAbility;

    public PokemonUpdateDto(char sex, String nature, int ivHp, int ivAttack, int ivDefense, int ivSpAttack, int ivSpDefense, int ivSpeed, int evHp, int evAttack,
                            int evDefense, int evSpAttack, int evSpDefense, int evSpeed, String[] currentMoves, String currentAbility) {
        // setSex(sex);
        setNature(nature);
        setIvHp(ivHp);
        setIvAttack(ivAttack);
        setIvDefense(ivDefense);
        setIvSpAttack(ivSpAttack);
        setIvSpDefense(ivSpDefense);
        setIvSpeed(ivSpeed);
        setEvHp(evHp);
        setEvAttack(evAttack);
        setEvDefense(evDefense);
        setEvSpAttack(evSpAttack);
        setEvSpDefense(evSpDefense);
        setEvSpeed(evSpeed);
        setCurrentMoves(currentMoves);
        setCurrentAbility(currentAbility);
    }

    // private void validateSex(char sex) {
    //     if (sex != 'M' || sex != 'F') {
    //         warnings.add("Sex must be 'M' or 'F'.");
    //     }
    // }

    // public char getSex() {
    //     return sex;
    // }

    // public void setSex(char sex) {
    //     validateSex(sex);
    //     this.sex = sex;
    // }

    private void validateNature(String nature) {
        String[] natures = {"Hardy", "Lonely", "Adamant", "Naughty", "Brave", "Bold", "Docile", "Impish", "Lax", "Relaxed", "Modest", "Mild", "Bashful", "Rash", "Quiet",
                            "Calm", "Gentle", "Careful", "Quirky", "Sassy", "Timid", "Hasty", "Jolly", "Naive", "Serious"};

        boolean valid = false;
        for (String validNature : natures) {
            if (nature.toUpperCase().equals(validNature.toUpperCase())) {
                valid = true;
            }
        }

        if (!valid) {
            warnings.add("Nature provided not valid.");
        }
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        validateNature(nature);
        this.nature = nature;
    }

    private void validateIv(int iv, String stat) {
        if (iv < 0) {
            warnings.add(stat + " IV cannot go below 0.");
        }
        else if (iv > 31) {
            warnings.add(stat + " IV cannot go above 31.");
        }
    }

    public int getIvHp() {
        return ivHp;
    }

    public void setIvHp(int ivHp) {
        validateIv(ivHp, "HP");
        this.ivHp = ivHp;
    }

    public int getIvAttack() {
        return ivAttack;
    }

    public void setIvAttack(int ivAttack) {
        validateIv(ivAttack, "Attack");
        this.ivAttack = ivAttack;
    }

    public int getIvDefense() {
        return ivDefense;
    }

    public void setIvDefense(int ivDefense) {
        validateIv(ivDefense, "Defense");
        this.ivDefense = ivDefense;
    }

    public int getIvSpAttack() {
        return ivSpAttack;
    }

    public void setIvSpAttack(int ivSpAttack) {
        validateIv(ivSpAttack, "Special Attack");
        this.ivSpAttack = ivSpAttack;
    }

    public int getIvSpDefense() {
        return ivSpDefense;
    }

    public void setIvSpDefense(int ivSpDefense) {
        validateIv(ivSpDefense, "Special Defense");
        this.ivSpDefense = ivSpDefense;
    }

    public int getIvSpeed() {
        return ivSpeed;
    }

    public void setIvSpeed(int ivSpeed) {
        validateIv(ivSpeed, "Speed");
        this.ivSpeed = ivSpeed;
    }

    private void validateEv(int ev, String stat) {
        if (ev < 0) {
            warnings.add(stat + " EV cannot go below 0.");
        }
        else if (ev > 255) {
            warnings.add(stat + " EV cannot go above 255.");
        }
    }

    public int getEvHp() {
        return evHp;
    }

    public void setEvHp(int evHp) {
        validateEv(evHp, "HP");
        this.evHp = evHp;
    }

    public int getEvAttack() {
        return evAttack;
    }

    public void setEvAttack(int evAttack) {
        validateEv(evAttack, "Attack");
        this.evAttack = evAttack;
    }

    public int getEvDefense() {
        return evDefense;
    }

    public void setEvDefense(int evDefense) {
        validateEv(evDefense, "Defense");
        this.evDefense = evDefense;
    }

    public int getEvSpAttack() {
        return evSpAttack;
    }

    public void setEvSpAttack(int evSpAttack) {
        validateEv(evSpAttack, "Special Attack");
        this.evSpAttack = evSpAttack;
    }

    public int getEvSpDefense() {
        return evSpDefense;
    }

    public void setEvSpDefense(int evSpDefense) {
        validateEv(evSpDefense, "Special Defense");
        this.evSpDefense = evSpDefense;
    }

    public int getEvSpeed() {
        return evSpeed;
    }

    public void setEvSpeed(int evSpeed) {
        validateEv(evSpeed, "Speed");
        this.evSpeed = evSpeed;
    }

    public void validateMoves(String[] moveNames) {
        for (String moveName : moveNames) {
            if (MoveFactory.generateMove(moveName).getName().equals("")) {
                warnings.add(moveName + " is not a valid move.");
            }
        }
    }

    public String[] getCurrentMoves() {
        return currentMoves;
    }

    public void setCurrentMoves(String[] currentMoves) {
        validateMoves(currentMoves);
        this.currentMoves = currentMoves;
    }

    public String getCurrentAbility() {
        return currentAbility;
    }

    public void setCurrentAbility(String currentAbility) {
        this.currentAbility = currentAbility;
    }
}
