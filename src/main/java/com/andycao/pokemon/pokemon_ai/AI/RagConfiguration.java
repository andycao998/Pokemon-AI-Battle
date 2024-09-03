package com.andycao.pokemon.pokemon_ai.AI;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

// @Configuration
// public class RagConfiguration {
// 	@Value("vectorstore.json")
// 	private String vectorStoreName;

// 	@Bean
// 	VectorStore vectorStore(EmbeddingModel embeddingModel) {
// 		SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);

// 		File vectorStoreFile = getVectorStoreFile();
// 		if (vectorStoreFile.exists()) {
// 			vectorStore.load(vectorStoreFile);
// 		}
// 		else {
// 			DataReader dataReader = new DataReader();
// 			List<Document> docs = dataReader.get();
// 			TokenTextSplitter textSplitter = new TokenTextSplitter();
// 			docs = textSplitter.apply(docs);

// 			vectorStore.add(docs);
// 			vectorStore.save(vectorStoreFile);
// 		}

// 		return vectorStore;
// 	}

// 	private File getVectorStoreFile() {
// 		Path path = Paths.get("src", "main", "resources", "data");
// 		String absPath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
// 		return new File(absPath);
// 	}
// }

@Configuration
class RagConfiguration {
	@Value("classpath:pokedex.json")
    private Resource pokedexFilePath;

	@Value("classpath:typeeffectiveness.txt")
	private Resource typeChartFilePath;

	@Value("classpath:abilities.txt")
	private Resource abilitiesFilePath;

	@Value("classpath:moves.txt")
	private Resource movesFilePath;

	@Value("classpath:items.txt")
	private Resource itemsFilePath;

	@Value("classpath:switching.txt")
	private Resource switchingFilePath;

	@Value("classpath:battling.txt")
	private Resource battlingFilePath;

	// @Value("vectorstore.json")
 	private String vectorStoreName = "vectorstore.json";

	@Bean
	VectorStore	vectorStore(EmbeddingModel embeddingModel) {
		SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);

		File vectorStoreFile = getVectorStoreFile();
		if (vectorStoreFile.exists()) {
			vectorStore.load(vectorStoreFile);
		}
		else {
			JsonReader jsonReader = new JsonReader(pokedexFilePath, "pokedex_number");
				// , "name", "japanese_name", "generation", "status", "species", "type_number", "type_1", "type_2", "height_m", "weight_kg", 
				// "abilities_number", "total_points", "hp", "attack", "defense", "sp_attack", "sp_defense", "speed", "catch_rate", "base_friendship",
				// "base_experience", "growth_rate", "egg_type_number", "egg_type_1", "egg_type_2", "percentage_male", "egg_cycles", "normal_damage_multiplier_against_pokemon", 
				// "fire_damage_multiplier_against_pokemon", "water_damage_multiplier_against_pokemon", "electric_damage_multiplier_against_pokemon", 
				// "grass_damage_multiplier_against_pokemon", "ice_damage_multiplier_against_pokemon", "fighting_damage_multiplier_against_pokemon", 
				// "poison_damage_multiplier_against_pokemon", "ground_damage_multiplier_against_pokemon", "flying_damage_multiplier_against_pokemon", 
				// "psychic_damage_multiplier_against_pokemon", "bug_damage_multiplier_against_pokemon", "rock_damage_multiplier_against_pokemon", 
				// "ghost_damage_multiplier_against_pokemon", "dragon_damage_multiplier_against_pokemon", "dark_damage_multiplier_against_pokemon", 
				// "steel_damage_multiplier_against_pokemon", "fairy_damage_multiplier_against_pokemon", "smogon_description", "bulba_description", "moves", "ability_1", 
				// "ability_2", "ability_hidden", "ability_1_description", "ability_2_description", "ability_hidden_description");

			List<Document> dexDocuments = jsonReader.get();

			TextReader textReader = new TextReader(typeChartFilePath);	
			List<Document> typeDocuments = splitDocument(textReader.get());

			textReader = new TextReader(abilitiesFilePath);
			List<Document> abilityDocuments = splitDocument(textReader.get());

			textReader = new TextReader(movesFilePath);
			List<Document> moveDocuments = splitDocument(textReader.get());

			textReader = new TextReader(itemsFilePath);
			List<Document> itemDocuments = splitDocument(textReader.get());

			textReader = new TextReader(switchingFilePath);
			List<Document> switchingDocuments = textReader.get();

			textReader = new TextReader(battlingFilePath);
			List<Document> battlingDocuments = splitDocument(textReader.get());

			// TokenTextSplitter textSplitter = new TokenTextSplitter();
			// documents = textSplitter.apply(documents);

			vectorStore.add(dexDocuments);
			vectorStore.add(typeDocuments);
			vectorStore.add(abilityDocuments);
			vectorStore.add(moveDocuments);
			vectorStore.add(itemDocuments);
			vectorStore.add(switchingDocuments);
			vectorStore.add(battlingDocuments);
			// Path path = Paths.get("src", "main", "resources", "data");
			// String absPath = path.toFile().getAbsolutePath() + "/vectorstore.json";
			// File vectorStoreFile = new File(absPath);
			// System.out.println(vectorStoreFile.createNewFile());
			vectorStore.save(vectorStoreFile);
		}
		// else {
		// 	DataReader dataReader = new DataReader(vectorStore);
		// 	dataReader.init(vectorStoreFile);
		// 	// List<Document> docs = dataReader.get();
		// 	// TokenTextSplitter textSplitter = new TokenTextSplitter();
		// 	// docs = textSplitter.apply(docs);

		// 	// vectorStore.add(docs);
		// 	// vectorStore.save(vectorStoreFile);
		// }
		

		return vectorStore;
	}

	private List<Document> splitDocument(List<Document> documents) {
		String text = documents.toString();
		String[] splitText = text.split("#");
		return convertStringsToDocuments(splitText);
	}

	private List<Document> convertStringsToDocuments(String[] collection) {
		List<Document> docs = new ArrayList<Document>();

		for (String content : collection) {
			docs.add(new Document(content));
		}

		return docs;
	}

	private File getVectorStoreFile() {
		Path path = Paths.get("src", "main", "resources", "data");
		String absPath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
		return new File(absPath);
	}
}

// @Configuration
// class AppConfig {
// 	@Bean
// 	VectorStore	vectorStore(EmbeddingModel embeddingModel) {
// 		return new SimpleVectorStore(embeddingModel);
// 	}
// }