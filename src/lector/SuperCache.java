/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lector;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MI PC
 */
public class SuperCache {
    
    private static SuperCache instance;
    private Map<String, Map<String, Object>> cacheStorage;

    private SuperCache() {
        cacheStorage = new HashMap<>();
    }

    public static SuperCache getInstance() {
        if (instance == null) {
            instance = new SuperCache();
        }
        return instance;
    }

    public void addToCache(String key, Map<String, Object> data) {
        cacheStorage.put(key, data);
    }

    public Map<String, Object> getFromCache(String key) {
        return cacheStorage.get(key);
    }
}
