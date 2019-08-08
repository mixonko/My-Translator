package com.myapp.test.mytranslator.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static final String ROOT_URL = "http://api.now-android.ru";
    public static final String API_KEY = "trnsl.1.1.20190808T171430Z.b7cc4d32dc6cc29c.100b59d52c20bad4b7ad39a174f2c3fbedfa41bd";


    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
