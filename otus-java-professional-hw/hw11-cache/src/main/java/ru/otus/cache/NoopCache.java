package ru.otus.cache;

import java.util.ArrayList;
import java.util.List;

public class NoopCache<K, V> implements HwCache<K, V> {
    private List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void remove(K key) {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    void notifyListeners(K key, V value, MyCache.Action action) {
        listeners.forEach(listener -> listener.notify(key, value, action.getName()));
    }
}
