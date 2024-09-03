package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.Exceptions.*;

public class BattleService {
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

            String[] moveset1 = {"THUNDERBOLT", "HYPERVOICE", "SHADOWBALL", "VOLTSWITCH"}; // hyper voice, shadow ball
            jolteon.setMoves(moveset1);
            String[] moveset2 = {"AURASPHERE", "AIRSLASH", "DAZZLINGGLEAM", "NASTYPLOT"}; // aura sphere
            togekiss.setMoves(moveset2);
            String[] moveset3 = {"SUNSTEELSTRIKE", "FLAREBLITZ", "PSYCHICFANGS", "CLOSECOMBAT"};
            solgaleo.setMoves(moveset3);
            String[] moveset4 = {"EARTHQUAKE", "ICESHARD", "ICICLECRASH", "STEALTHROCK"}; // stealth rock, iceshard, earthquake
            mamoswine.setMoves(moveset4);
            String[] moveset5 = {"DRACOMETEOR", "FLIPTURN", "BRAVEBIRD", "HYDROPUMP"}; // hyrrucabe
            kingdra.setMoves(moveset5);
            String[] moveset6 = {"SWORDSDANCE", "KNOCKOFF", "POISONJAB", "EARTHQUAKE"}; // knock off
            drapion.setMoves(moveset6);

            String[] moveset7 = {"EARTHQUAKE", "OUTRAGE", "FIREPUNCH", "UTURN"}; // UTuRN
            flygon.setMoves(moveset7);
            String[] moveset8 = {"BODYPRESS", "BRAVEBIRD", "IRONHEAD", "BULKUP"}; // iron head
            corviknight.setMoves(moveset8);
            String[] moveset9 = {"DRACOMETEOR", "HYDROPUMP", "ICEBEAM", "THUNDERBOLT"}; // thunderbnolt
            palkia.setMoves(moveset9);
            String[] moveset10 = {"QUIVERDANCE", "BUGBUZZ", "FIREBLAST", "ROOST"}; // roost, fireblast
            volcarona.setMoves(moveset10);
            String[] moveset11 = {"SPORE", "SLUDGEBOMB", "GIGADRAIN", "TOXIC"}; // toxic gigadrain
            amoonguss.setMoves(moveset11);
            String[] moveset12 = {"SHADOWBALL", "SUCKERPUNCH", "FOULPLAY", "WILLOWISP"}; // foul play
            spiritomb.setMoves(moveset12);

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
            Pokemon[] botParty = {flygon, volcarona, amoonguss, palkia, spiritomb, corviknight};

            PlayerPartyManager.getInstance().setParty(playerParty);
            BotPartyManager.getInstance().setParty(botParty);

            BattleManager.getInstance().startBattle();
            // BattleManager.getInstance().useMove(jolteon, "THUNDERBOLT");
            // // BattleManager.getInstance().useMove(flygon, "LANDSWRATH");
            // BattleManager.getInstance().useMove(flygon, "EARTHQUAKE");
            // jolteon.setFainted();
            // BattleManager.getInstance().updateSides(true, false);
            // // BattleManager.getInstance().useMove(jolteon, "HYPERVOICE");
            // // BattleManager.getInstance().useMove(flygon, "FIREPUNCH");
            // // BattleManager.getInstance().useMove(jolteon, "SHADOWBALL");T
            // // BattleManager.getInstance().useMove(flygon, "DRAGONCLAW");

            // BattleManager.getInstance().useMove(togekiss, "DAZZLINGGLEAM");
            // BattleManager.getInstance().useMove(flygon, "FIREPUNCH");
            // BattleManager.getInstance().useMove(togekiss, "AIRSLASH");
            // flygon.setFainted();
            // BattleManager.getInstance().updateSides(false, true);
            // BattleManager.getInstance().useMove(corviknight, "BRAVEBIRD");
            // BattleManager.getInstance().useMove(togekiss, "AURASPHERE");
            // BattleManager.getInstance().useMove(corviknight, "BODYPRESS");
            // BattleManager.getInstance().useMove(togekiss, "AIRSLASH");
            // BattleManager.getInstance().useMove(corviknight, "IRONHEAD");
        } 
        catch (InvalidStatException | InvalidIdentifierException | InvalidPartyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   
}
