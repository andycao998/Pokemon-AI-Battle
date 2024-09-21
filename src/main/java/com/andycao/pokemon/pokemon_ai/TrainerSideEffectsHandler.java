package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

public class TrainerSideEffectsHandler {
    private boolean stealthRocks;
    private int spikes;
    private int toxicSpikes;
    private boolean stickyWeb;

    private int reflect;
    private int lightScreen;
    private int auroraVeil;

    private int wishTurns;
    private int wishHealing;

    private boolean healingWish;

    public TrainerSideEffectsHandler() {
        wishTurns = -1; // Wish effect should not be active
    }

    /*----------Entry Hazards----------*/

    public boolean getStealthRocks() {
        return stealthRocks;
    }

    public void setStealthRocks(boolean active) {
        if (stealthRocks && !active) {
            TurnEventMessageBuilder.getInstance().appendEvent("Stealth rocks were removed!");
        } 

        stealthRocks = active;
    }

    public int stealthRocksDamageDivisor(double typeEffectiveness) {
        if (typeEffectiveness == 0.25) {
            return 32;
        }
        else if (typeEffectiveness == 0.5) {
            return 16;
        }
        else if (typeEffectiveness == 1.0) {
            return 8;
        }
        else if (typeEffectiveness == 2.0) {
            return 4;
        }
        else if (typeEffectiveness == 4.0) {
            return 2;
        }
        
        return 8;
    }

    public int getSpikeStacks() {
        return spikes;
    }

    public void setSpikeStacks(int count) {
        if (count < 0 || count > 3) {
            TurnEventMessageBuilder.getInstance().appendEvent("There are already three layers of spikes!");
            return;
        }

        spikes = count;
        
        if (count != 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("A layer of spikes surround the opposing team!");
        }
        else if (spikes > 0 && count == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("Spikes were removed!");
        }
    }

    public int spikesDamageDivisor() {
        if (spikes == 1) {
            return 8; // 1 stack: 1/8 max hp damage
        }
        else if (spikes == 2) {
            return 6; // 2 stacks: 1/6 max hp damage
        }
        else if (spikes == 3) {
            return 4; // 3 stacks: 1/4 max hp damage
        }
        
        return 8;
    }

    public int getToxicSpikeStacks() {
        return toxicSpikes;
    }

    public void setToxicSpikeStacks(int count) {
        if (count < 0 || count > 2) {
            TurnEventMessageBuilder.getInstance().appendEvent("There are already two layers of toxic spikes!");
            return;
        }

        toxicSpikes = count;

        if (count != 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("A layer of toxic spikes surround the opposing team!");
        }
        else if (toxicSpikes > 0 && count == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("Toxic spikes were removed!");
        }
    }

    public void applyToxicSpikesPoison(Pokemon pokemon) {
        if (!pokemon.canPoison(pokemon)) {
            return;
        }

        if (toxicSpikes == 1) {
            pokemon.setPoisoned(false);
        }
        else if (toxicSpikes == 2) {
            pokemon.setPoisoned(true);
        }
    }

    public boolean getStickyWeb() {
        return stickyWeb;
    }

    public void setStickyWeb(boolean active) {
        if (stickyWeb && !active) {
            TurnEventMessageBuilder.getInstance().appendEvent("Sticky webs were removed!");
        } 

        stickyWeb = active;
    }

    public void applyEntryHazards(Pokemon pokemon) {
        if (stealthRocks) {
            int divisor = stealthRocksDamageDivisor(pokemon.getDamageMultiplierFromRock());
            pokemon.receiveDamage((int) Math.floor((double) pokemon.getMaxHp() / divisor), pokemon);
            TurnEventMessageBuilder.getInstance().appendEvent("Pointed rocks dug into " + pokemon.getName() + "!");
        }

        // Stealth rocks apply to Flying-type or airborne Pokemon but other entry hazards normally do not
        if (pokemon.containsType("Flying") && !pokemon.getGrounded()) {
            return;
        }

        if (spikes > 0) {
            int divisor = spikesDamageDivisor();
            pokemon.receiveDamage((int) Math.floor((double) pokemon.getMaxHp() / divisor), pokemon);
            TurnEventMessageBuilder.getInstance().appendEvent("Spikes dug into " + pokemon.getName() + "!");
        }

        if (toxicSpikes > 0 && pokemon.containsType("Poison")) {
            TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " absorbed the toxic spikes");
            setToxicSpikeStacks(0);
        }
        else if (toxicSpikes > 0) {
            applyToxicSpikesPoison(pokemon);
            TurnEventMessageBuilder.getInstance().appendEvent("Toxic spikes poisoned " + pokemon.getName() + "!");
        }

