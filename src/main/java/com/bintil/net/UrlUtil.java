package com.bintil.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/5 11:36
 */
@SuppressWarnings("unused")
public class UrlUtil {

    /**
     * 获取网页代码（不包括外部引用）
     *
     * @param url 链接
     */
    public static String getWebPage(String url) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
