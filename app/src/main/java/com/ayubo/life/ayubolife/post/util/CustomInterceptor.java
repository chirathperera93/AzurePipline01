package com.ayubo.life.ayubolife.post.util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomInterceptor implements Interceptor {


    private String accessToken;

    public CustomInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request.newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build());
    }
}
