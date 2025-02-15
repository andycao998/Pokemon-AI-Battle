package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.andycao.pokemon.pokemon_ai.Exceptions.*;

@Service
public final class BattleService {
    private static BattleService instance;

    private DocumentGrabber documentGrabber;

    private BattleService() {
        
    }

    public static BattleService getInstance() {
        if (instance == null) {
            instance = new BattleService();
        }

        return instance;
    }

    public void setDocuments(DocumentGrabber documentGrabber) {
        this.documentGrabber = documentGrabber;
    }

    // Currently uses manual Pokemon generation (will be randomly pulled from https://pkmn.github.io/randbats/data/gen8randombattle.json)
    private List<Pokemon> createPokemon() {
        RandbatReader reader = new RandbatReader();

        int numPokemon = 12; // 6 for each team
        int currPokemon = 0;
        int randSize = reader.getSize(); // Number of available Pokemon

        List<Pokemon> party = new ArrayList<Pokemon>();
        List<Integer> checkedIds = new ArrayList<Integer>(); // Keeps track of already parsed random IDs (in case of failure or repeats)

        while (currPokemon < numPokemon) {
            Random random = new Random();
            int id = random.nextInt(randSize);

            if (checkedIds.contains(id)) {
                continue; // To avoid generating duplicate ids and ids that are invalid
            }
            checkedIds.add(id);

            String content = reader.getRandomPokemonSet(id);
            String name = content.substring(1, content.indexOf(":"));

            int dexId = documentGrabber.getIdFromName(name);

            // Generate another Pokemon (some sets are listed in forms such as G-Max, which are unimplemented)
            if (dexId == -1) {
                continue;
            }

            String moveList = content.substring(content.indexOf("\"moves\": "));
            // Convert move names into form: no space, no dashes, uppercase
            List<String> moveOptions = Arrays.asList(moveList.substring(moveList.indexOf("[") + 1, moveList.indexOf("]")).replace("\"", "")
                                                                                                                .replace(" ", "")
                                                                                                                .replace("-", "")
                                                                                                                .toUpperCase()
                                                                                                                .split(","));
            Collections.shuffle(moveOptions);

            String[] moves = {moveOptions.get(0), moveOptions.get(1), moveOptions.get(2), moveOptions.get(3)}; // Grab 4 moves from shuffled options

            try {
                Pokemon pokemon = new Pokemon(dexId);
                pokemon.setMoves(moves);

                party.add(pokemon);
                currPokemon += 1;
            }
            catch (InvalidStatException | InvalidIdentifierException e) {
                e.printStackTrace();
            }
        }

        return party;
    }

    private void setParties() {
        try {
            List<Pokemon> parties = createPokemon(); // Size of 12: indices 0-5 go to player, indices 6-11 go to bot
            Pokemon[] playerParty = {parties.get(0), parties.get(1), parties.get(2), parties.get(3), parties.get(4), parties.get(5)};
            Pokemon[] botParty = {parties.get(6), parties.get(7), parties.get(8), parties.get(9), parties.get(10), parties.get(11)};

            // PlayerPartyManager.getInstance().setParty(playerParty);
            // BotPartyManager.getInstance().setParty(botParty);
            BattleContextHolder.get().getPlayerPartyHandler().setParty(playerParty);
            BattleContextHolder.get().getBotPartyHandler().setParty(botParty);
        }
        catch (InvalidPartyException e) {
            e.printStackTrace();
        }
    }
    
