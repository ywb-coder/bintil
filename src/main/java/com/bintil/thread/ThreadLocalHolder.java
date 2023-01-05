package com.bintil.thread;

import java.util.HashMap;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/5 10:28
 */
@SuppressWarnings("unused")
public class ThreadLocalHolder {

    private final static ThreadLocal<HashMap<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(HashMap<String, Object> stringObjectHashMap) {
        THREAD_LOCAL.set(stringObjectHashMap);
    }

    public static HashMap<String, Object> get() {
        HashMap<String, Object> stringObjectHashMap = THREAD_LOCAL.get();
        clear();
        return stringObjectHashMap;
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
