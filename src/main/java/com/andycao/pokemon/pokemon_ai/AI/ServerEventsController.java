package com.andycao.pokemon.pokemon_ai.AI;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.andycao.pokemon.pokemon_ai.BattleManager;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
//@RequestMapping("/server-events")
public class ServerEventsController {
    private static List<SseEmitter> emitters = new CopyOnWriteArrayList<SseEmitter>();

    private static List<String> unreceivedMessages = new CopyOnWriteArrayList<String>();

    @CrossOrigin
    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public synchronized SseEmitter subscribe() {
        // if (emitters.size() > 0) {
        //     return new SseEmitter(100L);
        // }

        SseEmitter sseEmitter = new SseEmitter(-1L);
        try {
            sseEmitter.send(SseEmitter.event().name("Initialize"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        if (emitters.size() > 0) {
            emitters.get(0).complete();
            emitters.remove(0);
            System.out.println("Manually completed emitter");
        }

        sseEmitter.onCompletion(() -> {
            emitters.remove(sseEmitter); 
            System.out.println("Emitter completed");
        });

        sseEmitter.onTimeout(() -> {
            emitters.remove(sseEmitter); 
            System.out.println("Emitter timed out");
        });

        emitters.add(sseEmitter);

        return sseEmitter;
    }

    private void sendUnreceivedEvents(SseEmitter emitter) {
        if (unreceivedMessages.size() != 0) {
            for (String message : unreceivedMessages) {
                try {
                    emitter.send(SseEmitter.event().name("message").data(message));
                } 
                catch (IOException e) {
                    emitters.remove(emitter);
                }
            }

            unreceivedMessages.clear();
        }
    }

    @GetMapping(value = "/dispatch")
    public void sendEvent(String message) {
        if (emitters.size() == 0) {
            unreceivedMessages.add(message);
        }

        // Send event to all clients
        for (SseEmitter emitter : emitters) {
            try {
                sendUnreceivedEvents(emitter);

                emitter.send(SseEmitter.event().name("message").data(message));
            } 
            catch (Exception e) {
                emitters.remove(emitter);  // Remove emitter if there's an error
            }
        }
    }
}