package com.myapp.test.mytranslator.contracts;

public interface TranslateTextContract {
    interface View{
        String getText();
        void deleteUserText();
        void setText(String resultText);
        void showNoConnection();
        void showError(Throwable t);
        void recordVoice();
    }

    interface Presenter{
        void onSwapLangButtonWasClicked();
        void onTextWasChanged();
        void onButtonRecorderWasClicked();
        void onDeleteTextButtonWasClicked();
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


