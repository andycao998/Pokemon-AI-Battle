package com.andycao.pokemon.pokemon_ai.AI;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andycao.pokemon.pokemon_ai.Battle;
import com.andycao.pokemon.pokemon_ai.BattleContextHolder;
import com.andycao.pokemon.pokemon_ai.BattleManager;
import com.andycao.pokemon.pokemon_ai.BattleService;
import com.andycao.pokemon.pokemon_ai.BattleStateDto;
import com.andycao.pokemon.pokemon_ai.PartyStateDto;
import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;

// Serves as an endpoint for fetch requests from frontend
@RestController
public class BattleController {
    // Notify backend to generate a battle instance, otherwise server is idle
    @PostMapping("/ai/battle/start")
    public ResponseEntity<String> startBattle(@RequestParam String id) {
        String sessionId = id; //session.getId();

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

        long startTime = System.currentTimeMillis();
        int timeout = 120000; // 120 seconds max wait time

        // Wait for battle instance to finish initializing before notifying frontend to grab battle state
        while (true) {
            Battle context = BattleContextHolder.getSessionById(sessionId);

            if (context != null) {
                BattleContextHolder.set(context, sessionId);

                if (BattleContextHolder.get().getBattleReady()) {
                    break;
                }
            }

            if (System.currentTimeMillis() - startTime > timeout) {
                throw new RuntimeException("Battle initialization timeout for session: " + sessionId);
            }

            try {
                Thread.sleep(250);
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Battle initialization interrupted", e);
            }
        }

        return ResponseEntity.ok("Success"); // WIP: generic message to notify frontend to begin
    }

    // Remove inactive battles on browser reload/close
    @PostMapping("/ai/battle/end")
    public ResponseEntity<String> endSession(@RequestParam String id) {
        String sessionId = id; //session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);
        
        BattleContextHolder.set(context, sessionId);
        BattleContextHolder.remove();

        System.out.println("Ended battle for session: " + sessionId);

        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }

    // Check for existing battle with same sessionId
    @GetMapping("/ai/battle/session")
    public ResponseEntity<Boolean> checkBattleSession(@RequestParam String id) {
        Boolean battleExists = BattleContextHolder.getSessionById(id) != null;
        return ResponseEntity.ok(battleExists);
    }

    // Return every resource needed to load frontend display
    @GetMapping("/ai/battle/state")
    public BattleStateDto getBattleState(@RequestParam String id) {
        String sessionId = id; //session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);

        BattleStateDto battleState = null;

        BattleContextHolder.set(context, sessionId);
        System.out.println("Attempting to grab state of session: " + sessionId);

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
    public PartyStateDto getPartyState(@RequestParam String id) {
        String sessionId = id; //session.getId();
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
    public ResponseEntity<String> useMove(@RequestBody Map<String, String> payload) {
        String sessionId = payload.get("id"); //session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);

        String action = payload.get("action");
        System.out.println(action);

        BattleContextHolder.set(context, sessionId);

        BattleManager.getInstance().passPlayerSelectedAction(action);

        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }

    // Endpoint to receive Pokemon switch in picked by user
    @PostMapping("/ai/battle/switch")
    public ResponseEntity<String> switchPokemon(@RequestBody Map<String, String> payload) {
        String sessionId = payload.get("id"); //session.getId();
        Battle context = BattleContextHolder.getSessionById(sessionId);

        String action = payload.get("action");
        System.out.println(action);

        BattleContextHolder.set(context, sessionId);

        BattleManager.getInstance().passPlayerSelectedPokemon(action);

        return ResponseEntity.ok("Success"); // WIP: Error and meaningless
    }
}
