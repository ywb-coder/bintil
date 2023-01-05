package com.bintil.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/5 11:43
 */

@SuppressWarnings("unused")
public class IoUtil {

    /**
     * 复制小文件
     *
     * @param sourcePath 源文件地址
     * @param targetPath 复制文件地址
     */
    public static void smallFileCopy(String sourcePath, String targetPath) {
        //java.io.RandomAccessFile类，可以设置读、写模式的IO流类。
        //"rw"表示：读、写--输出流，需要读、写。
        try (RandomAccessFile randomAccessFile1 = new RandomAccessFile(sourcePath, "r");
             RandomAccessFile randomAccessFile2 = new RandomAccessFile(targetPath, "rw");
             FileChannel fileChannel1 = randomAccessFile1.getChannel();
             FileChannel fileChannel2 = randomAccessFile2.getChannel()) {
            // 获取文件的大小
            long size = fileChannel1.size();
            // 直接把硬盘中的文件映射到内存中
            MappedByteBuffer mappedByteBuffer1 = fileChannel1.map(FileChannel.MapMode.READ_ONLY, 0, size);
            MappedByteBuffer mappedByteBuffer2 = fileChannel2.map(FileChannel.MapMode.READ_WRITE, 0, size);
            // 循环读取数据
            for (long i = 0; i < size; i++) {
                // 读取字节
                byte byt = mappedByteBuffer1.get();
                // 保存到第二个数组中
                mappedByteBuffer2.put(byt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制大文件 使用MappedByteBuffer复制超过1GB的文件
     *
     * @param sourcePath 源文件地址
     * @param targetPath 复制文件地址
     */
    public static void bigFileCopy(String sourcePath, String targetPath) {
        // 创建RandomAccessFile对象
        try (RandomAccessFile randomAccessFile1 = new RandomAccessFile(sourcePath, "r");
             RandomAccessFile randomAccessFile2 = new RandomAccessFile(targetPath, "rw");
             FileChannel fileChannel1 = randomAccessFile1.getChannel();
             FileChannel fileChannel2 = randomAccessFile2.getChannel()) {
            // 如果文件大小超过2GB，则需要分块拷贝
            // 获取文件的总大小
            long size = fileChannel1.size();
            // 定义每次文件要拷贝的大小
            long everySize = 1024 * 1024 * 512;
            // 获取需要循环的次数
            long count = size % everySize == 0 ? size / everySize : size / everySize + 1;
            for (long i = 0; i < count; i++) {
                // 定义每次开始的位置
                long startSize = i * everySize;
                // 定义每次要拷贝的大小
                long trueSize = Math.min(size - startSize, everySize);
                // 创建映射缓冲数组
                MappedByteBuffer map1 = fileChannel1.map(FileChannel.MapMode.READ_ONLY, startSize, trueSize);
                MappedByteBuffer map2 = fileChannel2.map(FileChannel.MapMode.READ_WRITE, startSize, trueSize);
                // 循环读取数据
                for (long j = 0; j < trueSize; j++) {
                    // 读数据
                    byte b = map1.get();
                    // 写数据
                    map2.put(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
