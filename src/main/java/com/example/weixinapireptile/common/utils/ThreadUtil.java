package com.example.weixinapireptile.common.utils;


public class ThreadUtil {

    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (Exception ignored) {
        }
    }
}
