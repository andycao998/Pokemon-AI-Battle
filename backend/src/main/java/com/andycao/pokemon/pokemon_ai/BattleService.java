package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
    }

    public void initializeBattle(String sessionId) {
        BattleManager.getInstance().createHandlers(documentGrabber, false, null);
        startBattle();
    }

    public void initializeTestBattle(String sessionId, Pokemon[] playerParty, Pokemon[] botParty, Scanner actions, boolean allowCrits) {
        BattleManager.getInstance().createHandlers(documentGrabber, true, actions);
        try {
            BattleContextHolder.get().getPlayerPartyHandler().setParty(playerParty);
            BattleContextHolder.get().getBotPartyHandler().setParty(botParty);
        } 
        catch (InvalidPartyException e) {
            e.printStackTrace();
        }
        
        BattleContextHolder.get().getDamageHandler().setCriticalHitsEnabled(allowCrits);

        try {
            BattleManager.getInstance().startBattle();
        }
        catch (InvalidStatException | InvalidIdentifierException e) {
            e.printStackTrace();
        }
    }
}