    public void startBattle() {
        setParties();

        try {
            BattleManager.getInstance().startBattle();
        }
        catch (InvalidStatException | InvalidIdentifierException e) {
            e.printStackTrace();
        }

        // try {
        //     Pokemon jolteon = new Pokemon(135);
        //     Pokemon togekiss = new Pokemon(468);
        //     Pokemon solgaleo = new Pokemon(791);
        //     Pokemon mamoswine = new Pokemon(473);
        //     Pokemon kingdra = new Pokemon(230);
        //     Pokemon drapion = new Pokemon(452);

        //     Pokemon flygon = new Pokemon(330);
        //     Pokemon corviknight = new Pokemon(823);
        //     Pokemon palkia = new Pokemon(484);
        //     Pokemon volcarona = new Pokemon(637);
        //     Pokemon amoonguss = new Pokemon(591);
        //     Pokemon spiritomb = new Pokemon(442);

        //     String[] moveset1 = {"THUNDERBOLT", "HYPERVOICE", "SHADOWBALL", "VOLTSWITCH"};
        //     jolteon.setMoves(moveset1);
        //     String[] moveset2 = {"AURASPHERE", "AIRSLASH", "DAZZLINGGLEAM", "NASTYPLOT"};
        //     togekiss.setMoves(moveset2);
        //     String[] moveset3 = {"SUNSTEELSTRIKE", "FLAREBLITZ", "PSYCHICFANGS", "CLOSECOMBAT"};
        //     solgaleo.setMoves(moveset3);
        //     String[] moveset4 = {"EARTHQUAKE", "ICESHARD", "ICICLECRASH", "STEALTHROCK"};
        //     mamoswine.setMoves(moveset4);
        //     String[] moveset5 = {"DRACOMETEOR", "FLIPTURN", "HURRICANE", "HYDROPUMP"};
        //     kingdra.setMoves(moveset5);
        //     String[] moveset6 = {"SWORDSDANCE", "KNOCKOFF", "POISONJAB", "EARTHQUAKE"};
        //     drapion.setMoves(moveset6);

        //     String[] moveset7 = {"EARTHQUAKE", "OUTRAGE", "FIREPUNCH", "UTURN"};
        //     flygon.setMoves(moveset7);
        //     String[] moveset8 = {"BODYPRESS", "BRAVEBIRD", "IRONHEAD", "BULKUP"};
        //     corviknight.setMoves(moveset8);
        //     String[] moveset9 = {"DRACOMETEOR", "HYDROPUMP", "ICEBEAM", "THUNDERBOLT"};
        //     palkia.setMoves(moveset9);
        //     String[] moveset10 = {"QUIVERDANCE", "BUGBUZZ", "FIREBLAST", "ROOST"};
        //     volcarona.setMoves(moveset10);
        //     String[] moveset11 = {"SPORE", "SLUDGEBOMB", "GIGADRAIN", "TOXIC"};
        //     amoonguss.setMoves(moveset11);
        //     String[] moveset12 = {"SHADOWBALL", "SUCKERPUNCH", "FOULPLAY", "WILLOWISP"};
        //     spiritomb.setMoves(moveset12);

        //     // Currently the only abilities implemented
        //     jolteon.setCurrentAbility("Volt Absorb");
        //     togekiss.setCurrentAbility("Serene Grace");
        //     solgaleo.setCurrentAbility("Full Metal Body");
        //     mamoswine.setCurrentAbility("Thick Fat");
        //     kingdra.setCurrentAbility("Swift Swim");
        //     drapion.setCurrentAbility("Sniper");

        //     flygon.setCurrentAbility("Levitate");
        //     corviknight.setCurrentAbility("Mirror Armor");
        //     palkia.setCurrentAbility("Pressure");
        //     volcarona.setCurrentAbility("Flame Body");
        //     amoonguss.setCurrentAbility("Regenerator");
        //     spiritomb.setCurrentAbility("Pressure");

        //     Pokemon[] playerParty = {kingdra, jolteon, mamoswine, togekiss, drapion, solgaleo};
        //     Pokemon[] botParty = {corviknight, flygon, volcarona, amoonguss, palkia, spiritomb};

        //     PlayerPartyManager.getInstance().setParty(playerParty);
        //     BotPartyManager.getInstance().setParty(botParty);

        //     BattleManager.getInstance().startBattle();
        // } 
        // // WIP: create more specific exceptions
        // catch (InvalidStatException | InvalidIdentifierException | InvalidPartyException e) {
        //     e.printStackTrace();
        // }
    }

    public void initializeBattle(String sessionId) {
        BattleManager.getInstance().createHandlers(documentGrabber);
        startBattle();
    }
}
