package com.andycao.pokemon.pokemon_ai.AI;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andycao.pokemon.pokemon_ai.Battle;
import com.andycao.pokemon.pokemon_ai.BattleContextHolder;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BattleService;
import com.andycao.pokemon.pokemon_ai.BattleStateDto;
import com.andycao.pokemon.pokemon_ai.PartyStateDto;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;

import jakarta.servlet.http.HttpSession;

// Serves as an endpoint for fetch requests from frontend
@RestController
public class BattleController {
    // Notify backend to generate a battle instance, otherwise server is idle
    @PostMapping("/ai/battle/start")
    public ResponseEntity<String> startBattle(HttpSession session) {
        String sessionId = session.getId();

        // if (BattleContextgetSessionById(sessionId) != null) {
        //     session.invalidate();
        //     return ResponseEntity.ok("Removed duplicate");
        // }

        // Start an independent battle for each connection
        Thread battleInstance = new Thread(() -> {
            BattleContextHolder.set(BattleContextHolder.get(), sessionId); // Add battle to holder and concurrent hashmap
            BattleContextHolder.get().setSessionId(sessionId); // Label battle by sessionId
            
            System.out.println("Starting battle for session: " + sessionId);
            BattleService.getInstance().initializeBattle(sessionId);

            BattleContextHolder.remove();
        });

        battleInstance.start();
        BattleManager.getInstance().wait(2000); // Delay to allow for battle to load and start before frontend attempts to grab battle state
        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }

    // Remove inactive battles on browser reload/close
    @PostMapping("/ai/battle/end")
    public ResponseEntity<String> endSession(HttpSession session) {
        String sessionId = session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);
        
        BattleContextHolder.set(context, sessionId);
        BattleContextHolder.remove();

        System.out.println("Ended battle for session: " + sessionId);

        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }

    // Check for existing battle with same sessionId
    @GetMapping("/ai/battle/session")
    public ResponseEntity<Boolean> checkBattleSession(HttpSession session) {
        Boolean battleExists = BattleContextHolder.getSessionById(session.getId()) != null;
        return ResponseEntity.ok(battleExists);
    }

    // Return every resource needed to load frontend display
    @GetMapping("/ai/battle/state")
    public BattleStateDto getBattleState(HttpSession session) {
        String sessionId = session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);

        BattleStateDto battleState = null;

        BattleContextHolder.set(context, sessionId);

        try {
            battleState = new BattleStateDto(BattleManager.getInstance().getPlayerPokemon().generateStateDto(), 
                                             BattleManager.getInstance().getBotPokemon().generateStateDto());
        } 
        catch (InvalidIdentifierException e) {
            e.printStackTrace();
        }

        return battleState;
    }

    // Return every resource needed to load frontend's party interface
    @GetMapping("/ai/battle/party")
    public PartyStateDto getPartyState(HttpSession session) {
        String sessionId = session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);

        PartyStateDto partyState = null;

        BattleContextHolder.set(context, sessionId);

        try {
            partyState = new PartyStateDto();
        }
        catch (InvalidIdentifierException e) {
            e.printStackTrace();
        } 

        return partyState;
    }

    // Endpoint to receive move picked by user
    @PostMapping("/ai/battle/move")
    public ResponseEntity<String> useMove(HttpSession session, @RequestBody Map<String, String> payload) {
        String sessionId = session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);

        String action = payload.get("action");
        System.out.println(action);

        BattleContextHolder.set(context, sessionId);

        BattleManager.getInstance().passPlayerSelectedAction(action);

        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }

    // Endpoint to receive Pokemon switch in picked by user
    @PostMapping("/ai/battle/switch")
    public ResponseEntity<String> switchPokemon(HttpSession session, @RequestBody Map<String, String> payload) {
        String sessionId = session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);

        String action = payload.get("action");
        System.out.println(action);

        BattleContextHolder.set(context, sessionId);

        BattleManager.getInstance().passPlayerSelectedPokemon(action);

        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }
}
