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

import jakarta.annotation.PostConstruct;

/*
 

    UNUSED: switched to manual curation of documents


*/

public class DataReader {
    @Value("classpath:pokedex.json")
    private Resource filePath;

    private final SimpleVectorStore vectorStore;

    public DataReader(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() {
        try {
            if (filePath == null) {
                throw new IOException("File path is null");
            }

            System.out.println("File path: " + filePath); // Add logging to check the file path

            JsonReader jsonReader = new JsonReader(filePath, "pokedex_number"); // Split entries into Pokemon by Pokedex number

            List<Document> documents = jsonReader.get();

            TokenTextSplitter textSplitter = new TokenTextSplitter();
            documents = textSplitter.apply(documents);

            vectorStore.add(documents);
            Path path = Paths.get("src", "main", "resources", "data");
            String absPath = path.toFile().getAbsolutePath() + "/vectorstore.json";
            File vectorStoreFile = new File(absPath);
            System.out.println(vectorStoreFile.createNewFile());
            vectorStore.save(vectorStoreFile);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
