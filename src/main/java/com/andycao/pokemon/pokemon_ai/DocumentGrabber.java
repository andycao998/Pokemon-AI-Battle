package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Moves.Move;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.FileSystemResource;

public class DocumentGrabber {
    private File pokedexFilePath;
    private List<Document> dexDocuments;

    private File typeChartFilePath;
    private List<Document> typeDocuments;

    private File movesFilePath;
    private List<Document> moveDocuments;

    private File abilitiesFilePath;
    private List<Document> abilityDocuments;

    private File itemsFilePath;
    private List<Document> itemDocuments;

    private File battlingFilePath;
    private List<Document> battlingDocuments;

    private File switchingFilePath;
    private List<Document> switchingDocuments;

    public DocumentGrabber() {
        pokedexFilePath = new File(DocumentGrabber.class.getClassLoader().getResource("pokedex.json").getFile());
        typeChartFilePath = new File(DocumentGrabber.class.getClassLoader().getResource("typeeffectiveness.txt").getFile());
        movesFilePath = new File(DocumentGrabber.class.getClassLoader().getResource("moves.txt").getFile());
        abilitiesFilePath = new File(DocumentGrabber.class.getClassLoader().getResource("abilities.txt").getFile());
        itemsFilePath = new File(DocumentGrabber.class.getClassLoader().getResource("items.txt").getFile());
        battlingFilePath = new File(DocumentGrabber.class.getClassLoader().getResource("battling.txt").getFile());
        switchingFilePath = new File(DocumentGrabber.class.getClassLoader().getResource("switching.txt").getFile());

        readFilesIntoDocuments();
    }

    private void readFilesIntoDocuments() {
        TextReader textReader = new TextReader(new FileSystemResource(pokedexFilePath));
        dexDocuments = splitDocument(textReader.get(), "\"pokedex_number\":");

        textReader = new TextReader(new FileSystemResource(typeChartFilePath));	
        typeDocuments = splitDocument(textReader.get(), "#");

        textReader = new TextReader(new FileSystemResource(movesFilePath));
        moveDocuments = splitDocument(textReader.get(), "#-------------------------------");

        textReader = new TextReader(new FileSystemResource(abilitiesFilePath));
        abilityDocuments = splitDocument(textReader.get(), "#-------------------------------");

        textReader = new TextReader(new FileSystemResource(itemsFilePath));
        itemDocuments = splitDocument(textReader.get(), "#-------------------------------");

        textReader = new TextReader(new FileSystemResource(battlingFilePath));
        battlingDocuments = splitDocument(textReader.get(), "#");

        textReader = new TextReader(new FileSystemResource(switchingFilePath));
        switchingDocuments = textReader.get();
    }

    private List<Document> splitDocument(List<Document> documents, String splitExpression) {
		String text = documents.toString();
		String[] splitText = text.split(splitExpression);
		return convertStringsToDocuments(splitText);
	}

	private List<Document> convertStringsToDocuments(String[] collection) {
		List<Document> docs = new ArrayList<Document>();

		for (String content : collection) {
			docs.add(new Document(content));
		}

		return docs;
	}

    private Document findContent(List<Document> docs, String content) throws InvalidIdentifierException {
        for (Document doc : docs) {
            int contentLength = content.length();

            if (doc.getContent().length() <= contentLength + 12) {
                continue;
            }

            if (doc.getContent().substring(0, contentLength + 12).contains(content)) {
                return doc;
            }
        }

        throw new InvalidIdentifierException(content + " was not found in any documents");
    }

    public List<Document> getTurnDocuments(Pokemon playerPokemon, Pokemon botPokemon, String[] playerMoves, String[] botMoves) throws InvalidIdentifierException {
        List<Document> docs = new ArrayList<Document>();
        
        Pokemon[] playerParty = PlayerPartyManager.getInstance().updateAvailableParty(playerPokemon);
        Pokemon[] botParty = BotPartyManager.getInstance().updateAvailableParty(botPokemon);

        Set<Pokemon> allPokemon = new HashSet<Pokemon>();
        allPokemon.add(playerPokemon);
        allPokemon.add(botPokemon);
        allPokemon.addAll(Arrays.asList(playerParty));
        allPokemon.addAll(Arrays.asList(botParty));

        for (Pokemon pokemon : allPokemon) {
            String content = "\"name\": \"" + pokemon.getName() + "\"";

            docs.add(findContent(dexDocuments, content));
        }

        Set<String> allMoves = new HashSet<String>();
        allMoves.addAll(Arrays.asList(playerMoves));
        allMoves.addAll(Arrays.asList(botMoves));

        for (String move : allMoves) {
            String content = "[" + move + "]";

            docs.add(findContent(moveDocuments, content));
        }

        // Set<String> activeTypes = new HashSet<String>();
        // activeTypes.add(playerPokemon.getType1());
        // activeTypes.add(playerPokemon.getType2());
        // activeTypes.add(botPokemon.getType1());
        // activeTypes.add(botPokemon.getType2());

        // for (String type : activeTypes) {
        //     docs
        // }
        docs.addAll(typeDocuments);

        docs.addAll(battlingDocuments);
        docs.addAll(switchingDocuments);

        return docs;
    }
}