package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;

public abstract class Move implements ExecutableMove {
    private String id;
    private String name;
    private String type;
    private String category;
    private int power;
    private int accuracy;
    private int totalPP;
    private String target;
    private int priority;
    private String functionCode;
    private String[] flags;
    private int effectChance;
    // private int pp;
    // private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getTotalPP() {
        return totalPP;
    }

    public void setTotalPP(int totalPP) {
        this.totalPP = totalPP;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String[] getFlags() {
        return flags;
    }

    public void setFlags(String[] flags) {
        this.flags = flags;
    }

    public boolean containsFlag(String flagName) {
        for (String flag : flags) {
            if (flag.equals(flagName)) {
                return true;
            }
        }

        return false;
    }

    public int getEffectChance() {
        return effectChance;
    }

    public void setEffectChance(int effectChance) {
        this.effectChance = effectChance;
    }

    public boolean equals(Move otherMove) {
        if (name.equals(otherMove.getName()) && id.equals(otherMove.getId())) {
            return true;
        }

        return false;
    }

    public abstract void execute(Pokemon moveTarget) throws InvalidIdentifierException;
}
