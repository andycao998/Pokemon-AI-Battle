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
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class RagConsoleService {
    private final ChatClient aiClient;

    private OpenAiChatOptions options;

    private SystemPromptTemplate systemPromptTemplate;

    public RagConsoleService(ChatClient aiClient) {
        this.aiClient = aiClient;
        
        options = OpenAiChatOptions.builder()
                                   .withModel("gpt-4o-mini")
                                   .withTemperature(0.25f)
                                   .build();

        systemPromptTemplate = new SystemPromptTemplate(
            """
                You are an expert Pokemon trainer in a battle you must try your best to win. At the start of each turn, you will be provided with
                information on the events that happened last turn, your current Pokemon's information and your opponent's Pokemon's information.
                ONLY use the following information give to you to choose the best action for your Pokemon each turn, and consider your opponent's side as well.
                Your answer should only include your choice enclosed in brackets: either choose a valid move
                from the Pokemon you have out, or if you decide to switch, list an unfainted Pokemon in your answer.
                Here are some examples: [THUNDERBOLT] or [SWITCH Pikachu]. Do not give an explanation as to why you chose it.
                If you do not choose a valid action, a random move will be chosen for you. Do not use anything outside of what information is provided to you.

                {information}
            """
        );
    }

    public String generateAnswer(String contextId, String message, List<Document> similarDocuments) {
        // WIP: Current version does not require memory so remove (turn history is manually tracked)
        if (contextId.isEmpty()) {
            contextId = UUID.randomUUID().toString();
        }

        String query = message;

        // List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(query).withTopK(20).withSimilarityThreshold(0.79));
        String information = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));

        Message systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));

        PromptTemplate userPromptTemplate = new PromptTemplate("{query}");
        Message userMessage = userPromptTemplate.createMessage(Map.of("query", query));

        // var options = OpenAiChatOptions.builder()
        //                                .withModel("gpt-4o-mini")
        //                                .withTemperature(0.25f)
        //                                .build();

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), options);

        if (similarDocuments.isEmpty()) {
            return "Unknown";
        } 
        else {
            String response = aiClient.call(prompt).getResult().getOutput().getContent();
            System.out.println(response);
            return response;
        }
    }
}
