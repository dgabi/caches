package org.sandbox.lrucache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LinkedListLRUCacheTest {

    /**
     * Test plan
     * <p>
     * Test_1: Cache miss:
     * Condition: When cache is empty and value exists in the cold store
     * Actions: Load key/value into cache from cold store
     * Returns: value for requested key
     * <p>
     * Test_2: Cache hit
     * Situation: When value exists in the cache
     * Actions: verify there's no attempt to load from cold store
     * Return: value for requested key
     * <p>
     * Test_3: No value present in cache or cold store
     * Situation: When value doesn't exit, expect cache load attempt from cold store, expect return optional empty
     * Actions: verify load attempt from cold store
     * Return: empty optional
     * <p>
     * Test_4: Cache eviction
     * Situation: When cache is full and get() triggers cache miss
     * Actions: verify eviction, verify load from cold store
     * Return: value for requested key
     *
     * <p>
     * Test_5: Test cache LRU correctness
     * Situation: Cache hits move keys higher in the rank
     * Actions:
     */


    private LinkedListLRUCache<String, Integer> lruCache;
    @Mock
    private DataProvider<String, Integer> dataProviderMock;


    @BeforeEach
    public void setUp() {
        initMocks(this);
        when(dataProviderMock.get("zero")).thenReturn(Optional.of(0));
        when(dataProviderMock.get("one")).thenReturn(Optional.of(1));
        when(dataProviderMock.get("two")).thenReturn(Optional.of(2));
        when(dataProviderMock.get("three")).thenReturn(Optional.of(3));
        when(dataProviderMock.get("four")).thenReturn(Optional.of(4));

        lruCache = new LinkedListLRUCache<>(4, dataProviderMock);
    }

    /**
     * Test_1: Cache miss:
     * Condition: When cache is empty and value exists in the cold store
     * Actions: Load key/value into cache from cold store
     * Returns: value for requested key
     */

    @Test
    public void test1_cache_miss() {
        Optional<Integer> zero = lruCache.get("zero");
        Assertions.assertTrue(zero.isPresent());
        assertEquals(0, (int) zero.get());
        verify(dataProviderMock, times(1)).get("zero");
    }


    /**
     * Test_2: Cache hit
     * Situation: When value exists in the cache
     * Actions: verify there's no attempt to load from cold store
     * Return: value for requested key
     */
    @Test
    void test_cache_hit() {
        Optional<Integer> zero = lruCache.get("zero");
        Assertions.assertTrue(zero.isPresent());
        assertEquals(0, (int) zero.get());
        verify(dataProviderMock, times(1)).get("zero");

        zero = lruCache.get("zero");
        Assertions.assertTrue(zero.isPresent());
        assertEquals(0, (int) zero.get());
        verifyNoMoreInteractions(dataProviderMock);

    }

    /**
     * Test_3: No hit
     * <p/>
     * Situation: When value doesn't exit, expect cache load attempt from cold store<br/>
     * Actions: verify load attempt from cold store<br/>
     * Return: empty optional</br>
     */
    @Test
    void test_no_hit() {
        Optional<Integer> zero = lruCache.get("none");
        Assertions.assertFalse(zero.isPresent());
        verify(dataProviderMock, times(1)).get("none");
    }


    /**
     * Test_4: Cache eviction
     * Situation: When cache is full and get triggers cache miss
     * Actions: verify eviction, verify load from cold store
     * Return: value for requested key
     */
    @Test
    void test_cache_eviction() {

        // fill up the cache
        lruCache.get("zero");
        lruCache.get("one");
        lruCache.get("two");
        lruCache.get("three");
        lruCache.get("four");

        Optional<Integer> zero = lruCache.get("zero");
        Assertions.assertTrue(zero.isPresent());
        assertEquals(0, (int) zero.get());
        verify(dataProviderMock, times(1)).get("one");
        verify(dataProviderMock, times(1)).get("two");
        verify(dataProviderMock, times(1)).get("three");
        verify(dataProviderMock, times(1)).get("four");
        verify(dataProviderMock, times(2)).get("zero");
        verifyNoMoreInteractions(dataProviderMock);
    }

    @Test
    void test_cache_hits_lru() {
        lruCache.get("zero");
        lruCache.get("one");
        lruCache.get("two");
        lruCache.get("three");
        lruCache.get("zero");
        lruCache.get("three");
        lruCache.get("zero");
        lruCache.get("one");
        lruCache.get("zero");
        lruCache.get("one");
        lruCache.get("two");
        lruCache.get("three");
        lruCache.get("four");
        lruCache.get("two");
        lruCache.get("three");
        lruCache.get("four");
    }

    @Test
    void test_get_non_existing_key_expect_empty() {
        Optional<Integer> missing = lruCache.get("missing");
        assertFalse(missing.isPresent());
    }

    @Test
    void test_get_when_key_is_null_return_empty() {
        Optional<Integer> missing = lruCache.get(null);
        assertFalse(missing.isPresent());
    }
}