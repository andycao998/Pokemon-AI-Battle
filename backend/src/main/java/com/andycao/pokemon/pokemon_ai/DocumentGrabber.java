package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;

public class DocumentGrabber {
    private final int MAX_OFFSET = 12;

    private ClassPathResource pokedexResource;
    private List<Document> dexDocuments;

    private ClassPathResource typeChartResource;
    private List<Document> typeDocuments;

    private ClassPathResource movesResource;
    private List<Document> moveDocuments;

    // Currently unused - most abilities not implemented
    // private ClassPathResource abilitiesResource;
    // private List<Document> abilityDocuments;

    // Currently unused - items not implemented
    // private ClassPathResource itemsResource;
    // private List<Document> itemDocuments;

    private ClassPathResource battlingResource;
    private List<Document> battlingDocuments;

    private ClassPathResource switchingResource;
    private List<Document> switchingDocuments;

    public DocumentGrabber() {
        pokedexResource = new ClassPathResource("pokedex.json");
        typeChartResource = new ClassPathResource("typeeffectiveness.txt");
        movesResource = new ClassPathResource("moves.txt");
        // abilitiesResource = new ClassPathResource("abilities.txt");
        // itemsResource = new ClassPathResource("items.txt");
        battlingResource = new ClassPathResource("battling.txt");
        switchingResource = new ClassPathResource("switching.txt");

        readFilesIntoDocuments();
    }

    /*----------Document Creation----------*/
    
    private String readInputStream(InputStream inputStream) throws IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];

        StringBuilder text = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        
        for (int bytesRead; (bytesRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            text.append(buffer, 0, bytesRead);
        }
        
        return text.toString();
    }

    private void readFilesIntoDocuments() {
        try {
            String pokedex = readInputStream(pokedexResource.getInputStream());
            dexDocuments = splitDocument(pokedex, "\"pokedex_number\":");

            String typeChart = readInputStream(typeChartResource.getInputStream());
            typeDocuments = splitDocument(typeChart, "#");

            String moves = readInputStream(movesResource.getInputStream());
            moveDocuments = splitDocument(moves, "#-------------------------------");

            // String abilities = readInputStream(abilitiesResource.getInputStream());
            // abilityDocuments = splitDocument(abilities, "#-------------------------------");

            // String items = readInputStream(itemsResource.getInputStream());
            // itemDocuments = splitDocument(items, "#-------------------------------");

            String battling = readInputStream(battlingResource.getInputStream());
            battlingDocuments = splitDocument(battling, "#");

            String switching = readInputStream(switchingResource.getInputStream());
            switchingDocuments = new ArrayList<Document>();
            switchingDocuments.add(new Document(switching));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Document> splitDocument(String text, String splitExpression) {
		// String text = documents.toString();
		String[] splitText = text.split(splitExpression);
		return convertStringsToDocuments(splitText);
	}

	private List<Document> convertStringsToDocuments(String[] collection) {
		List<Document> docs = new ArrayList<Document>();

		for (String content : collection) {
            if (content.isEmpty()) {
                continue;
            }

			docs.add(new Document(content));
		}

		return docs;
	}

    /*---------Document Parsing----------*/

    private Document findContent(List<Document> docs, String content) {
        for (Document doc : docs) {
            int contentLength = content.length();

            if (doc.getContent().length() <= contentLength + MAX_OFFSET) {
                continue;
            }

            // Explore only a portion of a document's text where the content is expected to be found
            if (doc.getContent().substring(0, contentLength + MAX_OFFSET).contains(content)) {
                return doc;
            }
        }

        return null; // Content was not found, simply ignore
    }

    private Document findPokemon(String content) {
        for (Document doc : dexDocuments) {
            if (doc.getContent().contains(content)) {
                return doc;
            }
        }

        return null;
    }

    // Curates all the documents necessary for AI's response as opposed to using similarity search on a vector store
    public List<Document> getTurnDocuments(Pokemon playerPokemon, Pokemon botPokemon, String[] playerMoves, String[] botMoves) {
        List<Document> docs = new ArrayList<Document>();
        
        // Pokemon[] playerParty = PlayerPartyManager.getInstance().updateAvailableParty(playerPokemon);
        // Pokemon[] botParty = BotPartyManager.getInstance().updateAvailableParty(botPokemon);

        Pokemon[] playerParty = BattleContextHolder.get().getPlayerPartyHandler().updateAvailableParty(playerPokemon);
        Pokemon[] botParty = BattleContextHolder.get().getBotPartyHandler().updateAvailableParty(botPokemon);

        // Add all Pokemon from the player and AI's teams
        Set<Pokemon> allPokemon = new HashSet<Pokemon>();
        allPokemon.add(playerPokemon);
        allPokemon.add(botPokemon);
        allPokemon.addAll(Arrays.asList(playerParty));
        allPokemon.addAll(Arrays.asList(botParty));

        for (Pokemon pokemon : allPokemon) {
            String content = "\"name\": \"" + pokemon.getName() + "\"";
            Document doc = findContent(dexDocuments, content);

            if (doc != null) {
                docs.add(doc);
            }
        }

        // Add all moves from the current active Pokemon
        Set<String> allMoves = new HashSet<String>();
        allMoves.addAll(Arrays.asList(playerMoves));
        allMoves.addAll(Arrays.asList(botMoves));

        for (String move : allMoves) {
            String content = "[" + move + "]";

            Document doc = findContent(moveDocuments, content);

            if (doc != null) {
                docs.add(doc);
            }
        }

        // Add general battling documents
        docs.addAll(typeDocuments);
        docs.addAll(battlingDocuments);
        docs.addAll(switchingDocuments);

        return docs;
    }

    public int getIdFromName(String pokemon) {
        String content = "\"name\":" + pokemon;
        Document dexEntry = findPokemon(content);

        if (dexEntry == null || pokemon.equals("Ditto")) {
            return -1; // If name not found, signal to retry with another entry (Ex: Urshifu-Rapid-Strike-Gmax and Urshifu-Rapid-Strike are invalid as there are discrepancies with how they are represented in pokedex.json and in the randbats file)
        }

        return Integer.valueOf(dexEntry.getContent().substring(1, dexEntry.getContent().indexOf(","))); // First item in returned doc is the id: " 1," format (skip beginning space)
    }
}