package org.sandbox.lrucache;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BasicIntegrationTests {

    @Test
    void test() throws InterruptedException {

        final DataProvider<String, String> dataProvider = new HashMapDataProvider<>();
        loadData(dataProvider);

        final LRUCache<String, String> cache = new LinkedListLRUCache<>(1000000, dataProvider);

        final Random random = new Random();

        final RateLimiter rateLimiter = new RateLimiter(10_000, TimeUnit.SECONDS);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                int i1 = 0;
                while (i1 < 100_000) {
                    if (rateLimiter.call()) {
                        int nextInt = random.nextInt(1000_000);
                        Optional<String> s = cache.get(Integer.toString(nextInt));
                        assertTrue(s.isPresent());
                        assertEquals("value_" + nextInt, s.get());
                        i1++;
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.MINUTES);
        rateLimiter.stop();
    }


    private void loadData(DataProvider<String, String> dataProvider) {
        for (int i = 0; i < 1000000; i++) {
            dataProvider.put(Integer.toString(i), "value_" + i);
        }
    }
}
