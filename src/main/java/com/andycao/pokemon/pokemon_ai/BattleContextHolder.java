package com.andycao.pokemon.pokemon_ai;

import java.util.concurrent.ConcurrentHashMap;

public class BattleContextHolder {
    private static ThreadLocal<Battle> holder = InheritableThreadLocal.withInitial(() -> new Battle());
    private static final ConcurrentHashMap<String, Battle> battleContextMap = new ConcurrentHashMap<>();

    // Identify battle instance BattleController endpoint wants to modify/get information from
    public static void set(Battle instance, String sessionId) {
        if (instance != null) {
            holder.set(instance);
            battleContextMap.put(sessionId, instance);
        }
    }

    public static Battle get() {
        return holder.get();
    }

    public static Battle getSessionById(String sessionId) {
        return battleContextMap.get(sessionId);
    }

    // Remove from holder and map to prevent memory leaks
    public static void remove() {
        Battle context = get();

        if (context != null) {
            battleContextMap.values().remove(context);
        }

        holder.remove();
    }
}
