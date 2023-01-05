package com.bintil.thread.asyc2syc;

import java.util.concurrent.TimeUnit;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/5 10:53
 */
@SuppressWarnings("all")
public class ExampleAwaiterUse {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                //模拟server向micro server发送请求，需要2s才响应结果
                TimeUnit.SECONDS.sleep(1);
                microServiceResponse2Server();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //client -> server -> micro service
        client2ServerReq2MicroService();
    }

    /**
     * 模拟前端向后端请求 http
     * 后端需要向微服务请求 tcp（异步变为同步）
     * 如果说使用的netty,使用地方在 channel.writeAndFlush(data); 之前
     */
    static void client2ServerReq2MicroService() {
        System.out.println("client 2 server (http)");
        //do something...
        System.out.println("server 2 micro service (tcp)");
        //tcp request
        final String reqId = "reqId";
        Awaiter awaiter = new Awaiter(reqId);
        AwaiterHolder.putAwaiter(reqId, awaiter);
        // channel.writeAndFlush(data); 将数据写出去之前先添加awaiter ， 之后等待结果
        Object responseObj = awaiter.await(3, TimeUnit.SECONDS);
        if (responseObj == null) {
            System.out.println("timeout");
        }
        System.out.println("real response : " + responseObj);
    }

    /**
     * 模拟tcp响应,可写在netty入栈处
     */
    static void microServiceResponse2Server() {
        //需要与上面的reqId保持一致
        final String reqId = "reqId";
        Awaiter awaiter = AwaiterHolder.removeAwaiter(reqId);
        //填充数据
        awaiter.putResponse("这是从微服务（tcp通讯设备）返回的数据");
    }

}
