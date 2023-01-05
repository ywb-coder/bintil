package com.bintil.thread.asyc2syc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/5 10:38
 */
@SuppressWarnings("unused")
public class Awaiter {
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private Object key;
    private Object responseObj;

    public Awaiter() {
    }

    public Awaiter(Object key) {
        this.key = key;
    }

    public Object await() {
        try {
            countDownLatch.await();
            return responseObj;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object await(long time, TimeUnit timeUnit) {
        try {
            boolean await = countDownLatch.await(time, timeUnit);
            if (await) {
                return responseObj;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //确保忘记删除造成的内存溢出
            AwaiterHolder.removeAwaiter(key);
        }
        return null;
    }

    public void putResponse(Object responseObj) {
        this.responseObj = responseObj;
        countDownLatch.countDown();
    }
}
