package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class NullMove extends Move {
    @Override
    public String getId() {
        return "";
    }

    @Override
    public void setId(String id) {
        
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public void setType(String type) {

    }

    @Override
    public String getCategory() {
        return "";
    }

    @Override
    public void setCategory(String category) {

    }

    @Override
    public int getPower() {
        return 0;
    }

    @Override
    public void setPower(int power) {

    }

    @Override
    public int getAccuracy() {
        return 0;
    }

    @Override
    public void setAccuracy(int accuracy) {

    }

    @Override
    public int getTotalPP() {
        return 0;
    }

    @Override
    public void setTotalPP(int totalPP) {
        
    }

    @Override
    public String getTarget() {
        return "";
    }

    @Override
    public void setTarget(String target) {

    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void setPriority(int priority) {

    }

    @Override
    public String getFunctionCode() {
        return "";
    }

    @Override
    public void setFunctionCode(String functionCode) {

    }

    @Override
    public String[] getFlags() {
        return new String[0];
    }

    @Override
    public void setFlags(String[] flags) {

    }

    @Override
    public boolean containsFlag(String flagName) {
        return false;
    }

    @Override
    public int getEffectChance() {
        return 0;
    }

    @Override
    public void setEffectChance(int effectChance) {
        
    }

    @Override
    public boolean equals(Move otherMove) {
        return false;
    }

    @Override
    public void execute(Pokemon moveTarget) throws InvalidIdentifierException {
        
    }
}
