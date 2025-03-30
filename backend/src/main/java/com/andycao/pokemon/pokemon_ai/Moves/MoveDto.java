package com.andycao.pokemon.pokemon_ai.Moves;

public class MoveDto {
    public String moveName;
    public String displayName;
    public String type;
    public String category;
    public String power;
    public String accuracy;
    public String totalPP;
    public String target;
    public String priority;
    public String functionCode;
    public String[] flags;
    public String effectChance;

    public MoveDto(String moveName, String displayName, String type, String category, String power, String accuracy, String totalPP, String target, String priority, String functionCode, String[] flags, String effectChance) {
        this.moveName = moveName;
        this.displayName = displayName;
        this.type = type;
        this.category = category;
        this.power = power;
        this.accuracy = accuracy;
        this.totalPP = totalPP;
        this.target = target;
        this.priority = priority;
        this.functionCode = functionCode;
        this.flags = flags;
        this.effectChance = effectChance;
    }
}
