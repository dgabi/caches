package org.sandbox.lrucache;


/**
 * doubly LinkedList implementation
 */
public final class LinkedList<K, V> {

    private Node<K, V> head, tail;

    public LinkedList() {
        tail = null;
        head = null;
    }

    /**
     * Returns the linked list head
     *
     * @return the node that head points to
     */
    public Node<K, V> getHead() {
        return head;
    }

    /**
     * Returns the linked list tail
     *
     * @return the node that tail points to
     */
    public Node<K, V> getTail() {
        return tail;
    }

    /**
     * Adds the node to the head of the linked list. The new head will point to previous head.
     *
     * @param newNode node to be added in the list
     */
    public void add(Node<K, V> newNode) {
        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
            return;
        } else if (head == null) {
            throw new IllegalStateException("Head found null. This should not happen");
        } else if (tail == null) {
            throw new IllegalStateException("Tail found null. This should not happen");
        }
        newNode.setNext(head);
        head.setPrev(newNode);
        head = newNode;
    }

    public void remove(Node<K, V> node) {
        if (head == node) {
            Node<K, V> next = node.getNext();
            next.setPrev(null);
            head = next;
            return;
        }
        if (node == tail) {
            Node<K, V> tmp = this.tail;
            Node<K, V> prev = node.getPrev();
            prev.setNext(null);
            this.tail = prev;
            tmp.setPrev(null);
            tmp.setNext(null);
            return;
        }
        Node<K, V> prev = node.getPrev();
        Node<K, V> next = node.getNext();
        next.setPrev(prev);
        prev.setNext(next);
        node.setNext(null);
        node.setPrev(null);
    }
}
