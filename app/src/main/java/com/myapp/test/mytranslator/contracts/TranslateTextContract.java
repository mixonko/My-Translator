package com.myapp.test.mytranslator.contracts;

public interface TranslateTextContract {
    interface View{
        String getText();
        void setText(String resultText);
        void showNoConnection();
        void showError(Throwable t);
    }

    interface Presenter{
        void onSwapLangButtonWasClicked();
        void onTextWasChanged();
    }

    interface Repository {
        void getTranslatedText(String userText, OnFinishedListener onFinishedListener);

        interface OnFinishedListener {
            void onFinished(String translatedText);
            void onFailure(Throwable t);
            void onNoInternetConnection();
        }
    }
}


