package com.gmail.theslavahero.ai.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author Yevhenii Filatov
 * @since 12/25/20
 */

@Slf4j
public final class CommonUtils {
    private CommonUtils() {
        throw new UnsupportedOperationException("utility class");
    }

    public static void randomSleep(int fromMillis, int toMillis) {
        try {
            long random = randomLong(fromMillis, toMillis);
            Thread.sleep(random);
        } catch (InterruptedException e) {
            interruptThread();
        }
    }

    private static void interruptThread() {
        log.error("Thread #{} was interrupted unexpectedly", Thread.currentThread().getId());
        Thread.currentThread().interrupt();
    }

    public static void sleepFor(long seconds) {
        sleep(seconds * 1000);
    }

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    public static long randomLong(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }
}
