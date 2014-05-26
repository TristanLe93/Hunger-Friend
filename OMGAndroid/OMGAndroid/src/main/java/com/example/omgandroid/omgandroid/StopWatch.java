package com.example.omgandroid.omgandroid;

/**
 * This class acts like a typical stopwatch, used to log performance times of
 * activities.
 */
public class StopWatch {
    private static long startTime = 0;
    private static long endTime = 0;

    public static void start() {
        startTime = android.os.SystemClock.uptimeMillis();
    }

    public static void stop() {
        endTime = android.os.SystemClock.uptimeMillis();
    }

    public static long getTime() {
        return endTime - startTime;
    }
}
