package com.andycao.pokemon.pokemon_ai.AI;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BattleService;
import com.andycao.pokemon.pokemon_ai.DocumentGrabber;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PokemonAiApplication implements CommandLineRunner {
	private final RagConsoleService ragService;

    public PokemonAiApplication(RagConsoleService ragService) {
        this.ragService = ragService;
    }

	public static void main(String[] args) {
		SpringApplication.run(PokemonAiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PokemonAiService aiService = new PokemonAiService(ragService);
		ServerEventsController eventsController = new ServerEventsController(); // Server sent events to communicate backend -> frontend
		DocumentGrabber documentGrabber = new DocumentGrabber();

		BattleManager.getInstance().setAiService(aiService); // Inject AI chat generation service
		BattleManager.getInstance().setEventsController(eventsController); // Inject SSE handler
		BattleService.getInstance().setDocuments(documentGrabber); // Inject document grabber for mapping between Pokemon names to ids
	}
}
