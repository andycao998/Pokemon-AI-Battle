package com.andycao.pokemon.pokemon_ai.AI;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
// import org.springframework.ai.embedding.EmbeddingModel;
// import org.springframework.ai.vectorstore.SimpleVectorStore;
// import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BattleService;
import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;

@SpringBootApplication
public class PokemonAiApplication implements CommandLineRunner {
	private final RagConsoleService ragService;

    public PokemonAiApplication(RagConsoleService ragService) {
        this.ragService = ragService;
    }

	// private final PokemonAiService aiService;

    // public PokemonAiApplication(PokemonAiService aiService) {
    //     this.aiService = aiService;
    // }

	public static void main(String[] args) {
		SpringApplication.run(PokemonAiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PokemonAiService aiService = new PokemonAiService(ragService);
		ServerEventsController eventsController = new ServerEventsController();

		BattleService battleService = new BattleService();
		BattleManager.getInstance().setAiService(aiService);
		BattleManager.getInstance().setEventsController(eventsController);
		BattleManager.getInstance().createHandlers();
		battleService.startBattle();
	}
}

// @Configuration
// class AppConfig {
// 	@Bean
// 	VectorStore	vectorStore(EmbeddingModel embeddingModel) {
// 		return new SimpleVectorStore(embeddingModel);
// 	}
// }
