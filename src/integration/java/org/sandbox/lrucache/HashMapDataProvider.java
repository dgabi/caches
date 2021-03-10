package org.sandbox.lrucache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Thin wrapper around a HashMap that acts as a DataProvider to test LRU cache
 *
 * @param <K> key
 * @param <V> value
 */
public class HashMapDataProvider<K, V> implements DataProvider<K, V> {

    private final Map<K, V> store = new HashMap<>();

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(store.get(key));
    }

    @Override
    public void put(K key, V value) {
        store.put(key, value);
    }
}
