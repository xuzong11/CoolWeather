package com.xuzhong.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 与服务器交互
 * 传入请求地址，并注册一个回调来处理服务器响应
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}