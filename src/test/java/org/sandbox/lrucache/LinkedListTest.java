package org.sandbox.lrucache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LinkedListTest {


    private Node<String, String> firstNode;
    private Node<String, String> secondNode;
    private Node<String, String> thirdNode;

    private LinkedList<String, String> linkedList;

    @BeforeEach
    void setUp() {

        linkedList = new LinkedList<>();

        firstNode = new Node<>("one", "one");
        secondNode = new Node<>("second", "second");
        thirdNode = new Node<>("three", "three");

    }

    @Test
    void test_add_first_item_expect_head_and_tail_the_same() {
        linkedList.add(firstNode);
        assertEquals(linkedList.getHead(), firstNode);
        assertEquals(firstNode, linkedList.getTail());
    }

    @Test
    void test_add_item_expect_in_head() {
        linkedList.add(secondNode);
        linkedList.add(firstNode);
        assertEquals(linkedList.getHead(), firstNode);
        assertEquals(linkedList.getHead().getNext(), secondNode);
        assertEquals(linkedList.getHead().getNext().getPrev(), firstNode);
        assertEquals(secondNode, linkedList.getTail());
    }

    @Test
    void test_add_item_expect_head_points_to_previous() {
        linkedList.add(secondNode);
        linkedList.add(firstNode);
        assertEquals(linkedList.getHead().getNext(), secondNode);
    }


    @Test
    void test_add_item_expect_previous_points_to_head() {
        linkedList.add(secondNode);
        linkedList.add(firstNode);
        assertEquals(linkedList.getHead().getNext().getPrev(), firstNode);
    }

    @Test
    void test_add_expect_all_nodes_reachable() {
        LinkedList<String, String> testList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Node<String, String> node = new Node<>("node_" + i, "node_" + i);
            testList.add(node);
        }

        Node<String, String> current = testList.getHead();
        for (int i = 9; i >= 0; i--) {
            assertEquals("node_" + i, current.getValue());
            current = current.getNext();
        }
        assertEquals("node_0", testList.getTail().getValue());
    }

    @Test
    void test_remove_head_expect_head_updated() {


        linkedList.add(thirdNode);
        linkedList.add(secondNode);
        linkedList.add(firstNode);

        linkedList.remove(firstNode);
        assertEquals(linkedList.getHead(), secondNode);
        assertNull(linkedList.getHead().getPrev());
    }

    @Test
    void test_remove_tail_expect_tail_updated() {

        linkedList.add(thirdNode);
        linkedList.add(secondNode);
        linkedList.add(firstNode);

        linkedList.remove(thirdNode);
        assertEquals(linkedList.getTail(), secondNode);
        assertNull(linkedList.getTail().getNext());
        assertNull(thirdNode.getNext());
        assertNull(thirdNode.getPrev());
    }

    @Test
    void test_remove_middle_expect_removed() {
        linkedList.add(thirdNode);
        linkedList.add(secondNode);
        linkedList.add(firstNode);
        linkedList.remove(secondNode);
        assertNull(secondNode.getPrev());
        assertNull(secondNode.getNext());
    }
}