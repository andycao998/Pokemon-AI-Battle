package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.Exceptions.*;

public class BattleService {
    // Currently uses manual Pokemon generation (will later be randomly pulled from https://pkmn.github.io/randbats/data/gen8randombattle.json)
    public void startBattle() {
        try {
            Pokemon jolteon = new Pokemon(135);
            Pokemon togekiss = new Pokemon(468);
            Pokemon solgaleo = new Pokemon(791);
            Pokemon mamoswine = new Pokemon(473);
            Pokemon kingdra = new Pokemon(230);
            Pokemon drapion = new Pokemon(452);

            Pokemon flygon = new Pokemon(330);
            Pokemon corviknight = new Pokemon(823);
            Pokemon palkia = new Pokemon(484);
            Pokemon volcarona = new Pokemon(637);
            Pokemon amoonguss = new Pokemon(591);
            Pokemon spiritomb = new Pokemon(442);

            String[] moveset1 = {"THUNDERBOLT", "HYPERVOICE", "SHADOWBALL", "VOLTSWITCH"};
            jolteon.setMoves(moveset1);
            String[] moveset2 = {"AURASPHERE", "AIRSLASH", "DAZZLINGGLEAM", "NASTYPLOT"};
            togekiss.setMoves(moveset2);
            String[] moveset3 = {"SUNSTEELSTRIKE", "FLAREBLITZ", "PSYCHICFANGS", "CLOSECOMBAT"};
            solgaleo.setMoves(moveset3);
            String[] moveset4 = {"EARTHQUAKE", "ICESHARD", "ICICLECRASH", "STEALTHROCK"};
            mamoswine.setMoves(moveset4);
            String[] moveset5 = {"DRACOMETEOR", "FLIPTURN", "HURRICANE", "HYDROPUMP"};
            kingdra.setMoves(moveset5);
            String[] moveset6 = {"SWORDSDANCE", "KNOCKOFF", "POISONJAB", "EARTHQUAKE"};
            drapion.setMoves(moveset6);

            String[] moveset7 = {"EARTHQUAKE", "OUTRAGE", "FIREPUNCH", "UTURN"};
            flygon.setMoves(moveset7);
            String[] moveset8 = {"BODYPRESS", "BRAVEBIRD", "IRONHEAD", "BULKUP"};
            corviknight.setMoves(moveset8);
            String[] moveset9 = {"DRACOMETEOR", "HYDROPUMP", "ICEBEAM", "THUNDERBOLT"};
            palkia.setMoves(moveset9);
            String[] moveset10 = {"QUIVERDANCE", "BUGBUZZ", "FIREBLAST", "ROOST"};
            volcarona.setMoves(moveset10);
            String[] moveset11 = {"SPORE", "SLUDGEBOMB", "GIGADRAIN", "TOXIC"};
            amoonguss.setMoves(moveset11);
            String[] moveset12 = {"SHADOWBALL", "SUCKERPUNCH", "FOULPLAY", "WILLOWISP"};
            spiritomb.setMoves(moveset12);

            // Currently the only abilities implemented
            jolteon.setCurrentAbility("Volt Absorb");
            togekiss.setCurrentAbility("Serene Grace");
            solgaleo.setCurrentAbility("Full Metal Body");
            mamoswine.setCurrentAbility("Thick Fat");
            kingdra.setCurrentAbility("Swift Swim");
            drapion.setCurrentAbility("Sniper");

            flygon.setCurrentAbility("Levitate");
            corviknight.setCurrentAbility("Mirror Armor");
            palkia.setCurrentAbility("Pressure");
            volcarona.setCurrentAbility("Flame Body");
            amoonguss.setCurrentAbility("Regenerator");
            spiritomb.setCurrentAbility("Pressure");

            Pokemon[] playerParty = {kingdra, jolteon, mamoswine, togekiss, drapion, solgaleo};
            Pokemon[] botParty = {corviknight, flygon, volcarona, amoonguss, palkia, spiritomb};

            PlayerPartyManager.getInstance().setParty(playerParty);
            BotPartyManager.getInstance().setParty(botParty);

            BattleManager.getInstance().startBattle();
        } 
        // WIP: create more specific exceptions
        catch (InvalidStatException | InvalidIdentifierException | InvalidPartyException e) {
            e.printStackTrace();
        }
    }   
}
