package com.andycao.pokemon.pokemon_ai.Pokemon;

import java.util.Map;

// Contains natures and their stat increases/decreases ordered {Attack, Defense, Sp. Attack, Sp. Defense, Speed}
public class NatureConstants {
    public static final double[] HARDY =    {1.0, 1.0, 1.0, 1.0, 1.0};
    public static final double[] LONELY =   {1.1, 0.9, 1.0, 1.0, 1.0};
    public static final double[] ADAMANT =  {1.1, 1.0, 0.9, 1.0, 1.0};
    public static final double[] NAUGHTY =  {1.1, 1.0, 1.0, 0.9, 1.0};
    public static final double[] BRAVE =    {1.1, 1.0, 1.0, 1.0, 0.9};
    public static final double[] BOLD =     {0.9, 1.1, 1.0, 1.0, 1.0};
    public static final double[] DOCILE =   {1.0, 1.0, 1.0, 1.0, 1.0};
    public static final double[] IMPISH =   {1.0, 1.1, 0.9, 1.0, 1.0};
    public static final double[] LAX =      {1.0, 1.1, 1.0, 0.9, 1.0};
    public static final double[] RELAXED =  {1.0, 1.1, 1.0, 1.0, 0.9};
    public static final double[] MODEST =   {0.9, 1.0, 1.1, 1.0, 1.0};
    public static final double[] MILD =     {1.0, 0.9, 1.1, 1.0, 1.0};
    public static final double[] BASHFUL =  {1.0, 1.0, 1.0, 1.0, 1.0};
    public static final double[] RASH =     {1.0, 1.0, 1.1, 0.9, 1.0};
    public static final double[] QUIET =    {1.0, 1.0, 1.1, 1.0, 0.9};
    public static final double[] CALM =     {0.9, 1.0, 1.0, 1.1, 1.0};
    public static final double[] GENTLE =   {1.0, 0.9, 1.0, 1.1, 1.0};
    public static final double[] CAREFUL =  {1.0, 1.0, 0.9, 1.1, 1.0};
    public static final double[] QUIRKY =   {1.0, 1.0, 1.0, 1.0, 1.0};
    public static final double[] SASSY =    {1.0, 1.0, 1.0, 1.1, 0.9};
    public static final double[] TIMID =    {0.9, 1.0, 1.0, 1.0, 1.1};
    public static final double[] HASTY =    {1.0, 0.9, 1.0, 1.0, 1.1};
    public static final double[] JOLLY =    {1.0, 1.0, 0.9, 1.0, 1.1};
    public static final double[] NAIVE =    {1.0, 1.0, 1.0, 0.9, 1.1};
    public static final double[] SERIOUS =  {1.0, 1.0, 1.0, 1.0, 1.0};

    public static final Map<String, double[]> natures = Map.ofEntries(
        Map.entry("Hardy", HARDY),
        Map.entry("Lonely", LONELY),
        Map.entry("Adamant", ADAMANT),
        Map.entry("Naughty", NAUGHTY),
        Map.entry("Brave", BRAVE),
        Map.entry("Bold", BOLD),
        Map.entry("Docile", DOCILE),
        Map.entry("Impish", IMPISH),
        Map.entry("Lax", LAX),
        Map.entry("Relaxed", RELAXED),
        Map.entry("Modest", MODEST),
        Map.entry("Mild", MILD),
        Map.entry("Bashful", BASHFUL),
        Map.entry("Rash", RASH),
        Map.entry("Quiet", QUIET),
        Map.entry("Calm", CALM),
        Map.entry("Gentle", GENTLE),
        Map.entry("Careful", CAREFUL),
        Map.entry("Quirky", QUIRKY),
        Map.entry("Sassy", SASSY),
        Map.entry("Timid", TIMID),
        Map.entry("Hasty", HASTY),
        Map.entry("Jolly", JOLLY),
        Map.entry("Naive", NAIVE),
        Map.entry("Serious", SERIOUS)
    );

    private NatureConstants() {

    }
}
