package com.myapp.test.mytranslator.contracts;

public interface TranslateTextContract {
    interface View{
        String getText();
        void deleteUserText();
        void setText(String resultText);
        void showNoConnection();
        void showError(Throwable t);
        void voiceInputText();
        void playUserText();
        void playResultText();
        void showButtons();
        void hideButtons();
        void swapLanguages();
    }

    interface Presenter{
        void onSwapLangButtonWasClicked();
        void onTextWasChanged();
        void onPlayUserTextwasClicked();
        void onPlayResultTextWasClicked();
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


