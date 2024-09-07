package com.andycao.pokemon.pokemon_ai.AI;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BattleStateDto;
import com.andycao.pokemon.pokemon_ai.PartyStateDto;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class BattleController {
    @GetMapping("/ai/battle/state")
    public BattleStateDto getBattleState() {
        BattleStateDto battleState = null;
        try {
            battleState = new BattleStateDto(BattleManager.getInstance().getPlayerPokemon().generateStateDto(), BattleManager.getInstance().getBotPokemon().generateStateDto());
        } catch (InvalidIdentifierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return battleState;
    }

    @GetMapping("/ai/battle/party")
    public PartyStateDto getPartyState() {
        PartyStateDto partyState = null;
        try {
            partyState = new PartyStateDto();
        }
        catch (InvalidIdentifierException e) {
            e.printStackTrace();
        } 

        return partyState;
    }

    @PostMapping("/ai/battle/move")
    public ResponseEntity<String> useMove(@RequestBody Map<String, String> payload) {
        String action = payload.get("action");
        System.out.println(action);

        return ResponseEntity.ok("Success");
    }
}
