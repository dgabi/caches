package org.sandbox.lrucache;

import java.util.Optional;

/**
 * interface for cache loader
 *
 * @param <K>
 * @param <V>
 */
public interface DataProvider<K, V> {
    Optional<V> get(K key);

    void put(K key, V value);
}
