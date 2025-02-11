package com.andycao.pokemon.pokemon_ai.AI;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BattleService;
import com.andycao.pokemon.pokemon_ai.BattleStateDto;
import com.andycao.pokemon.pokemon_ai.PartyStateDto;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;

import jakarta.servlet.http.HttpSession;

// Serves as an endpoint for fetch requests from frontend
@RestController
public class BattleController {
    @PostMapping("/ai/battle/start")
    public ResponseEntity<String> startBattle(HttpSession session) {
        System.out.println(BattleService.getInstance().initializeBattle(session.getId()));
        session.invalidate();
        BattleManager.getInstance().wait(3000);
        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }

    @GetMapping("/ai/battle/session")
    public ResponseEntity<Boolean> checkBattleSession(HttpSession session) {
        Boolean battleExists = BattleService.getInstance().isActiveBattle(session.getId());
        return ResponseEntity.ok(battleExists);
    }

    @GetMapping("/ai/battle/state")
    public BattleStateDto getBattleState() {
        BattleStateDto battleState = null;

        try {
            battleState = new BattleStateDto(BattleManager.getInstance().getPlayerPokemon().generateStateDto(), BattleManager.getInstance().getBotPokemon().generateStateDto());
        } 
        catch (InvalidIdentifierException e) {
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
        BattleManager.getInstance().passPlayerSelectedAction(action);

        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }

    @PostMapping("/ai/battle/switch")
    public ResponseEntity<String> switchPokemon(@RequestBody Map<String, String> payload) {
        String action = payload.get("action");
        System.out.println(action);
        BattleManager.getInstance().passPlayerSelectedPokemon(action);

        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }
}
