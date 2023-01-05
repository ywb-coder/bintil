package com.bintil.thread.asyc2syc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/5 10:40
 */
@SuppressWarnings("unused")
public class AwaiterHolder {

    private final static ConcurrentHashMap<Object, Awaiter> AWAITER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    public static void putAwaiter(Object key, Awaiter awaiter) {
        AWAITER_CONCURRENT_HASH_MAP.putIfAbsent(key, awaiter);
    }

    public static Awaiter removeAwaiter(Object key) {
        return AWAITER_CONCURRENT_HASH_MAP.remove(key);
    }
}
