package com.andycao.pokemon.pokemon_ai.AI;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import com.andycao.pokemon.pokemon_ai.TurnEventMessageBuilder;

@Service
public class PokemonAiService {
    private final RagConsoleService ragService;

    public PokemonAiService(RagConsoleService ragService) {
        this.ragService = ragService;
    }

    public String queryBot(String query, List<Document> docs) {
        String contextId = "";
        String message = query;
        return ragService.generateAnswer(contextId, message, docs);
    }
}
