package com.andycao.pokemon.pokemon_ai.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversationalContextServiceImpl implements ConversationalContextService {
    private HashMap<String, List<String>> contextStore = new HashMap<String, List<String>>();

    @Override
    public List<String> fetchAllContextIds() {
        return new ArrayList<String>(contextStore.keySet());
    }

    @Override
    public String preparePromptWithContextHistory(String contextId, String message) {
        if (!contextStore.containsKey(contextId)) {
            ArrayList<String> history = new ArrayList<String>();
            history.add(message);
            contextStore.put(contextId, history);
            return message;
        }
        else {
            List<String> history = contextStore.get(contextId);
            history.add(message);
            contextStore.put(contextId, history);
        }

        StringBuilder prompt = new StringBuilder();
        List<String> history = contextStore.get(contextId);

        prompt.append(PromptConstants.PROMPT_WHAT_WERE_WE_TALKING_ABOUT);

        int length = history.size() - 1;
        for (int i = 0; i < length; i++) {
            prompt.append(padStringWithDelimiter(history.get(i)));
        }

        prompt.append(PromptConstants.PROMPT_DELIMITER_FOR_HISTORICAL_CONTEXT);
        prompt.append(PromptConstants.PROMPT_USE_CONTEXT_IF_NEEDED);
        prompt.append(PromptConstants.PROMPT_THE_CURRENT_QUESTION);
        prompt.append(history.get(length));

        return prompt.toString();
    }
    
    private String padStringWithDelimiter(String content) {
        return PromptConstants.PROMPT_DELIMITER + content + PromptConstants.PROMPT_DELIMITER;
    }
}
