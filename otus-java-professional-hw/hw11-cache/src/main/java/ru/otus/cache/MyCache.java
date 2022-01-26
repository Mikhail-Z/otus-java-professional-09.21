package ru.otus.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private WeakHashMap<K, V> map = new WeakHashMap<>();
    private List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        map.put(key, value);
        notifyListeners(key, value, Action.PUT);
    }

    @Override
    public void remove(K key) {
        var removedValue = map.remove(key);
        notifyListeners(key, removedValue, Action.REMOVE);
    }

    @Override
    public V get(K key) {
        var value = map.get(key);
        notifyListeners(key, value, Action.GET);

        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    void notifyListeners(K key, V value, Action action) {
        listeners.forEach(listener -> listener.notify(key, value, action.getName()));
    }

    enum Action {
        GET("get"),
        PUT("put"),
        REMOVE("remove");

        private final String name;

        Action(String value) {
            this.name = value;
        }

        public String getName() {
            return name;
        }
    }
}