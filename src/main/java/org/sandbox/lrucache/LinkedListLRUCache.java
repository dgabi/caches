package org.sandbox.lrucache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LinkedListLRUCache<K, V> implements LRUCache<K, V> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<K, Node<K, V>> map;
    private final DataProvider<K, V> dataProvider;
    private final int capacity;
    private final LinkedList<K, V> linkedList = new LinkedList<>();
    private int size = 0;

    /**
     * Creates a LRU Cache backed by a LinkedList and a HashMap
     * LinkedList stores the cached data
     * HashMap tracks the presence in the cache for individual keys
     *
     * @param capacity     capacity of the cache
     * @param dataProvider Data
     */
    public LinkedListLRUCache(int capacity, DataProvider<K, V> dataProvider) {
        this.capacity = capacity;
        this.dataProvider = dataProvider;
        map = new HashMap<>();
    }

    @Override
    public synchronized Optional<V> get(final K key) {
        if (key == null) {
            return Optional.empty();
        }
        if (map.containsKey(key)) {
            Node<K, V> node = map.get(key);
            LOGGER.debug("Cache hit: {}", node);
            if (node != linkedList.getHead()) {
                linkedList.remove(node);
                linkedList.add(node);
            }
            return Optional.of(node.getValue());
        } else {
            LOGGER.debug("Cache miss for key: {}", key);
            final Optional<V> newValue = dataProvider.get(key);
            if (!newValue.isPresent()) {
                LOGGER.debug("Object not found in the cold store for key: {}", key);
                return Optional.empty();
            } else {
                LOGGER.debug("Object loaded from cold store to cache: " + newValue.get());
            }
            final Node<K, V> newNode = new Node<>(key, newValue.get());

            if (size == capacity) {
                Node<K, V> tail = linkedList.getTail();
                LOGGER.debug("Capacity limit. Will remove tail element: " + tail);
                linkedList.remove(tail);
                map.remove(tail.getKey());
                size--;
            } else {
                linkedList.add(newNode);
                map.put(key, newNode);
                size++;
            }
            return newValue;
        }
    }

    public void stats() {
        System.out.println("Cache size = " + size);
    }
}
