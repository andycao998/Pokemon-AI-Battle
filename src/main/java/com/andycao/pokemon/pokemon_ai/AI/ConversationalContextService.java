package com.andycao.pokemon.pokemon_ai.AI;

import java.util.List;

public interface ConversationalContextService {
    List<String> fetchAllContextIds();

    String preparePromptWithContextHistory(final String contextId, final String message);
}
