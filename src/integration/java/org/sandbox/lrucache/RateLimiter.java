package org.sandbox.lrucache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple rate limiter algorithm. Bucket is filled with tokens once per period.
 * Once the tokens are consumed calls will return false until the end of the period.
 */

public class RateLimiter {

    private final AtomicInteger bucket = new AtomicInteger();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    /**
     * Creates a rate limiter instance
     *
     * @param tps  - desired TPS
     * @param unit - timeUnit period
     */
    public RateLimiter(int tps, TimeUnit unit) {
        this.bucket.set(tps);
        executorService.scheduleAtFixedRate(() -> {
            bucket.set(tps);
        }, 1, 1, unit);
    }

    /**
     * Check if a call can be placed
     *
     * @return true if rate limit was not exceeded yet
     */
    public boolean call() {
        return hasCapacity();
    }

    /**
     * stop rate limiter
     */
    public void stop() {
        executorService.shutdownNow();
    }

    private boolean hasCapacity() {
        return bucket.getAndDecrement() > 0;
    }
}
