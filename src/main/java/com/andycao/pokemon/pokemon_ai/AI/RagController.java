package com.andycao.pokemon.pokemon_ai.AI;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*


    UNUSED: See RagConsoleService


*/

@RestController
public class RagController {

    private final ChatClient aiClient;
    private final VectorStore vectorStore;
    private final ConversationalContextService contextService;

    public RagController(ChatClient aiClient, VectorStore vectorStore, ConversationalContextService contextService) {
        this.aiClient = aiClient;
        this.vectorStore = vectorStore;
        this.contextService = contextService;
    }

    @GetMapping("/contexts")
    public List<String> fetchAllContextIds() {
        return contextService.fetchAllContextIds();
    }

    @GetMapping("/ai/rag")
    public ResponseEntity<String> generateAnswer(@RequestParam(value = "contextId", defaultValue = "") String contextId,
                                                 @RequestParam(value = "message", defaultValue = "Tell me a random Pokemon fact") String message) {

        // WIP: Current version does not require memory so remove (turn history is manually tracked)
        if (contextId.isEmpty()) {
            contextId = UUID.randomUUID().toString();
        }

        String query = "";
        System.out.println(query);

        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(query).withTopK(3).withSimilarityThreshold(0.75));
        String information = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));

        var systemPromptTemplate = new SystemPromptTemplate(
            """
                You are an expert Pokemon trainer in a battle you must try your best to win. At the start of each turn, you will be provided with
                information on the events that happened last turn, your current Pokemon's information and your opponent's Pokemon's information.
                Rely primarily on the following information to choose the best action for your Pokemon each turn, and consider your opponent's side as well.
                The very first thing in your answer should be your choice enclosed in brackets: either choose a valid move
                from the Pokemon you have out, or if you decide to switch, list an unfainted Pokemon in your answer.
                Here are some examples: [THUNDERBOLT] or [SWITCH Pikachu]. If you do not choose a valid action, a random
                move will be chosen for you. Afterwards, give a short explanation of why you chose that action.

                {information}
            """
        );

        Message systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));

        PromptTemplate userPromptTemplate = new PromptTemplate("{query}");
        Message userMessage = userPromptTemplate.createMessage(Map.of("query", query));

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        if (similarDocuments.isEmpty()) {
            return ResponseEntity.ok("Unknown");
        }
        else {
            return ResponseEntity.ok(aiClient.call(prompt).getResult().getOutput().getContent());
        }
    }
}