        if (stickyWeb) {
            TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " got caught in a sticky web!");
            pokemon.updateSpeedStage(-1, pokemon, false);
        }
    }

    public void removeEntryHazards() {
        setStealthRocks(false);
        setSpikeStacks(0);
        setToxicSpikeStacks(0);
        setStickyWeb(false);
    }

    /*----------Screens----------*/

    public int getReflectTurns() {
        return reflect;
    }

    public void setReflectTurns(int turns) {
        if (reflect > 0 && turns == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("Reflect wore off!");
            reflect = 0;
        }

        reflect = turns;
    }

    private void decrementReflectTurns() {
        if (reflect > 0) {
            reflect -= 1;

            if (reflect == 0) {
                TurnEventMessageBuilder.getInstance().appendEvent("Reflect wore off!");
            }
        }
    }

    public int getLightScreenTurns() {
        return lightScreen;
    }

    public void setLightScreenTurns(int turns) {
        if (lightScreen > 0 && turns == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("Light Screen wore off!");
            lightScreen = 0;
        }

        lightScreen = turns;
    }

    private void decrementLightScreenTurns() {
        if (lightScreen > 0) {
            lightScreen -= 1;

            if (lightScreen == 0) {
                TurnEventMessageBuilder.getInstance().appendEvent("Light Screen wore off!");
            }
        }
    }

    public int getAuroraVeilTurns() {
        return auroraVeil;
    }

    public void setAuroraVeilTurns(int turns) {
        if (auroraVeil > 0 && turns == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("Aurora Veil wore off!");
            auroraVeil = 0;
        }

        auroraVeil = turns;
    }

    private void decrementAuroraVeilTurns() {
        if (auroraVeil > 0) {
            auroraVeil -= 1;

            if (auroraVeil == 0) {
                TurnEventMessageBuilder.getInstance().appendEvent("Aurora Veil wore off!");
            }
        }
    }

    public void decrementScreensDuration() {
        decrementReflectTurns();
        decrementLightScreenTurns();
        decrementAuroraVeilTurns();
    }

    public void removeScreens() {
        setReflectTurns(0);
        setLightScreenTurns(0);
        setAuroraVeilTurns(0);
    }

    /*----------Wishes----------*/

    public int getWishTurns() {
        return wishTurns;
    }

    public void setWishTurns(int turns) {
        wishTurns = turns;
    }

    public int getWishHealing() {
        return wishHealing;
    }

    public void setWishHealing(int health) {
        wishHealing = health;
    }

    public void decrementWishTurns(Pokemon pokemon) {
        if (wishTurns > 0 && wishTurns - 1 == 0) {
            pokemon.receiveHealing(wishHealing);
            TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + "'s wish came true!");
            setWishTurns(-1);
        }
        else if (wishTurns > 0) {
            setWishTurns(wishTurns - 1);
        }
    }

    public void applyWish(Pokemon user) {
        int healing = (int) Math.floor((double) user.getMaxHp() / 2);

        if (wishTurns > 0) {
            TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " already has a wish!");
            BattleManager.getInstance().setPokemonLastMoveFailed(user, true);
            return;
        }

        setWishTurns(2);
        setWishHealing(healing);
        TurnEventMessageBuilder.getInstance().appendEvent(user.getName() + " made a wish!");
    }

    public boolean getHealingWish() {
        return healingWish;
    }

    public void setHealingWish(boolean state) {
        healingWish = state;
    }

    public void receiveHealingWish(Pokemon pokemon) {
        if (!healingWish) {
            return;
        }

        pokemon.receiveHealing(pokemon.getMaxHp());
        pokemon.cureMajorStatus();

        TurnEventMessageBuilder.getInstance().appendEvent(pokemon.getName() + " was healed and cured of status from a healing wish!");
        healingWish = false;
    }
}
