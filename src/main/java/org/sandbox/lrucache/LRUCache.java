package org.sandbox.lrucache;

import java.util.Optional;

public interface LRUCache<K, V> {
    Optional<V> get(K key);
}
