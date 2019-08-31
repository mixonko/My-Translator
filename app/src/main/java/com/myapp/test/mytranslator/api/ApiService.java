package com.myapp.test.mytranslator.api;

import com.myapp.test.mytranslator.entity.TranslatedText;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/v1.5/tr.json/translate")
    Call<TranslatedText> getTranslatedText(@Query("key")String key, @Query("text") String text, @Query("lang") String lang);
}
