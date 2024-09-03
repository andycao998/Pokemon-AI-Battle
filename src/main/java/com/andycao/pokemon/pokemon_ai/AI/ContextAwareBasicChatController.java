package com.andycao.pokemon.pokemon_ai.AI;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/ai/ragv2")
public class ContextAwareBasicChatController {
    // private final OpenAiChatModel chatClient;
    // private final ConversationalContextService contextService;

    // @Autowired
    // public ContextAwareBasicChatController(OpenAiChatModel chatClient, ConversationalContextService contextService) {
    //     this.chatClient = chatClient;
    //     this.contextService = contextService;
    // }

    // @GetMapping("/contexts")
    // public List<String> fetchAllContextIds() {
    //     return contextService.fetchAllContextIds();
    // }
    
    // @GetMapping("/chat")
    // public HashMap<String, String> chat(@RequestParam(value = "contextId", defaultValue = "") String contextId,
    //                                     @RequestParam(value = "message", defaultValue = "Tell me a random Pokemon fact") String message) {

    //     if (contextId.isEmpty()) {
    //         contextId = UUID.randomUUID().toString();
    //     }

    //     String prompt = contextService.preparePromptWithContextHistory(contextId, message);
    //     return Map.of("Generated")
    // }

    
}
