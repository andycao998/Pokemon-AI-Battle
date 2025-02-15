package com.andycao.pokemon.pokemon_ai.AI;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@RestController
public class ServerEventsController {
    // private static List<SseEmitter> emitters = new CopyOnWriteArrayList<SseEmitter>(); // Holds subscribers (1 for current use case)
    private static ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<String, SseEmitter>();

    // WIP: Unreceived messaging system may not be necessary
    // private static List<String> unreceivedMessages = new CopyOnWriteArrayList<String>();

    @CrossOrigin
    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public synchronized SseEmitter subscribe(HttpSession session) {
        String sessionId = session.getId();
        SseEmitter sseEmitter = new SseEmitter(-1L); // No timeout

        try {
            sseEmitter.send(SseEmitter.event().name("Initialize"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // // Replace current emitter if a new subscription request comes in
        // if (emitters.size() > 0) {
        //     emitters.get(0).complete();
        //     emitters.remove(0);
        //     System.out.println("Manually completed emitter");
        // }

        // sseEmitter.onCompletion(() -> {
        //     emitters.remove(sseEmitter); 
        //     System.out.println("Emitter completed");
        // });

        // sseEmitter.onTimeout(() -> {
        //     emitters.remove(sseEmitter); 
        //     System.out.println("Emitter timed out");
        // });

        // // emitters.add(sseEmitter);
        // emitters.put(sessionId, sseEmitter);

        // Replace the existing emitter for this session if necessary
        
        // if (oldEmitter != null) {
        //     oldEmitter.complete();
        // }

        System.out.println("New emitter subscribed for session: " + sessionId);
        
        // Attempt to send any unreceived messages
        // sendUnreceivedEvents(sessionId, sseEmitter);

        // Handle completion & timeout events
        sseEmitter.onCompletion(() -> {
            emitters.remove(sessionId);
            System.out.println("Emitter completed for session: " + sessionId);
        });

        sseEmitter.onTimeout(() -> {
            emitters.remove(sessionId);
            System.out.println("Emitter timed out for session: " + sessionId);
        });

        emitters.put(sessionId, sseEmitter);

        return sseEmitter;
    }

    // private void sendUnreceivedEvents(SseEmitter emitter) {
    //     if (unreceivedMessages.size() != 0) {
    //         for (String message : unreceivedMessages) {
    //             try {
    //                 emitter.send(SseEmitter.event().name("message").data(message));
    //             } 
    //             catch (IOException e) {
    //                 emitters.remove(emitter);
    //             }
    //         }

    //         unreceivedMessages.clear();
    //     }
    // }

    @GetMapping(value = "/dispatch")
    public void sendEvent(String sessionId, String message) {
        // if (emitters.size() == 0) {
        //     unreceivedMessages.add(message);
        // }

        SseEmitter emitter = emitters.get(sessionId);

        try {
            emitter.send(SseEmitter.event().name("message").data(message));
        }
        catch (Exception e) {
            emitters.remove(sessionId);
        }
    }
}
