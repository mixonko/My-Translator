package com.myapp.test.mytranslator.repository;

import com.myapp.test.mytranslator.api.RetroClient;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.model.TranslatedText;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateRepository implements TranslateTextContract.Repository {
    private String translatedText;

    @Override
    public void getTranslatedText(String userText, final OnFinishedListener onFinishedListener) {
        if (InternetConnection.checkConnection(MyApplication.getAppContext())) {
            RetroClient.getApiService().getTranslatedText(RetroClient.API_KEY, userText, "ru").enqueue(new Callback<TranslatedText>() {
                @Override
                public void onResponse(Call<TranslatedText> call, Response<TranslatedText> response) {
                    if (response.isSuccessful()) {
                        translatedText = response.body().getText().get(0);
                        onFinishedListener.onFinished(translatedText);
                    }
                }

                @Override
                public void onFailure(Call<TranslatedText> call, Throwable t) {
                    onFinishedListener.onFailure(t);
                }
            });

        }else{
            onFinishedListener.onNoInternetConnection();
        }
    }
}
