package com.andycao.pokemon.pokemon_ai.Pokemon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;

public class DataReader {
    @Value("classpath:pokedex.json")
    private Resource filePath;

    private final SimpleVectorStore vectorStore;

    public DataReader(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() {
        Assert.notNull(filePath, "Path must not be null");

        // Add logging to check the file path
        System.out.println("File path: " + filePath);

        try {
            // String resource = filePath.getFile().getPath();
            JsonReader jsonReader = new JsonReader(filePath, "pokedex_number");
            // , "name", "japanese_name1", "generation", "status", "species", "type_number", "type_1", "type_2", "height_m", "weight_kg", 
            // "abilities_number", "total_points", "hp", "attack", "defense", "sp_attack", "sp_defense", "speed", "catch_rate", "base_friendship",
            // "base_experience", "growth_rate", "egg_type_number", "egg_type_1", "egg_type_2", "percentage_male", "egg_cycles", "against_normal1", "against_fire1",
            // "against_water1", "against_electric1", "against_grass1", "against_ice1", "against_fight1", "against_poison1", "against_ground1", "against_flying1", 
            // "against_psychic1", "against_bug1", "against_rock1", "against_ghost1", "against_dragon1", "against_dark1", "against_steel1", "against_fairy1",
            // "smogon_description", "bulba_description", "moves", "ability_1", "ability_2", "ability_hidden", "ability_1_description", "ability_2_description",
            // "ability_hidden_description");

            //return jsonReader.get();
            List<Document> documents = jsonReader.get();
            

            TokenTextSplitter textSplitter = new TokenTextSplitter();
            documents = textSplitter.apply(documents);

            vectorStore.add(documents);
            Path path = Paths.get("src", "main", "resources", "data");
            String absPath = path.toFile().getAbsolutePath() + "/vectorstore.json";
            File vectorStoreFile = new File(absPath);
            System.out.println(vectorStoreFile.createNewFile());
            vectorStore.save(vectorStoreFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // if (!resource.exists()) {
        //     throw new IllegalArgumentException("File not found: " + filePath);
        // }
        
        
    }
}
