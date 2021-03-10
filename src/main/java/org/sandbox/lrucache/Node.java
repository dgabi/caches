package org.sandbox.lrucache;

public class Node<K, V> {
    private K key;
    private V value;
    private Node<K, V> prev, next;

    public Node() {
        prev = null;
        next = null;
    }

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public Node<K, V> getPrev() {
        return prev;
    }

    public void setPrev(Node<K, V> prev) {
        this.prev = prev;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "org.sandbox.lrucache.Node{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
