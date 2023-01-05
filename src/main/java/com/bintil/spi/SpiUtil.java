package com.bintil.spi;

import java.util.ServiceLoader;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/5 10:32
 */
public class SpiUtil {

    /**
     * 通过spi获取类
     * @param tClass 类型
     * @param <T> 类型
     */
    public static <T> ServiceLoader<T> spiLoader(Class<T> tClass) {
        return ServiceLoader.load(tClass);
    }

}
